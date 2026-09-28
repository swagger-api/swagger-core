package io.swagger.v3.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * Covers the Jackson 3 replacement for {@code Json.mapper().registerModule(...)}: customizers registered with
 * {@link ObjectMapperFactory} and the replaceable mappers held by {@link Json}, {@link Yaml}, {@link Json31}
 * and {@link Yaml31}.
 */
public class MapperCustomizerTest {

    /** Serialized by a custom serializer only when the test module is registered. */
    public static class Marker {
        public String value = "plain";
    }

    private static SimpleModule markerModule() {
        return new SimpleModule("marker").addSerializer(Marker.class, new ValueSerializer<Marker>() {
            @Override
            public void serialize(Marker value, JsonGenerator gen, SerializationContext ctxt) {
                gen.writeString("customized");
            }
        });
    }

    @BeforeMethod
    @AfterMethod
    public void resetGlobalState() {
        ObjectMapperFactory.clearCustomizers();
        Json.reset();
        Yaml.reset();
        Json31.reset();
        Yaml31.reset();
    }

    @Test
    public void defaultsWithoutCustomizers() {
        assertEquals(Json.mapper().writeValueAsString(new Marker()), "{\"value\":\"plain\"}");
        assertTrue(ObjectMapperFactory.getCustomizers().isEmpty());
    }

    @Test
    public void addModuleAppliesToEveryMapper() {
        ObjectMapperFactory.addModule(markerModule());

        assertEquals(Json.mapper().writeValueAsString(new Marker()), "\"customized\"");
        assertEquals(Json31.mapper().writeValueAsString(new Marker()), "\"customized\"");
        assertEquals(Json31.converterMapper().writeValueAsString(new Marker()), "\"customized\"");
        assertEquals(Yaml.mapper().writeValueAsString(new Marker()).trim(), "customized");
        assertEquals(Yaml31.mapper().writeValueAsString(new Marker()).trim(), "customized");
    }

    @Test
    public void customizerSeesEveryTarget() {
        List<MapperTarget> seen = new ArrayList<>();
        ObjectMapperFactory.addCustomizer((builder, target) -> seen.add(target));

        Json.mapper();
        Yaml.mapper();
        Json31.mapper();
        Yaml31.mapper();
        Json31.converterMapper();

        assertEquals(EnumSet.copyOf(seen), EnumSet.allOf(MapperTarget.class));
        assertTrue(Yaml.mapper() instanceof YAMLMapper);
        assertTrue(Yaml31.mapper() instanceof YAMLMapper);
    }

    @Test
    public void customizerRunsAfterSwaggerDefaultsSoItWins() {
        // swagger-core sets NON_NULL inclusion; a customizer can override it
        ObjectMapperFactory.addCustomizer((builder, target) ->
                builder.changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.ALWAYS)));

        String json = Json.mapper().writeValueAsString(new Info().title("t"));
        assertTrue(json.contains("\"description\":null"), json);
    }

    @Test
    public void customizerRegisteredAfterFirstUseTakesEffect() {
        ObjectMapper before = Json.mapper();
        assertEquals(before.writeValueAsString(new Marker()), "{\"value\":\"plain\"}");

        ObjectMapperFactory.addModule(markerModule());

        ObjectMapper after = Json.mapper();
        assertNotSame(after, before);
        assertSame(Json.mapper(), after, "mapper is cached until the next change");
        assertEquals(after.writeValueAsString(new Marker()), "\"customized\"");
    }

    @Test
    public void removeAndClearCustomizersRestoreDefaults() {
        MapperCustomizer customizer = (builder, target) -> builder.addModule(markerModule());
        ObjectMapperFactory.addCustomizer(customizer);
        assertEquals(Json.mapper().writeValueAsString(new Marker()), "\"customized\"");

        assertTrue(ObjectMapperFactory.removeCustomizer(customizer));
        assertFalse(ObjectMapperFactory.removeCustomizer(customizer));
        assertEquals(Json.mapper().writeValueAsString(new Marker()), "{\"value\":\"plain\"}");

        ObjectMapperFactory.addCustomizer(customizer);
        ObjectMapperFactory.clearCustomizers();
        assertEquals(Json.mapper().writeValueAsString(new Marker()), "{\"value\":\"plain\"}");
    }

    @Test
    public void strictGenericMapperIsNotCustomized() {
        ObjectMapperFactory.addModule(markerModule());
        assertEquals(ObjectMapperFactory.buildStrictGenericObjectMapper().writeValueAsString(new Marker()),
                "{\"value\":\"plain\"}");
    }

    @Test
    public void perMapperAddModuleOnlyAffectsThatMapper() {
        Json.addModule(markerModule());

        assertEquals(Json.mapper().writeValueAsString(new Marker()), "\"customized\"");
        assertEquals(Json31.mapper().writeValueAsString(new Marker()), "{\"value\":\"plain\"}");
        assertEquals(Yaml.mapper().writeValueAsString(new Marker()).trim(), "value: plain");
    }

    @Test
    public void configureKeepsSwaggerDefaultsAndReturnsInstalledMapper() {
        ObjectMapper configured = Yaml.configure(builder -> builder.addModule(markerModule()));

        assertSame(Yaml.mapper(), configured);
        assertTrue(configured instanceof YAMLMapper);
        assertEquals(configured.writeValueAsString(new Marker()).trim(), "customized");
        // swagger mixins/inclusion survive the rebuild
        String yaml = configured.writeValueAsString(new OpenAPI().info(new Info().title("t")));
        assertEquals(yaml.trim(), "openapi: 3.0.1\ninfo:\n  title: t");
    }

    @Test
    public void explicitlyInstalledMapperIsKeptUntilReset() {
        ObjectMapper custom = ObjectMapperFactory.createJson().rebuild().addModule(markerModule()).build();
        Json.mapper(custom);
        assertSame(Json.mapper(), custom);

        // factory changes do not touch an explicitly installed mapper
        ObjectMapperFactory.addCustomizer((builder, target) ->
                builder.changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.ALWAYS)));
        assertSame(Json.mapper(), custom);
        // ...but do apply to the others
        assertTrue(Yaml.mapper().writeValueAsString(new Info().title("t")).contains("description: null"));

        Json.reset();
        assertNotSame(Json.mapper(), custom);
        assertTrue(Json.mapper().writeValueAsString(new Info().title("t")).contains("\"description\":null"));
    }

    @Test
    public void json31ResetAlsoResetsConverterMapper() {
        ObjectMapper custom = ObjectMapperFactory.createJsonConverter();
        Json31.converterMapper(custom);
        assertSame(Json31.converterMapper(), custom);

        Json31.reset();
        assertNotSame(Json31.converterMapper(), custom);
    }
}
