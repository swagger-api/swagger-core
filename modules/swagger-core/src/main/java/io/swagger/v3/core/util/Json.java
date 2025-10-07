package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Json {

    private static final class ObjectMapperHolder {
        private static final ObjectMapper MAPPER = ObjectMapperFactory.createJson();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Json.class);

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
            LOGGER.error("Error serializing object to JSON", e);
            return null;
        }
    }

    public static void prettyPrint(Object o) {
        try {
            LOGGER.debug(pretty().writeValueAsString(o).replace("\r", ""));
        } catch (Exception e) {
            LOGGER.error("Error pretty-printing JSON", e);
        }
    }
}
