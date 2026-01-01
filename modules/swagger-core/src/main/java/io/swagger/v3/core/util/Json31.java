package io.swagger.v3.core.util;

import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.cfg.MapperBuilder;

import java.util.Map;
import java.util.function.Consumer;

public class Json31 {

    private static final class ObjectMapperHolder {
        private static final ObjectMapper MAPPER = ObjectMapperFactory.createJson31();
    }

    private static final class ConverterMapperHolder {
        private static final ObjectMapper MAPPER = ObjectMapperFactory.createJsonConverter();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Json31.class);

    public static ObjectMapper mapper() {
        return ObjectMapperHolder.MAPPER;
    }

    public static ObjectMapper mapper(Consumer<MapperBuilder<? extends ObjectMapper, ? extends MapperBuilder<?, ?>>> mapperBuilderCustomizer) {
        return ObjectMapperFactory.createJson31(mapperBuilderCustomizer);
    }

    public static ObjectMapper converterMapper() {
        return ConverterMapperHolder.MAPPER;
    }

    public static ObjectWriter pretty() {
        return mapper().writerWithDefaultPrettyPrinter();
    }

    public static String pretty(Object o) {
        try {
            return pretty().writeValueAsString(o);
        } catch (Exception e) {
            PrettyPrintHelper.emitError(LOGGER, "Error serializing object to JSON (3.1)", e);
            return null;
        }
    }

    public static void prettyPrint(Object o) {
        try {
            String prettyString = pretty().writeValueAsString(o).replace("\r", "");
            PrettyPrintHelper.emit(LOGGER, prettyString);
        } catch (Exception e) {
            PrettyPrintHelper.emitError(LOGGER, "Error pretty-printing JSON (3.1)", e);
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

    public static Map<String, Object> jsonSchemaAsMap(JsonNode schema) {
        try {
            return mapper().readValue(mapper().writeValueAsString(schema), Map.class);
        } catch (JacksonException e) {
            LOGGER.error("Exception converting jsonSchema to Map", e);
            return null;
        }
    }
}
