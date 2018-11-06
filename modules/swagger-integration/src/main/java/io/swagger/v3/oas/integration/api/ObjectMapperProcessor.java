package io.swagger.v3.oas.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @since 2.0.6
 */
public interface ObjectMapperProcessor {

    void processJsonObjectMapper(ObjectMapper mapper);
    void processYamlObjectMapper(ObjectMapper mapper);
}
