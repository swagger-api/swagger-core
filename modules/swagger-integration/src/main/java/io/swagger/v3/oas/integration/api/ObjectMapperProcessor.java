package io.swagger.v3.oas.integration.api;

import tools.jackson.databind.ObjectMapper;

/**
 * @since 2.0.6
 */
public interface ObjectMapperProcessor {

    default void processJsonObjectMapper(ObjectMapper mapper) {};

    /**
     * @deprecated since 2.0.7, as no-op
     *
     */
    @Deprecated
    default void processYamlObjectMapper(ObjectMapper mapper) {}

    /**
     * @return
     * @since 2.1.6
     */
    default ObjectMapper processOutputJsonObjectMapper(ObjectMapper mapper) {
        return mapper;
    }

    /**
     * @since 2.1.6
     */
    default ObjectMapper processOutputYamlObjectMapper(ObjectMapper mapper) {
        return processOutputJsonObjectMapper(mapper);
    }
}
