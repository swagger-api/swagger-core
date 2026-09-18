package io.swagger.v3.core.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
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
import tools.jackson.core.type.TypeReference;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonPOJOBuilder;
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
import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Supplier;

import static org.testng.Assert.*;

public class ObjectMapperFactoryTest {

    @DataProvider(name = "publicMappers")
    public Object[][] publicMappers() {
        return new Object[][] {
                mapper("Json.mapper", () -> Json.mapper().rebuild().build()),
                mapper("Json31.mapper", () -> Json31.mapper().rebuild().build()),
                mapper("Yaml.mapper", () -> Yaml.mapper().rebuild().build()),
                mapper("Yaml31.mapper", () -> Yaml31.mapper().rebuild().build()),
                mapper("createJson", ObjectMapperFactory::createJson),
                mapper("createJson31", ObjectMapperFactory::createJson31),
                mapper("createYaml", ObjectMapperFactory::createYaml),
                mapper("createYaml31", ObjectMapperFactory::createYaml31),
                mapper("createJson(factory)", () -> ObjectMapperFactory.createJson(new JsonFactory())),
                mapper("createJson31(factory)", () -> ObjectMapperFactory.createJson31(new JsonFactory())),
                mapper("createYaml(factory)", () -> ObjectMapperFactory.createYaml(new YAMLFactory())),
                mapper("createYaml31(factory)", () -> ObjectMapperFactory.createYaml31(new YAMLFactory())),
                mapper("createJsonConverter", ObjectMapperFactory::createJsonConverter),
                mapper("Json31.converterMapper", () -> Json31.converterMapper().rebuild().build()),
                mapper("create(JSON, false)", () -> ObjectMapperFactory.create(new JsonFactory(), false)),
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

    private static Object[] mapper(String name, Supplier<ObjectMapper> mapper) {
        return new Object[] {name, mapper};
    }

    @Test(dataProvider = "publicMappers")
    public void rejectsTrailingRootContentButAllowsWhitespace(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS), name);
        String twoRoots = input(mapper, "{\"known\":\"one\"} {\"known\":\"two\"}",
                "---\nknown: one\n---\nknown: two\n");
        MismatchedInputException trailing = expectThrows(MismatchedInputException.class,
                () -> mapper.readValue(twoRoots, KnownProperty.class));
        assertTrue(trailing.getMessage().contains("FAIL_ON_TRAILING_TOKENS"), name + ": " + trailing.getMessage());
        KnownProperty value = mapper.readValue(input(mapper, "{\"known\":\"one\"}  \n",
                "---\nknown: one\n  \n"), KnownProperty.class);
        assertEquals(value.known, "one", name);
        var values = mapper.readerFor(KnownProperty.class).readValues(twoRoots).readAll();
        assertEquals(values.size(), 2, name);
    }

