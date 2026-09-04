package io.swagger.v3.oas.integration.api;

import tools.jackson.databind.ObjectMapper;

public interface ObjectMapperProcessor {

    default ObjectMapper processJsonObjectMapper(ObjectMapper mapper) {
        return mapper;
    }

    default ObjectMapper processOutputJsonObjectMapper(ObjectMapper mapper) {
        return mapper;
    }

    default ObjectMapper processOutputYamlObjectMapper(ObjectMapper mapper) {
        return processOutputJsonObjectMapper(mapper);
    }
}
