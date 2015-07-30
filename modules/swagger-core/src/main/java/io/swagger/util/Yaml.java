package io.swagger.util;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Yaml {
    static ObjectMapper mapper;

    public static ObjectMapper mapper() {
        if(mapper == null) {
            mapper = ObjectMapperFactory.createYaml();
        }
        return mapper;
    }

    public static ObjectWriter pretty() {
        return mapper().writer(new DefaultPrettyPrinter());
    }

    public static void prettyPrint(Object o) {
        try {
            System.out.println(pretty().writeValueAsString(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
