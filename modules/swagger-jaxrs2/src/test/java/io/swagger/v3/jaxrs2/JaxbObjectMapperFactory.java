package io.swagger.v3.jaxrs2;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.module.jaxb.JaxbAnnotationModule;
import io.swagger.v3.core.util.ObjectMapperFactory;

public class JaxbObjectMapperFactory extends ObjectMapperFactory {

    public static ObjectMapper getMapper() {
        return ObjectMapperFactory.createJson(mapperBuilder ->
                mapperBuilder.addModule(new JaxbAnnotationModule()));
    }
}
