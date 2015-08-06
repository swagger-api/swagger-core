package io.swagger.util;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.models.Model;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;


public class DeserializationModule extends SimpleModule {

    public DeserializationModule(boolean includePathDeserializer,
                                 boolean includeResponseDeserializer) {

        if (includePathDeserializer) {
            this.addDeserializer(Path.class, new PathDeserializer());
        }
        if (includeResponseDeserializer) {
            this.addDeserializer(Response.class, new ResponseDeserializer());
        }

        this.addDeserializer(Property.class, new PropertyDeserializer());
        this.addDeserializer(Model.class, new ModelDeserializer());
        this.addDeserializer(Parameter.class, new ParameterDeserializer());
        this.addDeserializer(SecuritySchemeDefinition.class, new SecurityDefinitionDeserializer());
    }

    public DeserializationModule() {
        this(true, true);
    }
}
