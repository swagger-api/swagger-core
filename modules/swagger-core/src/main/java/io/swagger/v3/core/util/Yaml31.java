package io.swagger.v3.core.util;

import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.cfg.MapperBuilder;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class Yaml31 {

    private static final MapperHolder HOLDER = new MapperHolder(ObjectMapperFactory::createYaml31);

    private static final Logger LOGGER = LoggerFactory.getLogger(Yaml31.class);

    /**
     * Returns the shared OpenAPI 3.1 YAML mapper. It is built lazily from {@link ObjectMapperFactory} and rebuilt when
     * {@link MapperCustomizer}s are added or removed, unless a mapper was installed explicitly with
     * {@link #mapper(ObjectMapper)} or {@link #configure(Consumer)}.
     *
     * @return the shared mapper
     */
    public static ObjectMapper mapper() {
        return HOLDER.get();
    }

    /**
     * Replaces the shared mapper. The given instance is used as-is until {@link #reset()} is called; registered
     * {@link MapperCustomizer}s are not applied to it. Prefer {@link ObjectMapperFactory#addCustomizer(MapperCustomizer)}
     * to customize all swagger-core mappers consistently, or {@link #configure(Consumer)} to tweak just this one.
     *
     * @param mapper the mapper to install
     * @since 3.0.0
     */
    public static void mapper(ObjectMapper mapper) {
        HOLDER.set(mapper);
    }

    /**
     * Rebuilds the shared mapper from its current configuration, applying the given customization, and installs
     * the result. This is the Jackson 3 replacement for mutating {@code Yaml31.mapper()} directly, e.g.
     * {@code Yaml31.configure(b -> b.addModule(new KotlinModule.Builder().build()))}.
     * Only this mapper is affected; use {@link ObjectMapperFactory#addCustomizer(MapperCustomizer)} to customize
     * all swagger-core mappers.
     *
     * @param customizer customization applied to a builder seeded from the current mapper
     * @return the new shared mapper
     * @since 3.0.0
     */
    public static ObjectMapper configure(Consumer<MapperBuilder<?, ?>> customizer) {
        return HOLDER.configure(customizer);
    }

    /**
     * Adds a module to the shared mapper, shortcut for {@code configure(b -> b.addModule(module))}.
     *
     * @param module the module to add
     * @return the new shared mapper
     * @since 3.0.0
     */
    public static ObjectMapper addModule(JacksonModule module) {
        Objects.requireNonNull(module, "module");
        return configure(b -> b.addModule(module));
    }

    /**
     * Discards any explicitly installed mapper; the next call to {@link #mapper()} builds a fresh one from
     * {@link ObjectMapperFactory} with the currently registered customizers.
     * @since 3.0.0
     */
    public static void reset() {
        HOLDER.reset();
    }

    public static ObjectWriter pretty() {
        return mapper().writerWithDefaultPrettyPrinter();
    }

    public static String pretty(Object o) {
        try {
            return pretty().writeValueAsString(o);
        } catch (Exception e) {
            PrettyPrintHelper.emitError(LOGGER, "Error serializing object to YAML (3.1)", e);
            return null;
        }
    }

    public static void prettyPrint(Object o) {
        try {
            String prettyString = pretty().writeValueAsString(o);
            PrettyPrintHelper.emit(LOGGER, prettyString);
        } catch (Exception e) {
            PrettyPrintHelper.emitError(LOGGER, "Error pretty-printing YAML (3.1)", e);
        }
    }

    public static Map<String, Object> jsonSchemaAsMap(String jsonSchema) {
        try {
            return mapper().readValue(jsonSchema, Map.class);
        } catch (JacksonException e) {
            LOGGER.error("Exception converting jsonSchema to Map", e);
            return null;
        }
    }

    public static Map<String, Object> jsonSchemaAsMap(Schema schema) {
        try {
            return mapper().readValue(mapper().writeValueAsString(schema), Map.class);
        } catch (JacksonException e) {
            LOGGER.error("Exception converting jsonSchema to Map", e);
            return null;
        }
    }
}
