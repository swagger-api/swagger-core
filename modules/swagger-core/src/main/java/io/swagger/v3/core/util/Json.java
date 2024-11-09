package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Json {

    private static final class ObjectMapperHolder {
        private static final ObjectMapper MAPPER = ObjectMapperFactory.createJson();
    }

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
            e.printStackTrace();
            return null;
        }
    }

    public static void prettyPrint(Object o) {
        try {
            System.out.println(pretty().writeValueAsString(o).replace("\r", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
