package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class Ticket5235Test extends SwaggerTestBase {

    @Test
    public void testExplicitNonStringTypesInOpenApi31() {
        io.swagger.v3.oas.models.media.Schema schema = resolve(true, null);

        assertSerializedTypes(Json31.mapper().valueToTree(schema));
        assertEquals(property(schema, "booleanOverride").getTypes(), Collections.singleton("boolean"));
        assertEquals(property(schema, "numberOverride").getTypes(), Collections.singleton("number"));
        assertEquals(property(schema, "integerOverride").getTypes(), Collections.singleton("integer"));
        assertNull(property(schema, "integerOverride").getFormat(), "format must not leak from mapped Java class");
        assertEquals(property(schema, "integerWithFormatOverride").getFormat(), "int32");
    }

    @Test(dataProvider = "siblingSchemaResolutions")
    public void testExplicitNonStringTypesWithSiblingsEnabled(
            io.swagger.v3.oas.models.media.Schema.SchemaResolution schemaResolution) {
        io.swagger.v3.oas.models.media.Schema schema = resolve(false, schemaResolution);

        assertSerializedTypes(Json.mapper().valueToTree(schema));
        assertEquals(property(schema, "booleanOverride").getType(), "boolean");
        assertEquals(property(schema, "numberOverride").getType(), "number");
        assertEquals(property(schema, "integerOverride").getType(), "integer");
        assertNull(property(schema, "integerOverride").getFormat(), "format must not leak from mapped Java class");
        assertEquals(property(schema, "integerWithFormatOverride").getFormat(), "int32");
    }

    @DataProvider
    public Object[][] siblingSchemaResolutions() {
        return new Object[][] {
                {io.swagger.v3.oas.models.media.Schema.SchemaResolution.ALL_OF},
                {io.swagger.v3.oas.models.media.Schema.SchemaResolution.ALL_OF_REF}
        };
    }

    private io.swagger.v3.oas.models.media.Schema resolve(
            boolean openapi31, io.swagger.v3.oas.models.media.Schema.SchemaResolution schemaResolution) {
        ModelResolver resolver = new ModelResolver(mapper()).openapi31(openapi31);
        if (schemaResolution != null) {
            resolver.schemaResolution(schemaResolution);
        }
        return new ModelConverterContextImpl(resolver).resolve(new AnnotatedType(ExplicitTypes.class));
    }

    private void assertSerializedTypes(JsonNode schema) {
        JsonNode properties = schema.path("properties");
        assertEquals(properties.path("booleanOverride").path("type").asText(), "boolean");
        assertEquals(properties.path("numberOverride").path("type").asText(), "number");
        assertEquals(properties.path("integerOverride").path("type").asText(), "integer");
        assertTrue(properties.path("integerOverride").path("format").isMissingNode(), "spurious format must be absent");
        assertEquals(properties.path("integerWithFormatOverride").path("format").asText(), "int32");
    }

    private io.swagger.v3.oas.models.media.Schema property(
            io.swagger.v3.oas.models.media.Schema schema, String propertyName) {
        return (io.swagger.v3.oas.models.media.Schema) schema.getProperties().get(propertyName);
    }

    static class ExplicitTypes {

        @Schema(type = "boolean")
        public Integer booleanOverride;

        @Schema(type = "number")
        public String numberOverride;

        @Schema(type = "integer")
        public Boolean integerOverride;

        @Schema(type = "integer", format = "int32")
        public Boolean integerWithFormatOverride;
    }
}
