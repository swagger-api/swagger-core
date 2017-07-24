package io.swagger.util;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.responses.ApiResponse;


public class DeserializationModule extends SimpleModule {

    public DeserializationModule(boolean includePathDeserializer,
                                 boolean includeResponseDeserializer) {

        if (includePathDeserializer) {
            this.addDeserializer(PathItem.class, new PathDeserializer());
        }
        if (includeResponseDeserializer) {
            this.addDeserializer(ApiResponse.class, new ResponseDeserializer());
        }

        this.addDeserializer(Schema.class, new ModelDeserializer());
        this.addDeserializer(Parameter.class, new ParameterDeserializer());
//        this.addDeserializer(RequestBody.class, new RequestBodyDeserializer());

        // TODO #2312
//        this.addDeserializer(SecurityDefinition.class, new SecurityDefinitionDeserializer());
    }

    public DeserializationModule() {
        this(true, true);
    }
}
