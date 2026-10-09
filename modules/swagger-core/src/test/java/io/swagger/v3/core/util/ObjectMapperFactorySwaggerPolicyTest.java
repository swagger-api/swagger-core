package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.exc.InvalidFormatException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.Supplier;

import static org.testng.Assert.*;

/**
 * Options that Swagger Core sets itself. MIGRATION.md says they apply with and without Jackson 2 compatibility,
 * so every test runs in both modes.
 */
public class ObjectMapperFactorySwaggerPolicyTest extends ObjectMapperFactoryTestBase {

    @Test(dataProvider = "publicMappersInBothModes")
    public void ignoresUnknownProperties(boolean compat, String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);
        assertFalse(mapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES), l);

        KnownProperty bean = mapper.readValue(input(mapper, "{\"known\":\"kept\",\"unknown\":1}",
                "known: kept\nunknown: 1\n"), KnownProperty.class);
        assertEquals(bean.known, "kept", l);

        OpenAPI openAPI = mapper.readValue(input(mapper,
                "{\"openapi\":\"3.0.1\",\"unknown\":true}",
                "openapi: 3.0.1\nunknown: true\n"), OpenAPI.class);
        assertEquals(openAPI.getOpenapi(), "3.0.1", l);
    }

    @Test(dataProvider = "strictMapperInBothModes")
    public void strictMapperIgnoresUnknownProperties(boolean compat, String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        assertFalse(mapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES), label(compat, name));
        assertEquals(mapper.readValue("{\"known\":\"kept\",\"unknown\":1}", KnownProperty.class).known,
                "kept", label(compat, name));
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void writesEmptyBeans(boolean compat, String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);
        assertFalse(mapper.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS), l);
        var tree = mapper.readTree(mapper.writeValueAsString(new EmptyBean()));
        assertTrue(tree.isObject(), l);
        assertEquals(tree.size(), 0, l);
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void writesDatesAsText(boolean compat, String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);
        assertFalse(mapper.isEnabled(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS), l);
        assertTrue(mapper.readTree(mapper.writeValueAsString(new Date(0))).isString(), l);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(0);
        assertTrue(mapper.readTree(mapper.writeValueAsString(calendar)).isString(), l);
        assertTrue(mapper.readTree(mapper.writeValueAsString(Instant.EPOCH)).isString(), l);
        assertTrue(mapper.readTree(mapper.writeValueAsString(
                OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.ofHours(2)))).isString(), l);
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void preservesDeclarationPropertyOrder(boolean compat, String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);
        assertFalse(mapper.isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY), l);
        String output = mapper.writeValueAsString(new OrderedBean());
        assertTrue(output.indexOf("beta") < output.indexOf("alpha"), l + ": " + output);
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void excludesNullPropertiesAndMapValuesButKeepsListPositions(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String output = mapper.writeValueAsString(new NullContainer());
        var tree = mapper.readTree(output);
        String l = label(compat, name) + ": " + output;
        assertFalse(tree.has("missing"), l);
        assertFalse(tree.get("map").has("missing"), l);
        assertTrue(tree.get("map").has("present"), l);
        assertTrue(tree.get("list").get(0).isNull(), l + " (collection positions must not be removed)");
    }

    @Test(dataProvider = "publicMappersInBothModes")
    public void publicMappersWriteBigDecimalWithoutExponent(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);
        assertTrue(mapper.isEnabled(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN), l);
        var output = mapper.readTree(mapper.writeValueAsString(new BigDecimal("1E+2")));
        assertEquals(output.decimalValue().scale(), 0, l);
        assertEquals(output.decimalValue(), new BigDecimal("100"), l);
    }

    @Test(dataProvider = "strictMapperInBothModes")
    public void strictMapperKeepsDefaultBigDecimalOutput(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        assertFalse(mapper.isEnabled(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN), label(compat, name));
        assertEquals(mapper.writeValueAsString(new BigDecimal("1E+2")), "1E+2", label(compat, name));
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void enumsAreWrittenWithToStringAndReadByName(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);
        assertTrue(mapper.isEnabled(EnumFeature.WRITE_ENUMS_USING_TO_STRING), l);
        assertFalse(mapper.isEnabled(EnumFeature.READ_ENUMS_USING_TO_STRING), l);

        WireEnumContainer source = new WireEnumContainer();
        source.value = WireEnum.VALUE;
        assertEquals(mapper.readTree(mapper.writeValueAsString(source)).get("value").asString(), "wire-value", l);

        WireEnumContainer byName = mapper.readValue(input(mapper,
                "{\"value\":\"VALUE\"}", "value: VALUE\n"), WireEnumContainer.class);
        assertEquals(byName.value, WireEnum.VALUE, l);

        InvalidFormatException byToString = expectThrows(InvalidFormatException.class,
                () -> mapper.readValue(input(mapper,
                        "{\"value\":\"wire-value\"}", "value: wire-value\n"), WireEnumContainer.class));
        assertTrue(byToString.getMessage().contains("wire-value"), l + ": " + byToString.getMessage());
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void acceptsLowerCaseAndNonLetterAccessorFirstCharacters(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        Jackson2AccessorBean source = new Jackson2AccessorBean();
        source.setvalue("lower-case");
        source.set_value("non-letter");

        String output = mapper.writeValueAsString(source);
        var tree = mapper.readTree(output);
        String l = label(compat, name) + ": " + output;
        assertEquals(tree.size(), 2, l);
        assertEquals(tree.get("value").asString(), "lower-case", l);
        assertEquals(tree.get("_value").asString(), "non-letter", l);

        Jackson2AccessorBean roundTrip = mapper.readValue(output, Jackson2AccessorBean.class);
        assertEquals(roundTrip.getvalue(), "lower-case", l);
        assertEquals(roundTrip.get_value(), "non-letter", l);
    }

    /** Jackson 3 names acronym properties differently; {@code configureForJackson2()} does not restore Jackson 2. */
    @Test(dataProvider = "allMappersInBothModes")
    public void acronymAccessorsKeepJackson3PropertyNames(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);

        var getters = mapper.readTree(mapper.writeValueAsString(new AcronymGetterBean()));
        assertEquals(getters.get("URL").asString(), "u", l + ": " + getters);
        assertEquals(getters.get("IPhone").asString(), "i", l + ": " + getters);
        assertFalse(getters.has("url"), l + ": " + getters);
        assertFalse(getters.has("iphone"), l + ": " + getters);

        var is = mapper.readTree(mapper.writeValueAsString(new AcronymIsBean()));
        assertTrue(is.get("URL").asBoolean(), l + ": " + is);
        assertFalse(is.has("url"), l + ": " + is);
    }

    @Test(dataProvider = "allMappersInBothModes")
    public void nullForBoxedTypesRemainsNullAndZeroStaysValid(boolean compat, String name,
            Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = build(compat, supplier);
        String l = label(compat, name);

        PrimitiveBean boxed = mapper.readValue(
                input(mapper, "{\"boxedCount\":null,\"boxedActive\":null}", "boxedCount: null\nboxedActive: null\n"),
                PrimitiveBean.class);
        assertNull(boxed.boxedCount, l + ": null Integer must stay null");
        assertNull(boxed.boxedActive, l + ": null Boolean must stay null");

        PrimitiveBean zero = mapper.readValue(
                input(mapper, "{\"count\":0,\"active\":false}", "count: 0\nactive: false\n"), PrimitiveBean.class);
        assertEquals(zero.count, 0, l);
        assertFalse(zero.active, l);
    }
}
