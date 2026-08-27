package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
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

        @Schema(title = "Unit")
        public Freq unit;

        enum Freq { DAY, WEEK, MONTH }
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
}
