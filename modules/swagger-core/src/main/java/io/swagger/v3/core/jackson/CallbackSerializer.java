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

        if (value != null && value.getExtensions() != null && !value.getExtensions().isEmpty()) {
            jgen.writeStartObject();

            if (StringUtils.isBlank(value.get$ref())) {
                if (!value.isEmpty()) {
                    for (String key: value.keySet()) {
                        jgen.writeObjectField(key , value.get(key));
                    }
                }
            } else { // handle ref schema serialization skipping all other props
                jgen.writeStringField("$ref", value.get$ref());
            }
            for (String ext: value.getExtensions().keySet()) {
                jgen.writeObjectField(ext , value.getExtensions().get(ext));
            }
            jgen.writeEndObject();
        } else {
            if (StringUtils.isBlank(value.get$ref())) {
                provider.defaultSerializeValue(value, jgen);
            } else { // handle ref schema serialization skipping all other props
                jgen.writeStartObject();
                jgen.writeStringField("$ref", value.get$ref());
                jgen.writeEndObject();
            }
        }
    }
}
