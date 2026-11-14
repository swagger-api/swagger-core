package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.MediaType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class MediaTypeSerializer extends JsonSerializer<MediaType> implements ResolvableSerializer {

    private JsonSerializer<Object> defaultSerializer;
    private SpecVersion specVersion;

    public MediaTypeSerializer(JsonSerializer<Object> serializer) {
        this(serializer, SpecVersion.V30);
    }

    public MediaTypeSerializer(JsonSerializer<Object> serializer, SpecVersion specVersion) {
        defaultSerializer = serializer;
        this.specVersion = specVersion;
    }

    @Override
    public void resolve(SerializerProvider serializerProvider) throws JsonMappingException {
        if (defaultSerializer instanceof ResolvableSerializer) {
            ((ResolvableSerializer) defaultSerializer).resolve(serializerProvider);
        }
    }

    @Override
    public void serialize(
            MediaType value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        if (SpecVersion.V32.equals(specVersion) && StringUtils.isNotBlank(value.get$ref())) {
            // a $ref value in a content/mediaTypes map is a Reference Object:
            // sibling media type fields are ignored per spec
            jgen.writeStartObject();
            jgen.writeStringField("$ref", value.get$ref());
            jgen.writeEndObject();
            return;
        }
        if (value.getExampleSetFlag() && value.getExample() == null) {
            jgen.writeStartObject();
            defaultSerializer.unwrappingSerializer(null).serialize(value, jgen, provider);
            jgen.writeNullField("example");
            jgen.writeEndObject();
        } else {
            defaultSerializer.serialize(value, jgen, provider);
        }
    }
}
