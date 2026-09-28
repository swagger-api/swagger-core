package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.MapperCustomizer;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tools.jackson.databind.PropertyNamingStrategies;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * The default {@link io.swagger.v3.core.jackson.ModelResolver} captures its mapper at construction; verifies that
 * {@link ModelConverters} rebuilds it when customizers are registered later, so that model introspection sees
 * the change just as it did with mutable Jackson 2 mappers.
 */
public class ModelConvertersCustomizerTest {

    public static class Person {
        public String firstName;
        public String lastName;
    }

    /** A user-added converter that must survive the default resolver being refreshed. */
    private static class NoopConverter implements ModelConverter {
        @Override
        public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
        }
    }

    private static final MapperCustomizer SNAKE_CASE =
            (builder, target) -> builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @BeforeMethod
    @AfterMethod
    public void resetGlobalState() {
        ObjectMapperFactory.clearCustomizers();
        Json.reset();
        Yaml.reset();
        Json31.reset();
        Yaml31.reset();
        ModelConverters.reset();
    }

    @Test
    public void customizerRegisteredAfterInstanceCreationIsUsedByDefaultResolver() {
        ModelConverters converters = ModelConverters.getInstance();
        assertTrue(properties(converters).containsKey("firstName"));

        ObjectMapperFactory.addCustomizer(SNAKE_CASE);

        assertSame(ModelConverters.getInstance(), converters, "singleton is kept");
        Map<String, Schema> properties = properties(converters);
        assertTrue(properties.containsKey("first_name"), properties.keySet().toString());
        assertTrue(properties.containsKey("last_name"), properties.keySet().toString());
    }

    @Test
    public void customizerAppliesToOpenApi31Instance() {
        ModelConverters converters = ModelConverters.getInstance(true);
        assertTrue(properties(converters).containsKey("firstName"));

        ObjectMapperFactory.addCustomizer(SNAKE_CASE);

        assertTrue(properties(converters).containsKey("first_name"));
    }

    @Test
    public void userConvertersSurviveRefresh() {
        ModelConverters converters = ModelConverters.getInstance();
        NoopConverter userConverter = new NoopConverter();
        converters.addConverter(userConverter);
        List<ModelConverter> before = converters.getConverters();
        ModelConverter defaultResolver = before.get(before.size() - 1);

        ObjectMapperFactory.addCustomizer(SNAKE_CASE);

        List<ModelConverter> after = converters.getConverters();
        assertEquals(after.size(), before.size());
        assertSame(after.get(0), userConverter, "user converter keeps its position");
        assertNotSame(after.get(after.size() - 1), defaultResolver, "default resolver was rebuilt");
        assertTrue(properties(converters).containsKey("first_name"));
    }

    @Test
    public void removingCustomizerRestoresDefaultResolution() {
        ObjectMapperFactory.addCustomizer(SNAKE_CASE);
        ModelConverters converters = ModelConverters.getInstance();
        assertTrue(properties(converters).containsKey("first_name"));

        ObjectMapperFactory.removeCustomizer(SNAKE_CASE);

        assertTrue(properties(converters).containsKey("firstName"));
    }

    private static Map<String, Schema> properties(ModelConverters converters) {
        Map<String, Schema> models = converters.readAll(Person.class);
        Schema person = models.get("Person");
        assertTrue(person != null, "Person schema resolved: " + models.keySet());
        return person.getProperties();
    }
}
