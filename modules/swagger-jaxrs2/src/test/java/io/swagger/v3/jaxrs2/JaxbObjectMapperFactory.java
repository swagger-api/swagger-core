package io.swagger.v3.jaxrs2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import io.swagger.v3.core.util.ObjectMapperFactory;

public class JaxbObjectMapperFactory extends ObjectMapperFactory {

    public static ObjectMapper getMapper() {
        ObjectMapper mapper = ObjectMapperFactory.createJson();
        mapper.registerModule(new JaxbAnnotationModule());
        return mapper;
    }
}
