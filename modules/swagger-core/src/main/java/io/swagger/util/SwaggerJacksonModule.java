package io.swagger.util;

import io.swagger.models.Model;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class SwaggerJacksonModule extends SimpleModule{
    private static final long serialVersionUID = 1L;

    public SwaggerJacksonModule() {
        super("swagger-jackson-module");

        addDeserializer(Property.class, new PropertyDeserializer());
        addDeserializer(Model.class, new ModelDeserializer());
        addDeserializer(Parameter.class, new ParameterDeserializer());
        addDeserializer(SecuritySchemeDefinition.class, new SecurityDefinitionDeserializer());
    }
}
