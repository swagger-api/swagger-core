package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.Schema.SchemaResolution;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;

import static io.swagger.v3.oas.models.media.Schema.BIND_TYPE_AND_TYPES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Regression tests for https://github.com/swagger-api/swagger-core/issues/5233
 *
 * Root symptom: @Schema(type="number"|"integer"|"boolean") in OAS 3.1 mode produced
 * getType()="number" but getTypes()=["string"] — correct scalar, wrong set.
 * The OAS 3.1 serializer reads the types set, so every explicitly typed property
 * was emitted as "string".
 */
public class Ticket5233Test extends SwaggerTestBase {

    private String previousBindType;

    @BeforeMethod
    public void clearBindType() {
        previousBindType = System.getProperty(BIND_TYPE_AND_TYPES);
        System.clearProperty(BIND_TYPE_AND_TYPES);
    }

    @AfterMethod(alwaysRun = true)
    public void restoreBindType() {
        if (previousBindType == null) {
            System.clearProperty(BIND_TYPE_AND_TYPES);
        } else {
            System.setProperty(BIND_TYPE_AND_TYPES, previousBindType);
        }
    }

    // --- OAS 3.1 via ModelConverters (exact repro from issue) ---

    @Test(description = "OAS 3.1: explicit scalar types resolve correctly in types set")
    public void testExplicitTypesInOas31ViaModelConverters() {
        ResolvedSchema resolved = new ModelConverters(true)
                .resolveAsResolvedSchema(new AnnotatedType(Dto.class));

        io.swagger.v3.oas.models.media.Schema schema = resolved.schema;

        // control: inferred type must be unaffected
        assertEquals(typesOf(schema, "inferred"), Collections.singleton("number"));
        assertNull(schemaOf(schema, "inferred").getType(), "inferred scalar type must be null in OAS 3.1");

        // core bug cases
        assertEquals(typesOf(schema, "amount"), Collections.singleton("number"),
                "amount: getTypes() was [\"string\"] before fix");
        assertEquals(typesOf(schema, "count"), Collections.singleton("integer"),
                "count: getTypes() was [\"string\"] before fix");
        assertEquals(typesOf(schema, "flag"), Collections.singleton("boolean"),
                "flag: getTypes() was [\"string\"] before fix");

        // control: enum
        assertEquals(typesOf(schema, "unit"), Collections.singleton("string"));
    }

    @Test(description = "OAS 3.1: explicit scalar types resolve correctly via ModelResolver")
    public void testExplicitTypesInOas31ViaModelResolver() {
        ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(true);
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(Dto.class));

        Map<String, io.swagger.v3.oas.models.media.Schema> properties = schema.getProperties();
        assertEquals(properties.get("amount").getTypes(), Collections.singleton("number"));
        assertEquals(properties.get("count").getTypes(), Collections.singleton("integer"));
        assertEquals(properties.get("flag").getTypes(), Collections.singleton("boolean"));

