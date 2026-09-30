package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tools.jackson.core.ErrorReportConfiguration;
import tools.jackson.core.FormatSchema;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonEncoding;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.core.ObjectWriteContext;
import tools.jackson.core.StreamReadConstraints;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.core.StreamWriteConstraints;
import tools.jackson.core.TSFBuilder;
import tools.jackson.core.TokenStreamFactory;
import tools.jackson.core.Version;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.cfg.JsonNodeFeature;
import tools.jackson.databind.exc.InvalidFormatException;
import tools.jackson.databind.exc.MismatchedInputException;

import tools.jackson.core.io.ContentReference;

import java.io.DataInput;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.testng.Assert.*;

public class ObjectMapperFactoryTest {

    @DataProvider(name = "publicMappers")
    public Object[][] publicMappers() {
        return Stream.concat(Arrays.stream(jsonMappers()), Arrays.stream(yamlMappers()))
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "jsonMappers")
    public Object[][] jsonMappers() {
        return new Object[][] {
                mapper("Json.mapper", () -> Json.mapper().rebuild().build()),
                mapper("Json31.mapper", () -> Json31.mapper().rebuild().build()),
                mapper("createJson", ObjectMapperFactory::createJson),
                mapper("createJson31", ObjectMapperFactory::createJson31),
                mapper("createJson(factory)", () -> ObjectMapperFactory.createJson(new JsonFactory())),
                mapper("createJson31(factory)", () -> ObjectMapperFactory.createJson31(new JsonFactory())),
                mapper("createJsonConverter", ObjectMapperFactory::createJsonConverter),
                mapper("Json31.converterMapper", () -> Json31.converterMapper().rebuild().build()),
                mapper("create(JSON, false)", () -> ObjectMapperFactory.create(new JsonFactory(), false))
        };
    }

    @DataProvider(name = "yamlMappers")
    public Object[][] yamlMappers() {
        return new Object[][] {
                mapper("Yaml.mapper", () -> Yaml.mapper().rebuild().build()),
                mapper("Yaml31.mapper", () -> Yaml31.mapper().rebuild().build()),
                mapper("createYaml", ObjectMapperFactory::createYaml),
                mapper("createYaml31", ObjectMapperFactory::createYaml31),
                mapper("createYaml(factory)", () -> ObjectMapperFactory.createYaml(new YAMLFactory())),
                mapper("createYaml31(factory)", () -> ObjectMapperFactory.createYaml31(new YAMLFactory())),
                mapper("create(YAML, true)", () -> ObjectMapperFactory.create(new YAMLFactory(), true)),
                mapper("createYaml(false)", () -> ObjectMapperFactory.createYaml(false)),
                mapper("createYaml(true)", () -> ObjectMapperFactory.createYaml(true))
        };
    }

    @DataProvider(name = "outputMappers")
    public Object[][] outputMappers() {
        return publicMappers();
    }

    @DataProvider(name = "strictMapper")
    public Object[][] strictMapper() {
        return new Object[][] {
                mapper("buildStrictGenericObjectMapper", ObjectMapperFactory::buildStrictGenericObjectMapper)
        };
    }

    @DataProvider(name = "jackson2CompatibilityMappers")
    public Object[][] jackson2CompatibilityMappers() {
        return Stream.concat(Arrays.stream(publicMappers()), Arrays.stream(strictMapper()))
                .toArray(Object[][]::new);
    }

    private static Object[] mapper(String name, Supplier<ObjectMapper> mapper) {
        return new Object[] {name, mapper};
    }

