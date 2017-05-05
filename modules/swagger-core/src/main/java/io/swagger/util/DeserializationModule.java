package io.swagger.util;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.models.PathItem;
import io.swagger.models.media.Schema;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.responses.Response;


public class DeserializationModule extends SimpleModule {

    public DeserializationModule(boolean includePathDeserializer,
                                 boolean includeResponseDeserializer) {

        if (includePathDeserializer) {
            this.addDeserializer(PathItem.class, new PathDeserializer());
        }
        if (includeResponseDeserializer) {
            this.addDeserializer(Response.class, new ResponseDeserializer());
        }

        this.addDeserializer(Schema.class, new ModelDeserializer());
        this.addDeserializer(Parameter.class, new ParameterDeserializer());
//        this.addDeserializer(RequestBody.class, new RequestBodyDeserializer());

        // TODO
//        this.addDeserializer(SecurityDefinition.class, new SecurityDefinitionDeserializer());
    }

    public DeserializationModule() {
        this(true, true);
    }
}
