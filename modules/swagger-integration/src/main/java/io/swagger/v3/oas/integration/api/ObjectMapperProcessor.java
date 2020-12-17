package io.swagger.v3.oas.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;

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
     * @since 2.1.6
     */
    default void processOutputJsonObjectMapper(ObjectMapper mapper) {}

    /**
     * @since 2.1.6
     */
    default void processOutputYamlObjectMapper(ObjectMapper mapper) {
        processOutputJsonObjectMapper(mapper);
    }
}