        // controls: inferred type and enum are unaffected
        assertEquals(properties.get("inferred").getTypes(), Collections.singleton("number"));
        assertEquals(properties.get("unit").getTypes(), Collections.singleton("string"));
        assertEquals(properties.get("unit").getEnum(), Arrays.asList("DAY", "WEEK", "MONTH"));
    }

    @Test(description = "OAS 3.1: serialized JSON contains correct types")
    public void testOas31SerializedOutput() {
        ResolvedSchema resolved = new ModelConverters(true)
                .resolveAsResolvedSchema(new AnnotatedType(Dto.class));

        JsonNode root = Json31.mapper().valueToTree(resolved.schema);
        JsonNode props = root.path("properties");

        assertEquals(props.path("inferred").path("type").asText(), "number");
        assertEquals(props.path("amount").path("type").asText(), "number");
        assertEquals(props.path("count").path("type").asText(), "integer");
        assertEquals(props.path("flag").path("type").asText(), "boolean");
        assertEquals(props.path("unit").path("type").asText(), "string");

        System.out.println(Json31.pretty(resolved.schema));
    }

    @Test(description = "OAS 3.1: nullable scalar types serialize as arrays, including when type is inferred")
    public void testNullableTypesInOas31() {
        ResolvedSchema resolved = new ModelConverters(true)
                .resolveAsResolvedSchema(new AnnotatedType(Dto.class));

        io.swagger.v3.oas.models.media.Schema schema = resolved.schema;
        java.util.Set<String> expectedTypes = new LinkedHashSet<>(Arrays.asList("number", "null"));

        assertEquals(typesOf(schema, "nullableAmount"), expectedTypes);
        assertEquals(typesOf(schema, "nullableInferred"), expectedTypes);
        assertNull(schemaOf(schema, "nullableAmount").getNullable());
        assertNull(schemaOf(schema, "nullableInferred").getNullable());

        JsonNode properties = Json31.mapper().valueToTree(schema).path("properties");
        assertSerializedTypes(properties.path("nullableAmount").path("type"), "number", "null");
        assertSerializedTypes(properties.path("nullableInferred").path("type"), "number", "null");
    }

    @Test(description = "OAS 3.1: supported type sources and precedence control serialized output")
    public void testSupportedTypeSourcesAndPrecedenceInOas31() {
        ResolvedSchema resolved = new ModelConverters(true)
                .resolveAsResolvedSchema(new AnnotatedType(Dto.class));
        io.swagger.v3.oas.models.media.Schema schema = resolved.schema;

        assertEquals(typesOf(schema, "stringOverride"), Collections.singleton("string"));
        assertEquals(typesOf(schema, "int32Override"), Collections.singleton("integer"));
        assertEquals(schemaOf(schema, "int32Override").getFormat(), "int32");
        assertEquals(typesOf(schema, "floatOverride"), Collections.singleton("number"));
        assertEquals(schemaOf(schema, "floatOverride").getFormat(), "float");
        assertEquals(typesOf(schema, "nullableBooleanOverride"),
                new LinkedHashSet<>(Arrays.asList("boolean", "null")));
        assertEquals(typesOf(schema, "objectOverride"), Collections.singleton("object"));
        assertEquals(typesOf(schema, "arrayOverride"), Collections.singleton("array"));
        assertEquals(typesOf(schema, "nullOverride"), Collections.singleton("null"));

        assertEquals(typesOf(schema, "typesOverride"), Collections.singleton("boolean"));
        assertEquals(typesOf(schema, "nullableTypesOverride"),
                new LinkedHashSet<>(Arrays.asList("integer", "null")));

        io.swagger.v3.oas.models.media.Schema conflictingTypeAndTypes =
                schemaOf(schema, "conflictingTypeAndTypes");
        assertNull(conflictingTypeAndTypes.getType());
        assertEquals(conflictingTypeAndTypes.getTypes(), Collections.singleton("boolean"),
                "OAS 3.1 serialization uses types(), without populating the legacy type()");

        assertEquals(typesOf(schema, "implementationOverride"), Collections.singleton("integer"));
        assertEquals(schemaOf(schema, "implementationOverride").getFormat(), "int64");

        io.swagger.v3.oas.models.media.Schema conflictingImplementationAndType =
                schemaOf(schema, "conflictingImplementationAndType");
        assertNull(conflictingImplementationAndType.getType());
        assertEquals(conflictingImplementationAndType.getTypes(), Collections.singleton("integer"),
                "implementation() controls resolution when it conflicts with type()");
        assertEquals(typesOf(schema, "nullableConflictingImplementationAndType"),
                new LinkedHashSet<>(Arrays.asList("integer", "null")));

        JsonNode properties = Json31.mapper().valueToTree(schema).path("properties");
        assertEquals(properties.path("stringOverride").path("type").asText(), "string");
        assertEquals(properties.path("int32Override").path("type").asText(), "integer");
        assertEquals(properties.path("int32Override").path("format").asText(), "int32");
        assertEquals(properties.path("floatOverride").path("type").asText(), "number");
        assertEquals(properties.path("floatOverride").path("format").asText(), "float");
        assertSerializedTypes(properties.path("nullableBooleanOverride").path("type"), "boolean", "null");
        assertEquals(properties.path("objectOverride").path("type").asText(), "object");
        assertEquals(properties.path("arrayOverride").path("type").asText(), "array");
        assertEquals(properties.path("nullOverride").path("type").asText(), "null");
        assertEquals(properties.path("typesOverride").path("type").asText(), "boolean");
        assertSerializedTypes(properties.path("nullableTypesOverride").path("type"), "integer", "null");
        assertEquals(properties.path("conflictingTypeAndTypes").path("type").asText(), "boolean");
        assertEquals(properties.path("implementationOverride").path("type").asText(), "integer");
        assertEquals(properties.path("conflictingImplementationAndType").path("type").asText(), "integer");
        assertSerializedTypes(properties.path("nullableConflictingImplementationAndType").path("type"), "integer", "null");
    }

    @Test(description = "OAS 3.1: type is copied to types without an inferred property schema")
    public void testTypeIsCopiedToTypesForAnnotationOnlyResolution() throws NoSuchFieldException {
        assertAnnotationOnlyType("objectType", "object");
        assertAnnotationOnlyType("arrayType", "array");
        assertAnnotationOnlyType("nullType", "null");
    }

    @Test(description = "OAS 3.1: a blank annotation type does not populate types")
    public void testBlankTypeForAnnotationOnlyResolution() throws NoSuchFieldException {
        io.swagger.v3.oas.models.media.Schema schema = annotationOnlySchema("blankType", true);

        assertNull(schema.getType());
        assertNull(schema.getTypes());
        assertEquals(Json31.mapper().valueToTree(schema).has("type"), false);
    }

    @Test(description = "OAS 3.1: nullable is added to blank and explicit annotation types")
    public void testNullableTypesForAnnotationOnlyResolution() throws NoSuchFieldException {
        io.swagger.v3.oas.models.media.Schema nullableBlank = annotationOnlySchema("nullableBlankType", true);
        assertNull(nullableBlank.getType());
        assertEquals(nullableBlank.getTypes(), Collections.singleton("null"));
        assertEquals(Json31.mapper().valueToTree(nullableBlank).path("type").asText(), "null");

        io.swagger.v3.oas.models.media.Schema nullableNumber = annotationOnlySchema("nullableNumberType", true);
        assertNull(nullableNumber.getType());
        assertEquals(nullableNumber.getTypes(), new LinkedHashSet<>(Arrays.asList("number", "null")));
        assertSerializedTypes(Json31.mapper().valueToTree(nullableNumber).path("type"), "number", "null");
    }

    @Test(description = "OAS 3.1: explicit types take precedence over type before nullable is added")
    public void testExplicitTypesPrecedenceForAnnotationOnlyResolution() throws NoSuchFieldException {
        io.swagger.v3.oas.models.media.Schema schema = annotationOnlySchema("nullableTypesOverride", true);

        assertNull(schema.getType());
        assertEquals(schema.getTypes(), new LinkedHashSet<>(Arrays.asList("boolean", "null")));
        assertSerializedTypes(Json31.mapper().valueToTree(schema).path("type"), "boolean", "null");
    }

    @Test(description = "OAS 3.0: type is not copied to the OAS 3.1 types set")
    public void testTypeIsNotCopiedToTypesForOas30() throws NoSuchFieldException {
        io.swagger.v3.oas.models.media.Schema schema = annotationOnlySchema("objectType", false);

        assertEquals(schema.getType(), "object");
        assertNull(schema.getTypes());
    }

    @Test(description = "bind-type only exposes a singleton types value through the legacy getType accessor")
    public void testBindTypeCompatibilityGetter() {
        io.swagger.v3.oas.models.media.Schema singleton = new io.swagger.v3.oas.models.media.Schema()
                .types(Collections.singleton("string"));
        io.swagger.v3.oas.models.media.Schema multiple = new io.swagger.v3.oas.models.media.Schema()
                .types(new LinkedHashSet<>(Arrays.asList("string", "null")));
        io.swagger.v3.oas.models.media.Schema conflicting = new io.swagger.v3.oas.models.media.Schema()
                .type("integer")
                .types(Collections.singleton("string"));
        io.swagger.v3.oas.models.media.Schema empty = new io.swagger.v3.oas.models.media.Schema();

        assertNull(singleton.getType());

        System.setProperty(BIND_TYPE_AND_TYPES, "true");

        assertEquals(singleton.getType(), "string");
        assertEquals(singleton.getTypes(), Collections.singleton("string"));
        assertNull(multiple.getType(), "multiple OAS 3.1 types cannot be represented by the legacy getter");
        assertEquals(conflicting.getType(), "integer", "the explicit legacy type must take precedence");
        assertEquals(conflicting.getTypes(), Collections.singleton("string"), "bind-type must not synchronize fields");
        assertNull(empty.getType());

        System.setProperty(BIND_TYPE_AND_TYPES, "false");
        assertNull(singleton.getType(), "getType must read bind-type on each invocation");
    }

    @Test(description = "bind-type does not change OAS 3.1 types or serialization")
    public void testBindTypeDoesNotAffectOas31Serialization() {
        ResolvedSchema resolved = new ModelConverters(true)
                .resolveAsResolvedSchema(new AnnotatedType(Dto.class));
        io.swagger.v3.oas.models.media.Schema schema = resolved.schema;
        io.swagger.v3.oas.models.media.Schema inferred = schemaOf(schema, "inferred");
        io.swagger.v3.oas.models.media.Schema nullable = schemaOf(schema, "nullableInferred");
        JsonNode withoutBinding = Json31.mapper().valueToTree(schema);

        assertNull(inferred.getType());
        assertNull(nullable.getType());

        System.setProperty(BIND_TYPE_AND_TYPES, "true");

        assertEquals(inferred.getType(), "number");
        assertNull(nullable.getType(), "nullable schema has both number and null types");
        assertEquals(typesOf(schema, "inferred"), Collections.singleton("number"));
        assertEquals(typesOf(schema, "nullableInferred"),
                new LinkedHashSet<>(Arrays.asList("number", "null")));
        assertEquals(Json31.mapper().valueToTree(schema), withoutBinding,
                "bind-type must not affect OAS 3.1 serialization");
    }

    @Test(description = "OAS 3.0 default mode: still correct (was working before regression)")
    public void testExplicitTypesInOas30DefaultMode() {
        ResolvedSchema resolved = new ModelConverters(false)
                .resolveAsResolvedSchema(new AnnotatedType(Dto.class));

        io.swagger.v3.oas.models.media.Schema schema = resolved.schema;

        assertEquals(schemaOf(schema, "amount").getType(), "number");
        assertEquals(schemaOf(schema, "count").getType(), "integer");
        assertEquals(schemaOf(schema, "flag").getType(), "boolean");
        assertEquals(schemaOf(schema, "inferred").getType(), "number");
    }

    @Test(description = "OAS 3.0 ALL_OF sibling mode: explicit non-string types resolve correctly")
    public void testExplicitTypesInOas30AllOfMode() {
        ResolvedSchema resolved = new ModelConverters(false, SchemaResolution.ALL_OF)
                .resolveAsResolvedSchema(new AnnotatedType(Dto.class));

        io.swagger.v3.oas.models.media.Schema schema = resolved.schema;

        assertEquals(schemaOf(schema, "amount").getType(), "number");
        assertEquals(schemaOf(schema, "count").getType(), "integer");
        assertEquals(schemaOf(schema, "flag").getType(), "boolean");
    }

    @Test(description = "OAS 3.0 ALL_OF_REF sibling mode: explicit non-string types resolve correctly")
    public void testExplicitTypesInOas30AllOfRefMode() {
        ResolvedSchema resolved = new ModelConverters(false, SchemaResolution.ALL_OF_REF)
                .resolveAsResolvedSchema(new AnnotatedType(Dto.class));

        io.swagger.v3.oas.models.media.Schema schema = resolved.schema;

        assertEquals(schemaOf(schema, "amount").getType(), "number");
        assertEquals(schemaOf(schema, "count").getType(), "integer");
        assertEquals(schemaOf(schema, "flag").getType(), "boolean");
    }

    // --- DTO from the issue (field types match the declared @Schema type) ---

    static class Dto {
        @Schema(title = "Inferred")
        public BigDecimal inferred;

        @Schema(title = "Amount", type = "number")
        public BigDecimal amount;

        @Schema(title = "Nullable amount", type = "number", nullable = true)
        public BigDecimal nullableAmount;

        @Schema(title = "Nullable inferred", nullable = true)
        public BigDecimal nullableInferred;

        @Schema(title = "Count", type = "integer")
        public Integer count;

        @Schema(title = "Flag", type = "boolean")
        public Boolean flag;

        @Schema(title = "String override", type = "string")
        public Integer stringOverride;

        @Schema(title = "Int32 override", type = "integer", format = "int32")
        public String int32Override;

        @Schema(title = "Float override", type = "number", format = "float")
        public String floatOverride;

        @Schema(title = "Nullable boolean override", type = "boolean", nullable = true)
        public String nullableBooleanOverride;

        @Schema(title = "Object override", type = "object")
        public String objectOverride;

        @Schema(title = "Array override", type = "array")
        public String arrayOverride;

        @Schema(title = "Null override", type = "null")
        public String nullOverride;

        @Schema(title = "Types override", types = {"boolean"})
        public String typesOverride;

        @Schema(title = "Nullable types override", types = {"integer"}, nullable = true)
        public String nullableTypesOverride;

        @Schema(title = "Conflicting type and types", type = "number", types = {"boolean"})
        public String conflictingTypeAndTypes;

        @Schema(title = "Implementation override", implementation = Long.class)
        public String implementationOverride;

        @Schema(title = "Conflicting implementation and type", implementation = Long.class, type = "boolean")
        public String conflictingImplementationAndType;

        @Schema(title = "Nullable conflicting implementation and type", implementation = Long.class,
                type = "boolean", nullable = true)
        public String nullableConflictingImplementationAndType;

        @Schema(title = "Unit")
        public Freq unit;

        enum Freq { DAY, WEEK, MONTH }
    }

    static class AnnotationOnlyTypes {
        @Schema(type = "object")
        public Object objectType;

        @Schema(type = "array")
        public Object arrayType;

        @Schema(type = "null")
        public Object nullType;

        @Schema(title = "Blank type")
        public Object blankType;

        @Schema(title = "Nullable blank type", nullable = true)
        public Object nullableBlankType;

        @Schema(type = "number", nullable = true)
        public Object nullableNumberType;

        @Schema(type = "number", types = {"boolean"}, nullable = true)
        public Object nullableTypesOverride;
    }

    @SuppressWarnings("unchecked")
    private java.util.Set<String> typesOf(io.swagger.v3.oas.models.media.Schema schema, String property) {
        return schemaOf(schema, property).getTypes();
    }

    private io.swagger.v3.oas.models.media.Schema schemaOf(
            io.swagger.v3.oas.models.media.Schema schema, String property) {
        return (io.swagger.v3.oas.models.media.Schema) schema.getProperties().get(property);
    }

    private void assertSerializedTypes(JsonNode types, String first, String second) {
        assertEquals(types.size(), 2);
        assertEquals(types.path(0).asText(), first);
        assertEquals(types.path(1).asText(), second);
    }

    private void assertAnnotationOnlyType(String fieldName, String expectedType) throws NoSuchFieldException {
        io.swagger.v3.oas.models.media.Schema schema = annotationOnlySchema(fieldName, true);

        assertNull(schema.getType());
        assertEquals(schema.getTypes(), Collections.singleton(expectedType));
        assertEquals(Json31.mapper().valueToTree(schema).path("type").asText(), expectedType);
    }

    private io.swagger.v3.oas.models.media.Schema annotationOnlySchema(String fieldName, boolean openapi31)
            throws NoSuchFieldException {
        Schema annotation = AnnotationOnlyTypes.class.getField(fieldName).getAnnotation(Schema.class);
        return AnnotationsUtils.getSchemaFromAnnotation(annotation, null, null, openapi31)
                .orElseThrow(AssertionError::new);
    }
}
