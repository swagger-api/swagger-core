package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import javax.validation.constraints.Email;
import java.net.URI;

import static io.swagger.v3.core.resolving.SwaggerTestBase.mapper;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class Ticket5185Test {

    @Test
    public void testExplicitFormatOverridesTypeDerivedFormat() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(UriExample.class));

        assertNotNull(schema);
        io.swagger.v3.oas.models.media.Schema uriReference =
                (io.swagger.v3.oas.models.media.Schema) schema.getProperties().get("uriReferenceValue");
        assertNotNull(uriReference);
        assertEquals(uriReference.getFormat(), "uri-reference");

        // A URI field without an explicit @Schema(format=...) still resolves to the default "uri" format
        io.swagger.v3.oas.models.media.Schema plainUri =
                (io.swagger.v3.oas.models.media.Schema) schema.getProperties().get("uriValue");
        assertNotNull(plainUri);
        assertEquals(plainUri.getFormat(), "uri");
    }

    @Test
    public void testExplicitFormatPrecedenceForStringAndNumericTypes() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(ExplicitFormatExample.class));

        assertNotNull(schema);
        assertProperty(schema, "uuidValue", "string", "uuid");
        assertProperty(schema, "emailValue", "string", "email");
        assertProperty(schema, "emailValidatedValue", "string", "email");
        assertProperty(schema, "beanValidationEmailValue", "string", "email");
        assertProperty(schema, "longValue", "integer", "int64");
        assertProperty(schema, "integerAsInt64Value", "integer", "int64");
    }

    @Test
    public void testExplicitFormatOverridesTypeDerivedFormatOpenApi31() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(UriExample.class));

        assertNotNull(schema);
        io.swagger.v3.oas.models.media.Schema uriReference =
                (io.swagger.v3.oas.models.media.Schema) schema.getProperties().get("uriReferenceValue");
        assertNotNull(uriReference);
        assertEquals(uriReference.getFormat(), "uri-reference");

        io.swagger.v3.oas.models.media.Schema plainUri =
                (io.swagger.v3.oas.models.media.Schema) schema.getProperties().get("uriValue");
        assertNotNull(plainUri);
        assertEquals(plainUri.getFormat(), "uri");
    }

    @Test
    public void testExplicitFormatPrecedenceForStringAndNumericTypesOpenApi31() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        io.swagger.v3.oas.models.media.Schema schema = context.resolve(new AnnotatedType(ExplicitFormatExample.class));

        assertNotNull(schema);
        assertProperty31(schema, "uuidValue", "string", "uuid");
        assertProperty31(schema, "emailValue", "string", "email");
        assertProperty31(schema, "emailValidatedValue", "string", "email");
        assertProperty31(schema, "beanValidationEmailValue", "string", "email");
        assertProperty31(schema, "longValue", "integer", "int64");
        assertProperty31(schema, "integerAsInt64Value", "integer", "int64");
    }

    private static void assertProperty(io.swagger.v3.oas.models.media.Schema schema, String propertyName, String type, String format) {
        io.swagger.v3.oas.models.media.Schema property =
                (io.swagger.v3.oas.models.media.Schema) schema.getProperties().get(propertyName);
        assertNotNull(property);
        assertEquals(property.getType(), type);
        assertEquals(property.getFormat(), format);
    }

    private static void assertProperty31(io.swagger.v3.oas.models.media.Schema schema, String propertyName, String type, String format) {
        io.swagger.v3.oas.models.media.Schema property =
                (io.swagger.v3.oas.models.media.Schema) schema.getProperties().get(propertyName);
        assertNotNull(property);
        assertNotNull(property.getTypes());
        assertTrue(property.getTypes().contains(type));
        assertEquals(property.getFormat(), format);
    }

    static class UriExample {
        @Schema(format = "uri-reference")
        public URI uriReferenceValue;

        public URI uriValue;
    }

    static class ExplicitFormatExample {
        @Schema(format = "uuid")
        public String uuidValue;

        @Schema(format = "email")
        public String emailValue;

        @Schema(format = "email")
        @Email
        public String emailValidatedValue;

        @Email
        public String beanValidationEmailValue;

        @Schema(format = "int64")
        public Long longValue;

        @Schema(format = "int64")
        public Integer integerAsInt64Value;
    }
}
