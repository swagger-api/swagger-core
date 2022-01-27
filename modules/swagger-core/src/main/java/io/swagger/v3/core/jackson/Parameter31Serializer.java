package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map.Entry;

public class Parameter31Serializer extends JsonSerializer<Parameter> {

    @Override
    public void serialize(Parameter value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value != null && StringUtils.isNotBlank(value.get$ref())) {
            jgen.writeStartObject();
            jgen.writeStringField("$ref", value.get$ref());
            if (StringUtils.isNotBlank(value.getDescription())) {
                jgen.writeStringField("description", value.getDescription());
            }
            if (StringUtils.isNotBlank(value.getSummary())) {
                jgen.writeStringField("summary", value.getSummary());
            }
            jgen.writeEndObject();
        } else {
            provider.defaultSerializeValue(value, jgen);
        }
    }
}
