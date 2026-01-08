package io.swagger.v3.core.jackson;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;

import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;
import org.apache.commons.lang3.StringUtils;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.io.IOException;
import java.util.Map.Entry;

public class CallbackSerializer extends ValueSerializer<Callback> {

    @Override
    public void serialize(
            Callback value, JsonGenerator jgen, SerializationContext provider)
            throws JacksonException {

        // has extensions
        if (value != null && value.getExtensions() != null && !value.getExtensions().isEmpty()) {
            jgen.writeStartObject();

            // not a ref
            if (StringUtils.isBlank(value.get$ref())) {
                if (!value.isEmpty()) {
                    // write map
                    for (Entry<String, PathItem> entry: value.entrySet()) {
                        jgen.writePOJOProperty(entry.getKey() , entry.getValue());
                    }
                }
            } else { // handle ref schema serialization skipping all other props ...
                jgen.writeStringProperty("$ref", value.get$ref());
            }
            for (String ext: value.getExtensions().keySet()) {
                jgen.writePOJOProperty(ext , value.getExtensions().get(ext));
            }
            jgen.writeEndObject();
        } else {
            if (value == null || StringUtils.isBlank(value.get$ref())) {
                jgen.writePOJO(value);
            } else { // handle ref schema serialization skipping all other props
                jgen.writeStartObject();
                jgen.writeStringProperty("$ref", value.get$ref());
                jgen.writeEndObject();
            }
        }
    }
}
