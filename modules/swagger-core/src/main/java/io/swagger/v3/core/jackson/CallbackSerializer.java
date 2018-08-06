package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.v3.oas.models.callbacks.Callback;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class CallbackSerializer extends JsonSerializer<Callback> {

    public CallbackSerializer() {
    }

    @Override
    public void serialize(
            Callback value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        // handle ref schema serialization skipping all other props
        if (StringUtils.isBlank(value.get$ref())) {
            provider.defaultSerializeValue(value, jgen);
        } else {
            jgen.writeStartObject();
            jgen.writeStringField("$ref", value.get$ref());
            jgen.writeEndObject();
        }
    }
}
