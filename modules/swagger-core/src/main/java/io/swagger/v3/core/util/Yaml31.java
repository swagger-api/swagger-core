package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Yaml31 {

    private static final class ObjectMapperHolder {
        private static final ObjectMapper MAPPER = ObjectMapperFactory.createYaml31();
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(Yaml31.class);

    public static ObjectMapper mapper() {
        return ObjectMapperHolder.MAPPER;
    }

    public static ObjectWriter pretty() {
        return mapper().writer(new DefaultPrettyPrinter());
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
        } catch (JsonProcessingException e) {
            LOGGER.error("Exception converting jsonSchema to Map", e);
            return null;
        }
    }

    public static Map<String, Object> jsonSchemaAsMap(Schema schema) {
        try {
            return mapper().readValue(mapper().writeValueAsString(schema), Map.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Exception converting jsonSchema to Map", e);
            return null;
        }
    }
}
