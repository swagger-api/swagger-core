package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * Regression for <a href="https://github.com/swagger-api/swagger-core/issues/5187">gh-5187</a>:
 * when the cycle guard added in #5004 fires, {@code AnnotationsUtils.getArraySchema}
 * is invoked with {@code processSchemaImplementation = false} and used to leave the
 * array's {@code items} unset entirely (degrading the schema to {@code type: array}
 * with no items, or dropping the property when downstream code stripped the empty
 * schema). The fallback should emit a {@code $ref} to the implementation type that
 * is already registered in the {@link ModelConverterContext}.
 */
public class Issue5187Test {

    @io.swagger.v3.oas.annotations.media.Schema
    static class Inner {
        public String name;
    }

    @io.swagger.v3.oas.annotations.media.Schema(name = "RenamedInner")
    static class RenamedInner {
        public String name;
    }

    @io.swagger.v3.oas.annotations.media.ArraySchema(
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Inner.class))
    static class HolderUsage {
    }

    @io.swagger.v3.oas.annotations.media.ArraySchema(
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RenamedInner.class))
    static class RenamedHolderUsage {
    }

    @Test
    public void cycleGuard_oas31_emitsRefToRegisteredImplementation() {
        ModelConverterContext context = new StubContext();
        context.defineModel("Inner", new JsonSchema());

        io.swagger.v3.oas.annotations.media.ArraySchema ann =
                HolderUsage.class.getAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);

        Schema existing = new JsonSchema().typesItem("array");
        Optional<Schema> result = AnnotationsUtils.getArraySchema(
                ann, null, null, /* openapi31 */ true, existing,
                /* processSchemaImplementation */ false, context);

        assertNotNull(result.orElse(null), "getArraySchema should produce a schema");
        Schema items = result.get().getItems();
        assertNotNull(items, "items must be populated via the cycle-guard fallback");
        assertEquals(items.get$ref(), "#/components/schemas/Inner");
    }

    @Test
    public void cycleGuard_oas30_emitsRefToRegisteredImplementation() {
        ModelConverterContext context = new StubContext();
        context.defineModel("Inner", new Schema());

        io.swagger.v3.oas.annotations.media.ArraySchema ann =
                HolderUsage.class.getAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);

        Optional<Schema> result = AnnotationsUtils.getArraySchema(
                ann, null, null, /* openapi31 */ false, /* existingSchema */ null,
                /* processSchemaImplementation */ false, context);

        assertNotNull(result.orElse(null), "getArraySchema should produce a schema");
        Schema items = result.get().getItems();
        assertNotNull(items, "items must be populated via the cycle-guard fallback");
        assertEquals(items.get$ref(), "#/components/schemas/Inner");
    }

    @Test
    public void cycleGuard_honoursExplicitSchemaName() {
        ModelConverterContext context = new StubContext();
        context.defineModel("RenamedInner", new Schema());

        io.swagger.v3.oas.annotations.media.ArraySchema ann =
                RenamedHolderUsage.class.getAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);

        Optional<Schema> result = AnnotationsUtils.getArraySchema(
                ann, null, null, false, null, false, context);

        Schema items = result.get().getItems();
        assertNotNull(items);
        assertEquals(items.get$ref(), "#/components/schemas/RenamedInner");
    }

    @Test
    public void cycleGuard_doesNotOverwriteExistingItems() {
        ModelConverterContext context = new StubContext();
        context.defineModel("Inner", new Schema());

        io.swagger.v3.oas.annotations.media.ArraySchema ann =
                HolderUsage.class.getAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);

        Schema preExisting = new JsonSchema().typesItem("array");
        preExisting.setItems(new JsonSchema().$ref("#/components/schemas/SomethingElse"));

        Optional<Schema> result = AnnotationsUtils.getArraySchema(
                ann, null, null, true, preExisting, false, context);

        // The fallback only runs when items is null; pre-existing items must be preserved.
        assertEquals(result.get().getItems().get$ref(), "#/components/schemas/SomethingElse");
    }

    @Test
    public void cycleGuard_doesNothingWhenTypeNotRegistered() {
        ModelConverterContext context = new StubContext();
        // Note: do not define Inner in the context.

        io.swagger.v3.oas.annotations.media.ArraySchema ann =
                HolderUsage.class.getAnnotation(io.swagger.v3.oas.annotations.media.ArraySchema.class);

        Optional<Schema> result = AnnotationsUtils.getArraySchema(
                ann, null, null, false, null, false, context);

        assertNull(result.get().getItems(),
                "without a registered schema we cannot invent a $ref; leave items unset");
    }

    /**
     * Minimal {@link ModelConverterContext} that records defined models — enough to
     * exercise the lookup used by the cycle-guard fallback without spinning up a
     * full {@code ModelConverters} chain.
     */
    private static final class StubContext implements ModelConverterContext {
        private final Map<String, Schema> models = new HashMap<>();

        @Override
        public void defineModel(String name, Schema model) {
            models.put(name, model);
        }

        @Override
        public void defineModel(String name, Schema model, AnnotatedType type, String previousName) {
            models.put(name, model);
        }

        @Override
        public void defineModel(String name, Schema model, java.lang.reflect.Type type, String previousName) {
            models.put(name, model);
        }

        @Override
        public Map<String, Schema> getDefinedModels() {
            return models;
        }

        @Override
        public Schema resolve(AnnotatedType type) {
            return null;
        }

        @Override
        public Iterator<ModelConverter> getConverters() {
            return java.util.Collections.<ModelConverter>emptyList().iterator();
        }
    }
}
