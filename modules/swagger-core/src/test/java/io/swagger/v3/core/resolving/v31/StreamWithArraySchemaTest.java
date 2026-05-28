package io.swagger.v3.core.resolving.v31;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.resolving.SwaggerTestBase;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import java.util.stream.Stream;

import static org.testng.Assert.*;

/**
 * Test for issue #5013: @ArraySchema applied on a stream property results in object type in OAS 3.1
 */
public class StreamWithArraySchemaTest extends SwaggerTestBase {

    @Test
    public void testStreamPropertyWithArraySchemaOAS31() {
        ModelResolver resolver = new ModelResolver(mapper()).openapi31(true);
        ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        
        io.swagger.v3.oas.models.media.Schema model = resolver.resolve(
            new AnnotatedType().type(ModelWithStream.class),
            context,
            null
        );

        assertNotNull(model);
        io.swagger.v3.oas.models.media.Schema greetingsProperty = (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("greetings");
        
        assertNotNull(greetingsProperty);
        assertNotNull(greetingsProperty.getTypes());
        assertTrue(greetingsProperty.getTypes().contains("array"), "Expected types to contain 'array' but got: " + greetingsProperty.getTypes());
        assertFalse(greetingsProperty.getTypes().contains("object"));
        
        assertNotNull(greetingsProperty.getItems());
        assertEquals(greetingsProperty.getItems().get$ref(), "#/components/schemas/Greeting");
        
        assertNull(greetingsProperty.getProperties());
    }

    @Test
    public void testStreamPropertyWithoutArraySchemaOAS31() {
        ModelResolver resolver = new ModelResolver(mapper()).openapi31(true);
        ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        
        io.swagger.v3.oas.models.media.Schema model = resolver.resolve(
            new AnnotatedType().type(ModelWithStreamNoAnnotation.class),
            context,
            null
        );
        
        assertNotNull(model);
        io.swagger.v3.oas.models.media.Schema itemsProperty = (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("items");
        
        assertNotNull(itemsProperty);
        assertTrue(itemsProperty.getTypes().contains("object"));
        assertFalse(itemsProperty.getTypes() != null && itemsProperty.getTypes().contains("array"));
    }

    @Test
    public void testStreamPropertyWithoutArraySchemaOAS30() {
        ModelResolver resolver = new ModelResolver(mapper()).openapi31(false);
        ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        
        io.swagger.v3.oas.models.media.Schema model = resolver.resolve(
            new AnnotatedType().type(ModelWithStreamNoAnnotation.class),
            context,
            null
        );
        
        assertNotNull(model);
        io.swagger.v3.oas.models.media.Schema itemsProperty = (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("items");
        
        assertNotNull(itemsProperty);
        assertEquals(itemsProperty.getType(), "object");
        assertNotEquals(itemsProperty.getType(), "array");
    }

    @Test
    public void testStreamPropertyWithArraySchemaOAS30() {
        ModelResolver resolver = new ModelResolver(mapper()).openapi31(false);
        ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        
        io.swagger.v3.oas.models.media.Schema model = resolver.resolve(
            new AnnotatedType().type(ModelWithStream.class),
            context,
            null
        );
        
        assertNotNull(model);
        io.swagger.v3.oas.models.media.Schema greetingsProperty = (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("greetings");
        
        assertNotNull(greetingsProperty);
        assertEquals(greetingsProperty.getType(), "array");
        assertNotNull(greetingsProperty.getItems());
        assertEquals(greetingsProperty.getItems().get$ref(), "#/components/schemas/Greeting");
        assertNull(greetingsProperty.getProperties());
    }

    @Test
    public void testStreamPropertyWithDetailedAnnotations() {
        ModelResolver resolver = new ModelResolver(mapper()).openapi31(true);
        ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        
        io.swagger.v3.oas.models.media.Schema model = resolver.resolve(
            new AnnotatedType().type(ModelWithDetailedStreamAnnotations.class),
            context,
            null
        );
        
        assertNotNull(model);
        io.swagger.v3.oas.models.media.Schema greetingsProperty = (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("greetings");

        assertNotNull(greetingsProperty);
        assertNotNull(greetingsProperty.getTypes());
        assertTrue(greetingsProperty.getTypes().contains("array"));
        
        assertNotNull(greetingsProperty.getItems());
        assertEquals(greetingsProperty.getItems().get$ref(), "#/components/schemas/Greeting");
        
        assertEquals(greetingsProperty.getMinItems(), Integer.valueOf(1));
        assertEquals(greetingsProperty.getMaxItems(), Integer.valueOf(10));
        assertTrue(greetingsProperty.getUniqueItems());
        
        assertEquals(greetingsProperty.getDescription(), "A collection of greetings");
    }

    @Test
    public void testListPropertyWithDetailedAnnotations() {
        ModelResolver resolver = new ModelResolver(mapper()).openapi31(true);
        ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        
        io.swagger.v3.oas.models.media.Schema model = resolver.resolve(
            new AnnotatedType().type(ModelWithDetailedListAnnotations.class),
            context,
            null
        );

        
        assertNotNull(model);
        io.swagger.v3.oas.models.media.Schema greetingsProperty = (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("greetings");

        assertNotNull(greetingsProperty);
    }

    public static class ModelWithStream {
        @ArraySchema(schema = @Schema(implementation = Greeting.class))
        private Stream<Greeting> greetings;
        
        public Stream<Greeting> getGreetings() {
            return greetings;
        }
        
        public void setGreetings(Stream<Greeting> greetings) {
            this.greetings = greetings;
        }
    }

    public static class Greeting {
        private String message;
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ModelWithStreamNoAnnotation {
        private Stream<String> items;
        
        public Stream<String> getItems() {
            return items;
        }
        
        public void setItems(Stream<String> items) {
            this.items = items;
        }
    }

    public static class ModelWithDetailedStreamAnnotations {
        @ArraySchema(
            minItems = 1,
            maxItems = 10,
            uniqueItems = true,
            arraySchema = @Schema(
                description = "A collection of greetings",
                example = "[{\"message\": \"Hello\"}]"
            ),
            schema = @Schema(implementation = Greeting.class)
        )
        private Stream<Greeting> greetings;
        
        public Stream<Greeting> getGreetings() {
            return greetings;
        }
        
        public void setGreetings(Stream<Greeting> greetings) {
            this.greetings = greetings;
        }
    }

    public static class ModelWithDetailedListAnnotations {
        @ArraySchema(
            minItems = 1,
            maxItems = 10,
            uniqueItems = true,
            arraySchema = @Schema(
                description = "A collection of greetings",
                example = "[{\"message\": \"Hello\"}]"
            ),
            schema = @Schema(implementation = Greeting.class)
        )
        private java.util.List<Greeting> greetings;
        
        public java.util.List<Greeting> getGreetings() {
            return greetings;
        }
        
        public void setGreetings(java.util.List<Greeting> greetings) {
            this.greetings = greetings;
        }
    }
}
