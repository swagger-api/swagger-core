package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map.Entry;

public class CallbackSerializer extends JsonSerializer<Callback> {

    @Override
    public void serialize(
            Callback value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        // has extensions
        if (value != null && value.getExtensions() != null && !value.getExtensions().isEmpty()) {
            jgen.writeStartObject();

            // not a ref
            if (StringUtils.isBlank(value.get$ref())) {
                if (!value.isEmpty()) {
                    // write map
                    for (Entry<String, PathItem> entry: value.entrySet()) {
                        jgen.writeObjectField(entry.getKey() , entry.getValue());
                    }
                }
            } else { // handle ref schema serialization skipping all other props ...
                jgen.writeStringField("$ref", value.get$ref());
            }
            for (String ext: value.getExtensions().keySet()) {
                jgen.writeObjectField(ext , value.getExtensions().get(ext));
            }
            jgen.writeEndObject();
        } else {
            if (value == null || StringUtils.isBlank(value.get$ref())) {
                provider.defaultSerializeValue(value, jgen);
            } else { // handle ref schema serialization skipping all other props
                jgen.writeStartObject();
                jgen.writeStringField("$ref", value.get$ref());
                jgen.writeEndObject();
            }
        }
    }
}
