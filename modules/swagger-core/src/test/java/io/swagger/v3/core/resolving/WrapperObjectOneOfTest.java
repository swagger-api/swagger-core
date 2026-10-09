package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * A schema that already carries a {@code oneOf} must keep it when
 * {@code @JsonTypeInfo(include = WRAPPER_OBJECT)} wrapping is applied.
 * <p>
 * {@code ModelResolver.resolveWrapping(...)} used to nest the whole model inside a new wrapper
 * schema, which dropped the {@code oneOf} (the wrapper only copied name + properties). The
 * composition is now preserved and the wrapper property is added alongside it.
 */
public class WrapperObjectOneOfTest {

    @Test
    public void wrapperObjectPreservesOneOf() {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema<?> model = context.resolve(new AnnotatedType(WrappedOneOf.class));

        assertTrue(model instanceof ComposedSchema,
                "the explicit @Schema(oneOf) must produce a ComposedSchema, got: " + model.getClass().getName());
        final ComposedSchema composed = (ComposedSchema) model;
        assertNotNull(composed.getOneOf(), "oneOf must be preserved when WRAPPER_OBJECT wrapping is applied");
        assertEquals(composed.getOneOf().size(), 2, "oneOf should reference both subtypes");
        assertEquals(composed.getOneOf().get(0).get$ref(), "#/components/schemas/Dog");
        assertEquals(composed.getOneOf().get(1).get$ref(), "#/components/schemas/Cat");
        // the WRAPPER_OBJECT property is added next to oneOf, not nested around it
        assertNotNull(composed.getProperties(), "the wrapper property must be present alongside oneOf");
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "pet")
    @io.swagger.v3.oas.annotations.media.Schema(oneOf = {Dog.class, Cat.class})
    static abstract class WrappedOneOf {
        public String petType;
    }

    static class Dog extends WrappedOneOf {
        public String bark;
    }

    static class Cat extends WrappedOneOf {
        public String meow;
    }
}