package io.swagger.v3.core.jackson;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

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
        schema.setExamples(new ArrayList<>(Arrays.asList("Hello", "World")));

        resolver.resolveSchemaMembers(schema, new AnnotatedType(Bean.class));

        assertEquals(schema.getExamples(), Arrays.asList("Hello", "World"),
                "applying the same @Schema(examples) annotation again must not duplicate the values");
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

    @Schema(examples = {"Hello", "World"})
    static class Bean {
        public String value;
    }
}