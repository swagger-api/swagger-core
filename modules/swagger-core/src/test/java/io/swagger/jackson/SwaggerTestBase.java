package io.swagger.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class SwaggerTestBase{
    static ObjectMapper mapper;

    public static ObjectMapper mapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return mapper;
    }

    protected ModelResolver modelResolver() {
        return new ModelResolver(new ObjectMapper());
    }

    protected void prettyPrint(Object o) {
        try {
            System.out.println(mapper().writer(new DefaultPrettyPrinter()).writeValueAsString(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
