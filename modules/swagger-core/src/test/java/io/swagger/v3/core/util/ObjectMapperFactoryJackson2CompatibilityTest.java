package io.swagger.v3.core.util;

import org.testng.annotations.Test;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.JsonNodeFeature;
import tools.jackson.databind.exc.MismatchedInputException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.Supplier;

import static org.testng.Assert.*;

/**
 * Options that depend on {@link ObjectMapperFactory#setJackson2Compatibility(boolean)}: Every test runs for every mapper with
 * the switch off (Jackson 3 defaults) and on (Jackson 2 compatibility).
 */
public class ObjectMapperFactoryJackson2CompatibilityTest extends ObjectMapperFactoryTestBase {

    @Test(dataProvider = "allMappersInBothModes")
    public void changedOptionsFollowTheSwitch(boolean compat, String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);

        // Jackson 3 value is "enabled", Jackson 2 compatibility disables it
        assertEquals(mapper.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES), !compat, l);
        assertEquals(mapper.isEnabled(MapperFeature.DETECT_PARAMETER_NAMES), !compat, l);
        assertEquals(mapper.isEnabled(MapperFeature.FIX_FIELD_NAME_UPPER_CASE_PREFIX), !compat, l);
        assertEquals(mapper.isEnabled(DateTimeFeature.ONE_BASED_MONTHS), !compat, l);

        // Jackson 3 value is "disabled", Jackson 2 compatibility enables it
        assertEquals(mapper.isEnabled(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES), compat, l);
        assertEquals(mapper.isEnabled(DateTimeFeature.WRITE_UTC_AS_OFFSET), compat, l);
        assertEquals(mapper.isEnabled(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS), compat, l);
        assertEquals(mapper.isEnabled(MapperFeature.USE_GETTERS_AS_SETTERS), compat, l);
        assertEquals(mapper.isEnabled(DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS), compat, l);
        assertEquals(mapper.isEnabled(SerializationFeature.FAIL_ON_ORDER_MAP_BY_INCOMPARABLE_KEY), compat, l);

        // The strict generic mapper always rejects trailing tokens, public mappers follow the switch
        assertEquals(mapper.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS),
                !compat || STRICT.equals(name), l);
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void nullForPrimitivesFailsOnlyWithoutCompatibility(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String json = input(mapper, "{\"count\":null,\"active\":null}", "count: null\nactive: null\n");

        if (compat) {
            PrimitiveBean result = mapper.readValue(json, PrimitiveBean.class);
            assertEquals(result.count, 0, label(compat, name) + ": null int must become 0");
            assertFalse(result.active, label(compat, name) + ": null boolean must become false");
        } else {
            expectThrows(MismatchedInputException.class, () -> mapper.readValue(json, PrimitiveBean.class));
        }
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void trailingTokens(boolean compat, String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String trailing = input(mapper,
                "{\"known\":\"first\"} {\"known\":\"second\"}",
                "---\nknown: first\n---\nknown: second\n");

        if (compat && !STRICT.equals(name)) {
            assertEquals(mapper.readValue(trailing, KnownProperty.class).known, "first", label(compat, name));
        } else {
            MismatchedInputException e = expectThrows(MismatchedInputException.class,
                    () -> mapper.readValue(trailing, KnownProperty.class));
            assertTrue(e.getMessage().contains("FAIL_ON_TRAILING_TOKENS"), label(compat, name) + ": " + e.getMessage());
        }
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void monthIndexIsOneBasedOnlyWithoutCompatibility(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);

        String output = mapper.writeValueAsString(Month.JANUARY);
        assertEquals(mapper.readTree(output).intValue(), compat ? 0 : 1, l + ": " + output);

        assertEquals(mapper.readValue(input(mapper, "\"JANUARY\"", "JANUARY\n"), Month.class), Month.JANUARY, l);
        assertEquals(mapper.readValue(input(mapper, "1", "1\n"), Month.class),
                compat ? Month.FEBRUARY : Month.JANUARY, l);
        if (compat) {
            assertEquals(mapper.readValue(input(mapper, "0", "0\n"), Month.class), Month.JANUARY, l);
        } else {
            expectThrows(MismatchedInputException.class,
                    () -> mapper.readValue(input(mapper, "0", "0\n"), Month.class));
        }
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void decimalTreeScaleIsStrippedOnlyWithCompatibility(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);
        int expectedScale = compat ? 2 : 4;
        String expectedText = compat ? "1.23" : "1.2300";

        var decimal = mapper.reader()
                .with(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .readTree(input(mapper, "1.2300", "1.2300\n"));
        assertEquals(decimal.decimalValue().scale(), expectedScale, l);
        assertEquals(decimal.decimalValue().compareTo(new BigDecimal("1.23")), 0, l);
        assertEquals(mapper.writeValueAsString(decimal).trim().replaceFirst("^---\\s*", ""), expectedText, l);

        assertEquals(mapper.valueToTree(new BigDecimal("1.2300")).decimalValue().scale(), expectedScale, l);
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void decimalReadsOutsideTheTreePolicyIgnoreTheSwitch(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);

        // a tree read that does not ask for BigDecimal keeps floating point numbers
        assertFalse(mapper.readTree(input(mapper, "1.2300", "1.2300\n")).isBigDecimal(), l);
        // direct binding keeps the scale of the input
        BigDecimal value = mapper.readValue(input(mapper, "1.2300", "1.2300\n"), BigDecimal.class);
        assertEquals(value.scale(), 4, l);
    }

    @Test
    public void callerCanPreserveDecimalTreeScaleWithCompatibility() {
        ObjectMapperFactory.setJackson2Compatibility(true);
        ObjectMapper mapper = ObjectMapperFactory.createJson().rebuild()
                .configure(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES, false)
                .build();
        var decimal = mapper.reader()
                .with(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .readTree("1.2300");
        assertEquals(decimal.decimalValue().scale(), 4);
        assertEquals(mapper.writeValueAsString(decimal), "1.2300");
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void durationsAreNumbersOnlyWithCompatibility(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);

        assertDuration(mapper, Duration.ofMillis(2234), "2.234", "PT2.234S", compat, l);
        assertDuration(mapper, Duration.ofMillis(-2234), "-2.234", "PT-2.234S", compat, l);
        assertDuration(mapper, Duration.ofSeconds(2, 123456789), "2.123456789", "PT2.123456789S", compat, l);
    }

    private static void assertDuration(ObjectMapper mapper, Duration duration, String number, String text,
            boolean compat, String l) {
        String output = mapper.writeValueAsString(duration);
        var tree = mapper.readTree(output);
        if (compat) {
            assertTrue(tree.isNumber(), l + ": " + output);
            assertEquals(tree.decimalValue().compareTo(new BigDecimal(number)), 0, l + ": " + output);
        } else {
            assertEquals(tree.asString(), text, l + ": " + output);
        }
        assertEquals(mapper.readValue(output, Duration.class), duration, l);
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void utcIsWrittenAsOffsetOnlyWithCompatibility(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);
        String expectedUtc = compat ? "1970-01-01T00:00:00.000+00:00" : "1970-01-01T00:00:00.000Z";

        assertEquals(mapper.readTree(mapper.writeValueAsString(new Date(0))).asString(), expectedUtc, l);

        Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utcCalendar.setTimeInMillis(0);
        assertEquals(mapper.readTree(mapper.writeValueAsString(utcCalendar)).asString(), expectedUtc, l);

        ObjectMapper offsetMapper = mapper.rebuild()
                .defaultTimeZone(TimeZone.getTimeZone("GMT+02:00"))
                .build();
        String offsetDate = offsetMapper.readTree(offsetMapper.writeValueAsString(new Date(0))).asString();
        assertTrue(offsetDate.endsWith("+02:00"), l + ": " + offsetDate);
        Calendar offsetCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+02:00"));
        offsetCalendar.setTimeInMillis(0);
        String calendarOutput = offsetMapper.readTree(offsetMapper.writeValueAsString(offsetCalendar)).asString();
        assertTrue(calendarOutput.endsWith("+02:00"), l + ": " + calendarOutput);
    }
}
