package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * A schema that already carries a {@code oneOf} must keep it when
 * {@code @JsonTypeInfo(include = WRAPPER_OBJECT)} wrapping is applied.
 * <p>
 * {@code ModelResolver.resolveWrapping(...)} used to nest the whole model inside a new wrapper
 * schema, which dropped the {@code oneOf} (the wrapper only copied name + properties). The
 * composition is now preserved: a model with a {@code oneOf} is returned unchanged.
 * Per-subtype wrapping is not modelled yet.
 */
public class WrapperObjectOneOfTest {

    @Test
    public void wrapperObjectPreservesOneOf() {
        final Schema<?> model = resolve(new ModelResolver(Json.mapper()), WrappedOneOf.class);

        assertTrue(model instanceof ComposedSchema,
                "the explicit @Schema(oneOf) must produce a ComposedSchema, got: " + model.getClass().getName());
        assertOneOfKept(model);
    }

    @Test
    public void wrapperObjectDoesNotAddWrapperProperty() {
        final Schema<?> model = resolve(new ModelResolver(Json.mapper()), WrappedOneOf.class);

        // only the declared member is present: no property named after the base type is added next to oneOf
        assertEquals(model.getProperties().keySet(), Collections.singleton("petType"));
    }

    @Test
    public void wrapperObjectOneOfIsStableWhenResolvedTwice() {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema<?> first = context.resolve(new AnnotatedType(WrappedOneOf.class));
        final Schema<?> second = context.resolve(new AnnotatedType(WrappedOneOf.class));

        assertOneOfKept(first);
        assertOneOfKept(second);
        assertEquals(second.getProperties().keySet(), first.getProperties().keySet());
    }

    @Test
    public void wrapperObjectPreservesOneOfOpenApi31() {
        final Schema<?> model = resolve(new ModelResolver(Json31.mapper()).openapi31(true), WrappedOneOf.class);

        assertOneOfKept(model);
        assertEquals(model.getProperties().keySet(), Collections.singleton("petType"));
    }

    @Test
    public void wrapperObjectWithoutOneOfIsStillWrapped() {
        final Schema<?> model = resolve(new ModelResolver(Json.mapper()), PlainWrapped.class);

        assertNull(model.getOneOf(), "a model without oneOf must not gain one");
        assertEquals(model.getProperties().keySet(), Collections.singleton("PlainWrapped"));
        final Schema<?> inner = (Schema<?>) model.getProperties().get("PlainWrapped");
        assertEquals(inner.getProperties().keySet(), Collections.singleton("value"));
    }

    private static Schema<?> resolve(ModelResolver modelResolver, Class<?> cls) {
        return new ModelConverterContextImpl(modelResolver).resolve(new AnnotatedType(cls));
    }

    private static void assertOneOfKept(Schema<?> model) {
        assertNotNull(model.getOneOf(), "oneOf must be preserved when WRAPPER_OBJECT wrapping is applied");
        assertEquals(model.getOneOf().size(), 2, "oneOf should reference both subtypes");
        assertEquals(model.getOneOf().get(0).get$ref(), "#/components/schemas/Dog");
        assertEquals(model.getOneOf().get(1).get$ref(), "#/components/schemas/Cat");
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    @io.swagger.v3.oas.annotations.media.Schema(oneOf = {Dog.class, Cat.class})
    static abstract class WrappedOneOf {
        public String petType;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    static class PlainWrapped {
        public String value;
    }

    static class Dog extends WrappedOneOf {
        public String bark;
    }

    static class Cat extends WrappedOneOf {
        public String meow;
    }
}