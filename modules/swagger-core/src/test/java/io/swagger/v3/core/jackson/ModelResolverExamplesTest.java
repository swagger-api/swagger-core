package io.swagger.v3.core.jackson;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Regression tests for examples duplication in {@link ModelResolver}.
 * <p>
 * {@code ModelResolver.resolveSchemaMembers(...)} reads the {@code examples} member of the
 * {@code @Schema} annotation and used to merge the parsed values into any examples already present
 * on the target schema:
 *
 * <pre>
 * if (schema.getExamples() == null || schema.getExamples().isEmpty()) {
 *     schema.setExamples(parsedExamples);
 * } else {
 *     schema.getExamples().addAll(parsedExamples);
 * }
 * </pre>
 *
 * Because the resolver can apply the same annotation to the same schema more than once (for
 * example when a downstream tool such as springdoc-openapi re-processes an already resolved
 * schema), the {@code addAll} branch appended the same values again, producing duplicated
 * {@code examples} entries such as {@code ["Hello", "World", "Hello", "World"]}.
 * <p>
 * The examples resolution must therefore be idempotent: applying the same annotation twice must
 * yield the same list as applying it once.
 */
public class ModelResolverExamplesTest {

    @Test
    public void resolvingSchemaMembersTwiceDoesNotDuplicateExamples() {
        final ModelResolver resolver = new ModelResolver(Json31.mapper());
        resolver.openapi31(true);

        final StringSchema schema = new StringSchema();
        // Simulate an already resolved schema that carries parsed examples.
        schema.setExamples(new ArrayList<>(Arrays.asList("Hello", "World", "existing")));

        resolver.resolveSchemaMembers(schema, new AnnotatedType(Bean.class));

        assertEquals(schema.getExamples(), Arrays.asList("Hello", "World", "existing"),
                "applying the same @Schema(examples) annotation again must not duplicate the values nor drop existing ones");
    }

    @Test
    public void resolvingSameModelTwiceKeepsExamplesStable() {
        final ModelResolver resolver = new ModelResolver(Json31.mapper());
        resolver.openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);

        final io.swagger.v3.oas.models.media.Schema first =
                context.resolve(new AnnotatedType(Bean.class));
        final List<Object> firstExamples = new ArrayList<>(first.getExamples());

        resolver.resolveSchemaMembers(first, new AnnotatedType(Bean.class));

        assertEquals(first.getExamples(), firstExamples,
                "re-resolving a model must not duplicate its examples");
    }

    @Test
    public void preExistingExamplesAreNotLostWhenAnnotationExamplesApplied() {
        final ModelResolver resolver = new ModelResolver(Json31.mapper());
        resolver.openapi31(true);

        final StringSchema schema = new StringSchema();
        schema.setExamples(new ArrayList<>(Arrays.asList("Existing")));

        resolver.resolveSchemaMembers(schema, new AnnotatedType(Bean.class));

        assertTrue(schema.getExamples().containsAll(Arrays.asList("Hello", "World", "Existing")),
                "annotation examples missing, examples=" + schema.getExamples());
    }

    @Test
    public void examplesSeededByResolverSubclassAreNotLost() {
        // customization: subclass seeds examples before the standard member resolution
        final ModelResolver resolver = new ModelResolver(Json31.mapper()) {
            @Override
            protected void resolveSchemaMembers(io.swagger.v3.oas.models.media.Schema schema, AnnotatedType annotatedType,
                                                ModelConverterContext context, Iterator<ModelConverter> next) {
                schema.setExamples(new ArrayList<>(Arrays.asList("Seeded")));
                super.resolveSchemaMembers(schema, annotatedType, context, next);
            }
        };
        resolver.openapi31(true);
        final ModelConverters converters = new ModelConverters(true);
        converters.addConverter(resolver);

        final Map<String, io.swagger.v3.oas.models.media.Schema> models = converters.readAll(Bean.class);
        final List<Object> examples = models.get("Bean").getExamples();

        assertTrue(examples.containsAll(Arrays.asList("Hello", "World", "Seeded")), "examples=" + examples);
    }

    @Schema(examples = {"Hello", "World"})
    static class Bean {
        public String value;
    }
}