    @Test(dataProvider = "jackson2CompatibilityMappers")
    public void stripsTrailingZeroesFromDecimalTreeValues(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES), name);

        var decimal = mapper.reader()
                .with(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .readTree(input(mapper, "1.2300", "1.2300\n"));
        assertEquals(decimal.decimalValue().scale(), 2, name);
        assertEquals(decimal.decimalValue(), new BigDecimal("1.23"), name);
        String output = mapper.writeValueAsString(decimal).trim().replaceFirst("^---\\s*", "");
        assertEquals(output, "1.23", name);
    }

    @Test(dataProvider = "jackson2CompatibilityMappers")
    public void stripsTrailingZeroesWhenCreatingDecimalNodes(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        var decimal = mapper.valueToTree(new BigDecimal("1.2300"));
        assertEquals(decimal.decimalValue().scale(), 2, name);
    }

    @Test(dataProvider = "jackson2CompatibilityMappers")
    public void leavesDefaultFloatingPointTreeReadsSeparate(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        var number = mapper.readTree(input(mapper, "1.2300", "1.2300\n"));
        assertFalse(number.isBigDecimal(), name);
    }

    @Test(dataProvider = "jackson2CompatibilityMappers")
    public void keepsDirectBigDecimalBindingSeparateFromTreePolicy(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        BigDecimal value = mapper.readValue(input(mapper, "1.2300", "1.2300\n"), BigDecimal.class);
        assertEquals(value.scale(), 4, name);
        assertEquals(value, new BigDecimal("1.2300"), name);
    }

    @Test
    public void callerCanPreserveDecimalTreeScale() {
        ObjectMapper mapper = ObjectMapperFactory.createJson().rebuild()
                .configure(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES, false)
                .build();
        var decimal = mapper.reader()
                .with(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .readTree("1.2300");
        assertEquals(decimal.decimalValue().scale(), 4);
        assertEquals(mapper.writeValueAsString(decimal), "1.2300");
    }

    @Test(dataProvider = "jackson2CompatibilityMappers")
    public void preservesNumericDurationOutput(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS), name);

        Duration positive = Duration.ofMillis(2234);
        String positiveOutput = mapper.writeValueAsString(positive);
        assertTrue(mapper.readTree(positiveOutput).isNumber(), name + ": " + positiveOutput);
        assertEquals(mapper.readTree(positiveOutput).decimalValue().compareTo(new BigDecimal("2.234")), 0, name);
        assertEquals(mapper.readValue(positiveOutput, Duration.class), positive, name);

        Duration negative = Duration.ofMillis(-2234);
        String negativeOutput = mapper.writeValueAsString(negative);
        assertTrue(mapper.readTree(negativeOutput).isNumber(), name + ": " + negativeOutput);
        assertEquals(mapper.readTree(negativeOutput).decimalValue().compareTo(new BigDecimal("-2.234")), 0, name);
        assertEquals(mapper.readValue(negativeOutput, Duration.class), negative, name);

        Duration nanoseconds = Duration.ofSeconds(2, 123456789);
        String nanosecondsOutput = mapper.writeValueAsString(nanoseconds);
        assertTrue(mapper.readTree(nanosecondsOutput).isNumber(), name + ": " + nanosecondsOutput);
        assertEquals(mapper.readTree(nanosecondsOutput).decimalValue().compareTo(new BigDecimal("2.123456789")),
                0, name);
        assertEquals(mapper.readValue(nanosecondsOutput, Duration.class), nanoseconds, name);
    }

    @Test(dataProvider = "jackson2CompatibilityMappers")
    public void preservesNumericUtcOffsetForClassicDates(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(DateTimeFeature.WRITE_UTC_AS_OFFSET), name);
        String utcDate = mapper.readTree(mapper.writeValueAsString(new Date(0))).asString();
        assertEquals(utcDate, "1970-01-01T00:00:00.000+00:00", name);

        Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utcCalendar.setTimeInMillis(0);
        String utcCalendarOutput = mapper.readTree(mapper.writeValueAsString(utcCalendar)).asString();
        assertEquals(utcCalendarOutput, "1970-01-01T00:00:00.000+00:00", name);

        ObjectMapper offsetMapper = mapper.rebuild()
                .defaultTimeZone(TimeZone.getTimeZone("GMT+02:00"))
                .build();
        String offsetDate = offsetMapper.readTree(offsetMapper.writeValueAsString(new Date(0))).asString();
        assertTrue(offsetDate.endsWith("+02:00"), name + ": " + offsetDate);
        Calendar offsetCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+02:00"));
        offsetCalendar.setTimeInMillis(0);
        String calendarOutput = offsetMapper.readTree(offsetMapper.writeValueAsString(offsetCalendar)).asString();
        assertTrue(calendarOutput.endsWith("+02:00"), name + ": " + calendarOutput);
    }

    @Test(dataProvider = "jackson2CompatibilityMappers")
    public void preservesJackson2ZeroBasedMonthIndexes(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(DateTimeFeature.ONE_BASED_MONTHS), name);

        ObjectMapper timestampMapper = mapper.rebuild()
                .enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        String output = timestampMapper.writeValueAsString(Month.JANUARY);
        assertEquals(timestampMapper.readTree(output).intValue(), 0, name + ": " + output);
        assertEquals(mapper.readValue(input(mapper, "0", "0\n"), Month.class), Month.JANUARY, name);
    }

    @Test(dataProvider = "jackson2CompatibilityMappers")
    public void usesJackson2CompatibilityDefaultsAsFoundation(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();

        assertTrue(mapper.isEnabled(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS), name);
        assertFalse(mapper.isEnabled(MapperFeature.DETECT_PARAMETER_NAMES), name);
        assertFalse(mapper.isEnabled(MapperFeature.FIX_FIELD_NAME_UPPER_CASE_PREFIX), name);
        assertTrue(mapper.isEnabled(MapperFeature.USE_GETTERS_AS_SETTERS), name);
    }

    @Test(dataProvider = "jackson2CompatibilityMappers")
    public void acceptsJackson2StyleAccessorFirstCharacters(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        Jackson2AccessorBean source = new Jackson2AccessorBean();
        source.setvalue("lower-case");
        source.set_value("non-letter");

        String output = mapper.writeValueAsString(source);
        var tree = mapper.readTree(output);
        assertEquals(tree.size(), 2, name + ": " + output);
        assertEquals(tree.get("value").asString(), "lower-case", name + ": " + output);
        assertEquals(tree.get("_value").asString(), "non-letter", name + ": " + output);

        Jackson2AccessorBean roundTrip = mapper.readValue(output, Jackson2AccessorBean.class);
        assertEquals(roundTrip.getvalue(), "lower-case", name);
        assertEquals(roundTrip.get_value(), "non-letter", name);
    }

    @Test(dataProvider = "publicMappers")
    public void publicMappersPreserveJackson2TrailingTokenBehavior(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS), name);

        KnownProperty first = mapper.readValue(input(mapper,
                "{\"known\":\"first\"} {\"known\":\"second\"}",
                "---\nknown: first\n---\nknown: second\n"), KnownProperty.class);
        assertEquals(first.known, "first", name);
    }

    @Test(dataProvider = "publicMappers")
    public void ignoresUnknownPropertiesExplicitly(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES), name);

        KnownProperty bean = mapper.readValue(input(mapper, "{\"known\":\"kept\",\"unknown\":1}",
                "known: kept\nunknown: 1\n"), KnownProperty.class);
        assertEquals(bean.known, "kept", name);

        OpenAPI openAPI = mapper.readValue(input(mapper,
                "{\"openapi\":\"3.0.1\",\"unknown\":true}",
                "openapi: 3.0.1\nunknown: true\n"), OpenAPI.class);
        assertEquals(openAPI.getOpenapi(), "3.0.1", name);
    }

    @Test(dataProvider = "publicMappers")
    public void writesEmptyBeans(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS), name);
        assertTrue(mapper.readTree(mapper.writeValueAsString(new EmptyBean())).isObject(), name);
        assertEquals(mapper.readTree(mapper.writeValueAsString(new EmptyBean())).size(), 0, name);
    }

    @Test(dataProvider = "jackson2CompatibilityMappers")
    public void preservesJackson2EnumReadPolicy(String name, Supplier<ObjectMapper> supplier) {
        assertEnumPolicy(name, supplier.get());
    }

    @Test(dataProvider = "outputMappers")
    public void writesDatesAsText(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS), name);
        assertTrue(mapper.readTree(mapper.writeValueAsString(new Date(0))).isString(), name);
        Calendar calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(0);
        assertTrue(mapper.readTree(mapper.writeValueAsString(calendar)).isString(), name);
        assertTrue(mapper.readTree(mapper.writeValueAsString(Instant.EPOCH)).isString(), name);
        assertTrue(mapper.readTree(mapper.writeValueAsString(
                OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.ofHours(2)))).isString(), name);
    }

    @Test(dataProvider = "jackson2CompatibilityMappers")
    public void preservesDeclarationPropertyOrder(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(tools.jackson.databind.MapperFeature.SORT_PROPERTIES_ALPHABETICALLY), name);
        String output = mapper.writeValueAsString(new OrderedBean());
        assertTrue(output.indexOf("beta") < output.indexOf("alpha"), name + ": " + output);
    }

    @Test(dataProvider = "outputMappers")
    public void excludesNullPropertiesAndMapValuesButKeepsListPositions(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        NullContainer value = new NullContainer();
        String output = mapper.writeValueAsString(value);
        var tree = mapper.readTree(output);
        assertFalse(tree.has("missing"), name + ": " + output);
        assertFalse(tree.get("map").has("missing"), name + ": " + output);
        assertTrue(tree.get("list").get(0).isNull(), name + ": collection positions must not be removed");
    }

    @Test(dataProvider = "outputMappers")
    public void writesBigDecimalWithoutExponent(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN), name);
        var output = mapper.readTree(mapper.writeValueAsString(new BigDecimal("1E+2")));
        assertEquals(output.decimalValue().scale(), 0, name);
        assertEquals(output.decimalValue(), new BigDecimal("100"), name);
    }

    @Test(dataProvider = "strictMapper")
    public void strictMapperIgnoresUnknownProperties(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES), name);
        KnownProperty known = mapper.readValue("{\"known\":\"kept\",\"unknown\":1}", KnownProperty.class);
        assertEquals(known.known, "kept", name);
    }

    @Test(dataProvider = "strictMapper")
    public void strictMapperOverridesBaselineToRejectTrailingTokens(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS), name);

        MismatchedInputException trailing = expectThrows(MismatchedInputException.class,
                () -> mapper.readValue("{\"known\":\"first\"} {\"known\":\"second\"}", KnownProperty.class));
        assertTrue(trailing.getMessage().contains("FAIL_ON_TRAILING_TOKENS"),
                name + ": " + trailing.getMessage());
    }

    @Test(dataProvider = "strictMapper")
    public void strictMapperConvertsNullPrimitivesToDefaults(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES), name);

        PrimitiveBean result = mapper.readValue(
                "{\"count\":null,\"active\":null}", PrimitiveBean.class);
        assertEquals(result.count, 0, name + ": null int must become 0");
        assertFalse(result.active, name + ": null boolean must become false");
    }

    @Test(dataProvider = "strictMapper")
    public void strictMapperWritesEmptyBeans(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS), name);
        assertEquals(mapper.readTree(mapper.writeValueAsString(new EmptyBean())).size(), 0, name);
    }

    @Test(dataProvider = "strictMapper")
    public void strictMapperAppliesSwaggerDatePolicy(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS), name);
        assertTrue(mapper.readTree(mapper.writeValueAsString(new Date(0))).isString(), name);
    }

    @Test(dataProvider = "strictMapper")
    public void strictMapperExcludesNullPropertiesAndMapValues(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        var nullTree = mapper.readTree(mapper.writeValueAsString(new NullContainer()));
        assertFalse(nullTree.has("missing"), name);
        assertFalse(nullTree.get("map").has("missing"), name);
        assertTrue(nullTree.get("list").get(0).isNull(), name);
    }

    @Test(dataProvider = "strictMapper")
    public void strictMapperKeepsDefaultBigDecimalOutput(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN), name);
        assertEquals(mapper.writeValueAsString(new BigDecimal("1E+2")), "1E+2", name);
    }

    @Test
    public void preservesSuppliedJsonFactoryAndReadConstraints() {
        StreamReadConstraints constraints = StreamReadConstraints.builder().maxNestingDepth(17).build();
        JsonFactory factory = JsonFactory.builder().streamReadConstraints(constraints).build();
        for (ObjectMapper mapper : List.of(ObjectMapperFactory.createJson(factory),
                ObjectMapperFactory.createJson31(factory))) {
            assertSame(mapper.tokenStreamFactory(), factory);
            assertSame(mapper.tokenStreamFactory().streamReadConstraints(), constraints);
            assertEquals(mapper.tokenStreamFactory().streamReadConstraints().getMaxNestingDepth(), 17);
        }
    }

    @Test
    public void preservesSuppliedYamlFactoryAndReadConstraints() {
        StreamReadConstraints constraints = StreamReadConstraints.builder().maxNestingDepth(19).build();
        YAMLFactory factory = YAMLFactory.builder().streamReadConstraints(constraints).build();
        for (ObjectMapper mapper : List.of(ObjectMapperFactory.createYaml(factory),
                ObjectMapperFactory.createYaml31(factory))) {
            assertSame(mapper.tokenStreamFactory(), factory);
            assertSame(mapper.tokenStreamFactory().streamReadConstraints(), constraints);
            assertEquals(mapper.tokenStreamFactory().streamReadConstraints().getMaxNestingDepth(), 19);
        }
    }

    private static String input(ObjectMapper mapper, String json, String yaml) {
        return mapper.tokenStreamFactory() instanceof YAMLFactory ? yaml : json;
    }

    private static void assertEnumPolicy(String name, ObjectMapper mapper) {
        assertTrue(mapper.isEnabled(EnumFeature.WRITE_ENUMS_USING_TO_STRING), name);
        assertFalse(mapper.isEnabled(EnumFeature.READ_ENUMS_USING_TO_STRING), name);

        WireEnumContainer source = new WireEnumContainer();
        source.value = WireEnum.VALUE;
        String output = mapper.writeValueAsString(source);
        assertEquals(mapper.readTree(output).get("value").asString(), "wire-value", name);

        WireEnumContainer enumNameInput = mapper.readValue(input(mapper,
                "{\"value\":\"VALUE\"}", "value: VALUE\n"), WireEnumContainer.class);
        assertEquals(enumNameInput.value, WireEnum.VALUE, name);

        InvalidFormatException toStringValue = expectThrows(InvalidFormatException.class,
                () -> mapper.readValue(input(mapper,
                        "{\"value\":\"wire-value\"}", "value: wire-value\n"), WireEnumContainer.class));
        assertTrue(toStringValue.getMessage().contains("wire-value"),
                name + ": " + toStringValue.getMessage());
    }

    public static class KnownProperty {
        public String known;
    }

    public static class EmptyBean {
    }

    public enum WireEnum {
        VALUE;

        @Override
        public String toString() {
            return "wire-value";
        }
    }

    public static class WireEnumContainer {
        public WireEnum value;
    }

    public static class OrderedBean {
        public String beta = "b";
        public String alpha = "a";
    }

    public static class Jackson2AccessorBean {
        private String lowerCaseValue;
        private String nonLetterValue;

        public String getvalue() {
            return lowerCaseValue;
        }

        public void setvalue(String value) {
            lowerCaseValue = value;
        }

        public String get_value() {
            return nonLetterValue;
        }

        public void set_value(String value) {
            nonLetterValue = value;
        }
    }

    public static class NullContainer {
        public String missing;
        public Map<String, String> map = new LinkedHashMap<>();
        public List<String> list = new ArrayList<>();

        public NullContainer() {
            map.put("present", "value");
            map.put("missing", null);
            list.add(null);
            list.add("value");
        }
    }

    public static class PrimitiveBean {
        public int count = 9;
        public boolean active = true;
        public Integer boxedCount;
        public Boolean boxedActive;
    }

    @Test(dataProvider = "publicMappers")
    public void nullForPrimitivesConvertsToDefault(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES), name);

        PrimitiveBean result = mapper.readValue(
                input(mapper, "{\"count\":null,\"active\":null}", "count: null\nactive: null\n"),
                PrimitiveBean.class);
        assertEquals(result.count, 0, name + ": null int must become 0");
        assertFalse(result.active, name + ": null boolean must become false");
    }

    @Test(dataProvider = "publicMappers")
    public void nullForBoxedTypesRemainsNull(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();

        PrimitiveBean result = mapper.readValue(
                input(mapper, "{\"boxedCount\":null,\"boxedActive\":null}", "boxedCount: null\nboxedActive: null\n"),
                PrimitiveBean.class);
        assertNull(result.boxedCount, name + ": null Integer must stay null");
        assertNull(result.boxedActive, name + ": null Boolean must stay null");
    }

    @Test(dataProvider = "publicMappers")
    public void numericZeroRemainsValidForPrimitives(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();

        PrimitiveBean result = mapper.readValue(
                input(mapper, "{\"count\":0,\"active\":false}", "count: 0\nactive: false\n"),
                PrimitiveBean.class);
        assertEquals(result.count, 0, name);
        assertFalse(result.active, name);
    }

    @Test
    public void createWithNullFactoryReturnsJsonMapper() {
        assertNotNull(ObjectMapperFactory.create(null, false));
        assertNotNull(ObjectMapperFactory.create(null, true));
    }

    @Test
    public void createWithJsonFactorySucceeds() {
        assertNotNull(ObjectMapperFactory.create(new JsonFactory(), false));
        assertNotNull(ObjectMapperFactory.create(new JsonFactory(), true));
    }

    @Test
    public void createWithYamlFactorySucceeds() {
        assertNotNull(ObjectMapperFactory.create(new YAMLFactory(), false));
        assertNotNull(ObjectMapperFactory.create(new YAMLFactory(), true));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
          expectedExceptionsMessageRegExp = ".*Unsupported TokenStreamFactory.*")
    public void createWithUnsupportedFactoryThrows() {
        ObjectMapperFactory.create(new UnsupportedFactory(), false);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
          expectedExceptionsMessageRegExp = ".*Unsupported TokenStreamFactory.*")
    public void createJson31WithUnsupportedFactoryThrows() {
        ObjectMapperFactory.createJson31(new UnsupportedFactory());
    }

    static class UnsupportedFactory extends TokenStreamFactory {

        UnsupportedFactory() {
            super(StreamReadConstraints.defaults(), StreamWriteConstraints.defaults(),
                    ErrorReportConfiguration.defaults(), 0, 0);
        }

        @Override public TokenStreamFactory copy() { throw new UnsupportedOperationException(); }
        @Override public TokenStreamFactory snapshot() { return this; }
        @Override public TSFBuilder<?, ?> rebuild() { throw new UnsupportedOperationException(); }
        @Override public boolean canHandleBinaryNatively() { return false; }
        @Override public boolean canParseAsync() { return false; }
        @Override public boolean canUseSchema(FormatSchema s) { return false; }
        @Override public String getFormatName() { return "unsupported-test-format"; }
        @Override public Version version() { return Version.unknownVersion(); }

        @Override public JsonParser createParser(ObjectReadContext ctx, File f) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, Path p) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, InputStream in) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, Reader r) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, byte[] b, int off, int len) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, String s) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, char[] c, int off, int len) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonParser createParser(ObjectReadContext ctx, DataInput in) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonGenerator createGenerator(ObjectWriteContext ctx, OutputStream out, JsonEncoding enc) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonGenerator createGenerator(ObjectWriteContext ctx, Writer w) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonGenerator createGenerator(ObjectWriteContext ctx, File f, JsonEncoding enc) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override public JsonGenerator createGenerator(ObjectWriteContext ctx, Path p, JsonEncoding enc) throws JacksonException { throw new UnsupportedOperationException(); }
        @Override protected ContentReference _createContentReference(Object src) { return ContentReference.unknown(); }
        @Override protected ContentReference _createContentReference(Object src, int off, int len) { return ContentReference.unknown(); }
    }
}
