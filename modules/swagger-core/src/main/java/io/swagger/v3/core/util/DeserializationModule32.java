package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;

public class DeserializationModule32 extends DeserializationModule31 {

    public DeserializationModule32() {

        this.addDeserializer(Schema.class, new Model32Deserializer());
        this.addDeserializer(Parameter.class, new Parameter32Deserializer());
        this.addDeserializer(SecurityScheme.class, new SecurityScheme32Deserializer());
        this.addDeserializer(ApiResponses.class, new ApiResponses32Deserializer());
        this.addDeserializer(Paths.class, new Paths32Deserializer());
        this.addDeserializer(Callback.class, new Callback32Deserializer());

        this.setDeserializerModifier(new BeanDeserializerModifier()
        {
            @Override public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (beanDesc.getBeanClass() == OpenAPI.class) {
                    return new OpenAPI32Deserializer(deserializer);
                }
                return deserializer;
            }
        });
    }
}
