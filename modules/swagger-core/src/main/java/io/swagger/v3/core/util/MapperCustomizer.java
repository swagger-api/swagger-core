package io.swagger.v3.core.util;

import tools.jackson.databind.cfg.MapperBuilder;

/**
 * Hook to customize the Jackson {@link MapperBuilder}s used to build the {@link tools.jackson.databind.ObjectMapper}
 * instances that swagger-core relies on ({@link Json#mapper()}, {@link Yaml#mapper()}, {@link Json31#mapper()},
 * {@link Yaml31#mapper()} and {@link Json31#converterMapper()}).
 *
 * <p>Jackson 3 mappers are immutable, so the Jackson 2 idiom of {@code Json.mapper().registerModule(...)} is no
 * longer possible. Register a customizer via {@link ObjectMapperFactory#addCustomizer(MapperCustomizer)} instead
 * (or use the {@link ObjectMapperFactory#addModule(tools.jackson.databind.JacksonModule)} shortcut); it runs as
 * the last step of every mapper built by {@link ObjectMapperFactory}, after swagger-core's own configuration, so
 * customizations take precedence over the defaults.</p>
 *
 * <pre>{@code
 * ObjectMapperFactory.addCustomizer((builder, target) -> {
 *     builder.addModule(new KotlinModule.Builder().build());
 *     if (target.isYaml()) {
 *         builder.enable(SerializationFeature.INDENT_OUTPUT);
 *     }
 * });
 * }</pre>
 *
 * @since 3.0.0
 */
@FunctionalInterface
public interface MapperCustomizer {

    /**
     * Customize the builder for the given target.
     *
     * @param builder the builder, already configured with swagger-core's defaults
     * @param target  which mapper is being built
     * @since 3.0.0
     */
    void customize(MapperBuilder<?, ?> builder, MapperTarget target);
}
