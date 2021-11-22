package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Yaml {
    static ObjectMapper mapper;

    public static ObjectMapper mapper() {
        if (mapper == null) {
            mapper = ObjectMapperFactory.createYaml();
        }
        return mapper;
    }

    /**
     * Cause Yaml.mapper() to be recreated. If {@link ObjectMapperFactory} state has been updated then the new mapper
     * will reflect those changes.
     * @since 2.1.12
     */
    public static void recreateMapper() {
        mapper = ObjectMapperFactory.createYaml();
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
            System.out.println(pretty().writeValueAsString(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
