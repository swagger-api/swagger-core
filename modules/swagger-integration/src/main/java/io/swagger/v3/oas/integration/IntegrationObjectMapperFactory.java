package io.swagger.v3.oas.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.ObjectMapperFactory;

public class IntegrationObjectMapperFactory extends ObjectMapperFactory {

    public static ObjectMapper createJson() {
        return ObjectMapperFactory.createJson();
    }

    public static ObjectMapper createJson31() {
        return ObjectMapperFactory.createJson31();
    }
}
