package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.EncodingProperty;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;

public class DeserializationModule31 extends SimpleModule {

    public DeserializationModule31() {

        this.addDeserializer(Schema.class, new Model31Deserializer());
        this.addDeserializer(Parameter.class, new Parameter31Deserializer());
        this.addDeserializer(Header.StyleEnum.class, new HeaderStyleEnumDeserializer());
        this.addDeserializer(Encoding.StyleEnum.class, new EncodingStyleEnumDeserializer());
        this.addDeserializer(EncodingProperty.StyleEnum.class, new EncodingPropertyStyleEnumDeserializer());

        this.addDeserializer(SecurityScheme.class, new SecurityScheme31Deserializer());

        this.addDeserializer(ApiResponses.class, new ApiResponses31Deserializer());
        this.addDeserializer(Paths.class, new Paths31Deserializer());
        this.addDeserializer(Callback.class, new Callback31Deserializer());

        this.setDeserializerModifier(new BeanDeserializerModifier()
        {
            @Override public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (beanDesc.getBeanClass() == OpenAPI.class) {
                    return new OpenAPI31Deserializer(deserializer);
                }
                return deserializer;
            }
        });
    }
}