    @Test(dataProvider = "publicMappers")
    public void rejectsNullPrimitivesButAcceptsNullBoxedValues(
            String name, Supplier<ObjectMapper> supplier) throws Exception {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES), name);
        MismatchedInputException nullInt = expectThrows(MismatchedInputException.class, () -> mapper.readValue(
                input(mapper, "{\"count\":null}", "count: null\n"), PrimitiveValues.class));
        assertTrue(nullInt.getMessage().contains("FAIL_ON_NULL_FOR_PRIMITIVES"), name + ": " + nullInt.getMessage());
        assertTrue(nullInt.getMessage().contains("int"), name + ": " + nullInt.getMessage());
        MismatchedInputException nullBoolean = expectThrows(MismatchedInputException.class, () -> mapper.readValue(
                input(mapper, "{\"enabled\":null}", "enabled: null\n"), PrimitiveValues.class));
        assertTrue(nullBoolean.getMessage().contains("FAIL_ON_NULL_FOR_PRIMITIVES"),
                name + ": " + nullBoolean.getMessage());
        assertTrue(nullBoolean.getMessage().contains("boolean"), name + ": " + nullBoolean.getMessage());
        BoxedValues boxed = mapper.readValue(input(mapper,
                "{\"count\":null,\"enabled\":null}", "count: null\nenabled: null\n"), BoxedValues.class);
        assertNull(boxed.count, name);
        assertNull(boxed.enabled, name);
        assertEquals(mapper.readValue(input(mapper, "{\"count\":0}", "count: 0\n"),
                PrimitiveValues.class).count, 0, name);
    }

    @Test(dataProvider = "publicMappers")
    public void doesNotTreatUnannotatedGettersAsSetters(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(tools.jackson.databind.MapperFeature.USE_GETTERS_AS_SETTERS), name);
        GetterOnlyList value = mapper.readValue(input(mapper, "{\"values\":[\"new\"]}",
                "values:\n  - new\n"), GetterOnlyList.class);
        assertEquals(value.getValues(), List.of("sentinel"), name);
        SetterList settable = mapper.readValue(input(mapper, "{\"values\":[\"new\"]}",
                "values:\n  - new\n"), SetterList.class);
        assertEquals(settable.getValues(), List.of("new"), name);
        AnnotatedGetterOnlyList annotated = mapper.readValue(input(mapper, "{\"values\":[\"new\"]}",
                "values:\n  - new\n"), AnnotatedGetterOnlyList.class);
        assertEquals(annotated.getValues(), List.of("sentinel", "new"), name);
    }

    @Test(dataProvider = "publicMappers")
    public void doesNotMutateImplicitFinalFields(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(tools.jackson.databind.MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS), name);
        FinalValue value = mapper.readValue(input(mapper, "{\"value\":7}", "value: 7\n"), FinalValue.class);
        assertEquals(value.value, 41, name);
        ExplicitFinalValue explicit = mapper.readValue(input(mapper, "{\"value\":7}", "value: 7\n"),
                ExplicitFinalValue.class);
        assertEquals(explicit.value, 7, name);
        CreatorFinalValue created = mapper.readValue(input(mapper, "{\"value\":7}", "value: 7\n"),
                CreatorFinalValue.class);
        assertEquals(created.value, 7, name);
        FinalRecord record = mapper.readValue(input(mapper, "{\"value\":7}", "value: 7\n"), FinalRecord.class);
        assertEquals(record.value(), 7, name);
    }

    @Test(dataProvider = "publicMappers")
    public void activeViewsExcludeUnannotatedProperties(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(tools.jackson.databind.MapperFeature.DEFAULT_VIEW_INCLUSION), name);
        ViewBean source = new ViewBean();
        String output = mapper.writerWithView(PublicView.class).writeValueAsString(source);
        var tree = mapper.readTree(output);
        assertTrue(tree.has("publicValue"), name);
        assertFalse(tree.has("secret"), name);
        assertTrue(mapper.readTree(mapper.writeValueAsString(source)).has("secret"), name);
        ViewBean read = mapper.readerWithView(PublicView.class).forType(ViewBean.class).readValue(
                input(mapper, "{\"publicValue\":\"changed\",\"secret\":\"changed\"}",
                        "publicValue: changed\nsecret: changed\n"));
        assertEquals(read.publicValue, "changed", name);
        assertEquals(read.secret, "secret", name);

        String inheritedOutput = mapper.writerWithView(ProtectedView.class).writeValueAsString(source);
        var inheritedTree = mapper.readTree(inheritedOutput);
        assertTrue(inheritedTree.has("publicValue"), name + ": a child view must include its parent view");
        assertTrue(inheritedTree.has("protectedValue"), name);
        assertFalse(inheritedTree.has("secret"), name);
    }

    @Test
    public void schemaViewInclusionRemainsSeparateFromMapperDefaultViewInclusion() {
        ObjectMapper mapper = ObjectMapperFactory.createJson();
        assertFalse(mapper.isEnabled(tools.jackson.databind.MapperFeature.DEFAULT_VIEW_INCLUSION));
        ModelResolver resolver = new ModelResolver(mapper);
        ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        JsonView publicView = jsonView(PublicView.class);

        Schema<?> includingUnannotated = context.resolve(new AnnotatedType(ViewBean.class)
                .jsonViewAnnotation(publicView)
                .includePropertiesWithoutJSONView(true));
        Schema<?> excludingUnannotated = context.resolve(new AnnotatedType(ViewBean.class)
                .jsonViewAnnotation(publicView)
                .includePropertiesWithoutJSONView(false));

        assertTrue(includingUnannotated.getProperties().containsKey("secret"));
        assertFalse(excludingUnannotated.getProperties().containsKey("secret"));
        assertTrue(excludingUnannotated.getProperties().containsKey("publicValue"));
    }

    @Test(dataProvider = "publicMappers")
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

    @Test(dataProvider = "publicMappers")
    public void preservesJackson2MonthHandling(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(DateTimeFeature.ONE_BASED_MONTHS), name);
        assertEquals(mapper.readTree(mapper.writeValueAsString(Month.JANUARY)).asString(), "JANUARY", name);
        assertEquals(mapper.readValue("0", Month.class), Month.JANUARY, name);
        assertEquals(mapper.readValue("1", Month.class), Month.FEBRUARY, name);
        assertEquals(mapper.readValue("11", Month.class), Month.DECEMBER, name);
        assertEquals(mapper.readValue(input(mapper, "\"JANUARY\"", "JANUARY\n"), Month.class), Month.JANUARY, name);
        InvalidFormatException twelve = expectThrows(InvalidFormatException.class,
                () -> mapper.readValue("12", Month.class));
        assertTrue(twelve.getMessage().contains("outside 0-11 range"), name + ": " + twelve.getMessage());
        InvalidFormatException negative = expectThrows(InvalidFormatException.class,
                () -> mapper.readValue("-1", Month.class));
        assertTrue(negative.getMessage().contains("outside 0-11 range"), name + ": " + negative.getMessage());

        MonthAsText explicit = new MonthAsText();
        explicit.month = Month.JANUARY;
        String explicitOutput = mapper.writeValueAsString(explicit);
        assertEquals(mapper.readTree(explicitOutput).get("month").asString(), "January", name);
        assertEquals(mapper.readValue(explicitOutput, MonthAsText.class).month, Month.JANUARY, name);
    }

    @Test(dataProvider = "publicMappers")
    public void detectsVoidPropertiesOnlyWhenNullsAreIncluded(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(tools.jackson.databind.MapperFeature.ALLOW_VOID_VALUED_PROPERTIES), name);
        assertTrue(mapper.readTree(mapper.writeValueAsString(new IncludedVoid())).has("nothing"), name);
        assertFalse(mapper.readTree(mapper.writeValueAsString(new ExcludedVoid())).has("nothing"), name);
    }

    @Test(dataProvider = "publicMappers")
    public void usesJackson3AccessorNames(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(tools.jackson.databind.MapperFeature.FIX_FIELD_NAME_UPPER_CASE_PREFIX), name);
        AccessorNames value = new AccessorNames();
        String output = mapper.writeValueAsString(value);
        var tree = mapper.readTree(output);
        assertTrue(tree.has("URL"), name + ": " + output);
        assertTrue(tree.has("eDeskStatus"), name + ": " + output);
        assertTrue(tree.has("$ref"), name + ": Swagger model-style get$ref() must remain visible: " + output);
        assertTrue(tree.has("explicitName"), name + ": @JsonProperty must win over implicit naming: " + output);
        assertFalse(tree.has("implicitName"), name + ": implicit name must not survive @JsonProperty: " + output);
        assertFalse(tree.has("url"), name + ": Jackson 2 acronym mangling must not be restored: " + output);
        assertEquals(mapper.readValue(input(mapper, "{\"URL\":\"changed\"}", "URL: changed\n"),
                AccessorNames.class).getURL(), "changed", name);
        assertEquals(mapper.readValue(input(mapper, "{\"$ref\":\"#/changed\"}", "$ref: '#/changed'\n"),
                AccessorNames.class).get$ref(), "#/changed", name);
        assertEquals(mapper.readValue(input(mapper, "{\"iPhone\":\"changed\"}", "iPhone: changed\n"),
                AccessorNames.class).getiPhone(), "changed", name);
        assertTrue(tree.has("enabled"), name + ": is-getters must use the normal property name: " + output);

        BuilderValue built = mapper.readValue(input(mapper, "{\"value\":\"built\"}", "value: built\n"),
                BuilderValue.class);
        assertEquals(built.getValue(), "built", name);

        AccessorRecord record = new AccessorRecord("record");
        var recordTree = mapper.readTree(mapper.writeValueAsString(record));
        assertEquals(recordTree.get("recordValue").asString(), "record", name);
        assertEquals(mapper.readValue(mapper.writeValueAsString(record), AccessorRecord.class), record, name);
    }

    @Test(dataProvider = "publicMappers")
    public void schemaAndSerializationUseTheSameAccessorNames(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        ModelConverterContextImpl context = new ModelConverterContextImpl(new ModelResolver(mapper));
        Schema<?> schema = context.resolve(new AnnotatedType(AccessorParityBean.class));
        Set<String> serializedNames = new java.util.LinkedHashSet<>();
        serializedNames.addAll(mapper.readTree(mapper.writeValueAsString(new AccessorParityBean())).propertyNames());
        assertEquals(schema.getProperties().keySet(), serializedNames,
                name + ": schema=" + schema.getProperties().keySet() + ", serialization=" + serializedNames);

        Schema<?> recordSchema = context.resolve(new AnnotatedType(AccessorRecord.class));
        assertEquals(recordSchema.getProperties().keySet(), Set.of("recordValue"), name);
    }

    @Test(dataProvider = "publicMappers")
    public void usesZForUtcDates(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(DateTimeFeature.WRITE_UTC_AS_OFFSET), name);
        assertTrue(mapper.readTree(mapper.writeValueAsString(new Date(0))).asString().endsWith("Z"), name);

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

    @Test(dataProvider = "publicMappers")
    public void preservesDecimalTreeScale(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get().rebuild()
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .build();
        assertFalse(mapper.isEnabled(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES), name);
        var node = mapper.readTree("1.2300");
        assertEquals(node.decimalValue().scale(), 4, name);
        String output = mapper.writeValueAsString(node);
        assertTrue(output.matches("(?s)\\s*(?:---\\s*)?1\\.2300\\s*"), name + ": " + output);
        assertEquals(mapper.readTree(output).decimalValue().scale(), 4, name);
        assertEquals(mapper.readValue("1.2300", BigDecimal.class).scale(), 4, name);
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

    @Test(dataProvider = "publicMappers")
    public void preservesAsymmetricEnumPolicy(String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(EnumFeature.WRITE_ENUMS_USING_TO_STRING), name);
        assertFalse(mapper.isEnabled(EnumFeature.READ_ENUMS_USING_TO_STRING), name);
        assertEquals(mapper.readTree(mapper.writeValueAsString(WireEnum.VALUE)).asString(), "wire-value", name);
        assertEquals(mapper.readValue(input(mapper, "\"VALUE\"", "VALUE\n"), WireEnum.class), WireEnum.VALUE, name);
        InvalidFormatException enumValue = expectThrows(InvalidFormatException.class,
                () -> mapper.readValue(input(mapper, "\"wire-value\"", "wire-value\n"), WireEnum.class));
        assertTrue(enumValue.getMessage().contains("wire-value"), name + ": " + enumValue.getMessage());
        assertTrue(enumValue.getMessage().contains("WireEnum"), name + ": " + enumValue.getMessage());

        WireEnumContainer property = new WireEnumContainer();
        property.value = WireEnum.VALUE;
        assertEquals(mapper.readTree(mapper.writeValueAsString(property)).get("value").asString(),
                "wire-value", name);
        assertEquals(mapper.readValue(input(mapper, "{\"value\":\"VALUE\"}", "value: VALUE\n"),
                WireEnumContainer.class).value, WireEnum.VALUE, name);
        InvalidFormatException enumProperty = expectThrows(InvalidFormatException.class,
                () -> mapper.readValue(input(mapper,
                                "{\"value\":\"wire-value\"}", "value: wire-value\n"),
                        WireEnumContainer.class));
        assertTrue(enumProperty.getMessage().contains("wire-value"), name + ": " + enumProperty.getMessage());
        assertTrue(enumProperty.getMessage().contains("WireEnum"), name + ": " + enumProperty.getMessage());

        Map<WireEnum, String> enumKeys = new LinkedHashMap<>();
        enumKeys.put(WireEnum.VALUE, "value");
        assertTrue(mapper.readTree(mapper.writeValueAsString(enumKeys)).has("wire-value"), name);
        Map<WireEnum, String> readKeys = mapper.readValue(input(mapper,
                        "{\"VALUE\":\"value\"}", "VALUE: value\n"),
                new TypeReference<Map<WireEnum, String>>() { });
        assertEquals(readKeys.get(WireEnum.VALUE), "value", name);
        InvalidFormatException enumKey = expectThrows(InvalidFormatException.class,
                () -> mapper.readValue(input(mapper,
                                "{\"wire-value\":\"value\"}", "wire-value: value\n"),
                        new TypeReference<Map<WireEnum, String>>() { }));
        assertTrue(enumKey.getMessage().contains("wire-value"), name + ": " + enumKey.getMessage());
        assertTrue(enumKey.getMessage().contains("WireEnum"), name + ": " + enumKey.getMessage());
    }

    @Test
    public void parameterDeserializerKeepsItsToStringEnumException() {
        io.swagger.v3.oas.models.parameters.Parameter parameter = Json.mapper().readValue(
                "{\"in\":\"query\",\"style\":\"deepObject\"}",
                io.swagger.v3.oas.models.parameters.Parameter.class);
        assertEquals(parameter.getStyle(),
                io.swagger.v3.oas.models.parameters.Parameter.StyleEnum.DEEPOBJECT);
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

    @Test(dataProvider = "outputMappers")
    public void writesOffsetDateTimeWithItsOffsetAndReadsAtTheMapperTimeZone(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        OffsetDateTime value = OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.ofHours(2));

        String output = mapper.writeValueAsString(value);

        assertEquals(mapper.readTree(output).asString(), "1970-01-01T02:00:00+02:00", name);
        assertEquals(mapper.readValue(output, OffsetDateTime.class),
                OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC), name);
    }

    @Test(dataProvider = "outputMappers")
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
    public void strictMapperRejectsTrailingContent(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS), name);
        MismatchedInputException trailing = expectThrows(MismatchedInputException.class,
                () -> mapper.readValue("{\"known\":\"one\"} {\"known\":\"two\"}", KnownProperty.class));
        assertTrue(trailing.getMessage().contains("FAIL_ON_TRAILING_TOKENS"), name + ": " + trailing.getMessage());
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
    public void strictMapperWritesEmptyBeans(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS), name);
        assertEquals(mapper.readTree(mapper.writeValueAsString(new EmptyBean())).size(), 0, name);
    }

    @Test(dataProvider = "strictMapper")
    public void strictMapperWritesDatesAsText(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertFalse(mapper.isEnabled(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS), name);
        assertTrue(mapper.readTree(mapper.writeValueAsString(new Date(0))).isString(), name);
    }

    @Test(dataProvider = "strictMapper")
    public void strictMapperPreservesAsymmetricEnumPolicy(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(EnumFeature.WRITE_ENUMS_USING_TO_STRING), name);
        assertFalse(mapper.isEnabled(EnumFeature.READ_ENUMS_USING_TO_STRING), name);
        assertEquals(mapper.readTree(mapper.writeValueAsString(WireEnum.VALUE)).asString(), "wire-value", name);
        assertEquals(mapper.readValue("\"VALUE\"", WireEnum.class), WireEnum.VALUE, name);
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

    @Test(dataProvider = "strictMapper")
    public void strictMapperKeepsJackson3PropertyOrder(
            String name, Supplier<ObjectMapper> supplier) {
        ObjectMapper mapper = supplier.get();
        assertTrue(mapper.isEnabled(tools.jackson.databind.MapperFeature.SORT_PROPERTIES_ALPHABETICALLY), name);
    }

    @Test
    public void preservesSuppliedJsonFactoryAndReadConstraints() {
        StreamReadConstraints constraints = StreamReadConstraints.builder().maxNestingDepth(17).build();
        JsonFactory factory = JsonFactory.builder().streamReadConstraints(constraints).build();
        ObjectMapper mapper = ObjectMapperFactory.createJson(factory);

        assertSame(mapper.tokenStreamFactory(), factory);
        assertSame(mapper.tokenStreamFactory().streamReadConstraints(), constraints);
        assertEquals(mapper.tokenStreamFactory().streamReadConstraints().getMaxNestingDepth(), 17);
    }

    @Test
    public void preservesSuppliedYamlFactoryAndReadConstraints() {
        StreamReadConstraints constraints = StreamReadConstraints.builder().maxNestingDepth(19).build();
        YAMLFactory factory = YAMLFactory.builder().streamReadConstraints(constraints).build();
        ObjectMapper mapper = ObjectMapperFactory.createYaml(factory);

        assertSame(mapper.tokenStreamFactory(), factory);
        assertSame(mapper.tokenStreamFactory().streamReadConstraints(), constraints);
        assertEquals(mapper.tokenStreamFactory().streamReadConstraints().getMaxNestingDepth(), 19);
    }

    private static String input(ObjectMapper mapper, String json, String yaml) {
        return mapper.tokenStreamFactory() instanceof YAMLFactory ? yaml : json;
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

    public static class PrimitiveValues {
        public int count = 9;
        public boolean enabled = true;
    }

    public static class BoxedValues {
        public Integer count = 9;
        public Boolean enabled = true;
    }

    public static class GetterOnlyList {
        private final List<String> values = new ArrayList<>(List.of("sentinel"));

        public List<String> getValues() {
            return values;
        }
    }

    public static class SetterList {
        private List<String> values = new ArrayList<>(List.of("sentinel"));

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }
    }

    public static class AnnotatedGetterOnlyList {
        private final List<String> values = new ArrayList<>(List.of("sentinel"));

        public List<String> getValues() {
            return values;
        }

        @JsonProperty("values")
        public void acceptValues(List<String> values) {
            this.values.addAll(values);
        }
    }

    public static class FinalValue {
        public final int value = 41;
    }

    public static class ExplicitFinalValue {
        @JsonProperty("value")
        public final int value;

        public ExplicitFinalValue() {
            value = 41;
        }
    }

    public static class CreatorFinalValue {
        public final int value;

        @JsonCreator
        public CreatorFinalValue(@JsonProperty("value") int value) {
            this.value = value;
        }
    }

    public record FinalRecord(int value) {
    }

    public static class PublicView {
    }

    public static class ProtectedView extends PublicView {
    }

    public static class ViewBean {
        @JsonView(PublicView.class)
        public String publicValue = "public";
        @JsonView(ProtectedView.class)
        public String protectedValue = "protected";
        public String secret = "secret";
    }

    public static class MonthAsText {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMMM", locale = "en")
        public Month month;
    }

    public static class IncludedVoid {
        @JsonInclude(JsonInclude.Include.ALWAYS)
        public Void getNothing() {
            return null;
        }
    }

    public static class ExcludedVoid {
        public Void getNothing() {
            return null;
        }
    }

    public static class AccessorNames {
        private String URL = "url";
        private String iPhone = "phone";
        public String eDeskStatus = "ready";
        private String reference = "#/components/schemas/Example";
        private boolean enabled = true;

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public String getiPhone() {
            return iPhone;
        }

        public void setiPhone(String iPhone) {
            this.iPhone = iPhone;
        }

        public String getEDeskStatus() {
            return eDeskStatus;
        }

        public String get$ref() {
            return reference;
        }

        public void set$ref(String reference) {
            this.reference = reference;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @JsonProperty("explicitName")
        public String getImplicitName() {
            return "explicit";
        }
    }

    @JsonDeserialize(builder = BuilderValue.Builder.class)
    public static class BuilderValue {
        private final String value;

        private BuilderValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @JsonPOJOBuilder(withPrefix = "with")
        public static class Builder {
            private String value;

            public Builder withValue(String value) {
                this.value = value;
                return this;
            }

            public BuilderValue build() {
                return new BuilderValue(value);
            }
        }
    }

    public record AccessorRecord(String recordValue) {
    }

    public static class AccessorParityBean {
        private String URL = "url";
        private String iPhone = "phone";
        public String eDeskStatus = "ready";
        private boolean enabled = true;

        public String getURL() { return URL; }
        public void setURL(String URL) { this.URL = URL; }
        @JsonProperty("iPhone")
        public String getiPhone() { return iPhone; }
        public void setiPhone(String iPhone) { this.iPhone = iPhone; }
        public String getEDeskStatus() { return eDeskStatus; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        @JsonProperty("explicitName")
        public String getImplicitName() { return "explicit"; }
    }

    private static JsonView jsonView(Class<?> view) {
        return new JsonView() {
            @Override
            public Class<?>[] value() {
                return new Class<?>[] {view};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonView.class;
            }
        };
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
