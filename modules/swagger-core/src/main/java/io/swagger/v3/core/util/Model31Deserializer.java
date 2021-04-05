package io.swagger.v3.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.media.Schema;

import java.io.IOException;
import java.util.Map;

public class Model31Deserializer extends JsonDeserializer<Schema> {
    @Override
    public Schema deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Schema schema = new Schema();
        Map<String, Object> jsonSchema = Json31.mapper().convertValue(node, Map.class);
        schema.jsonSchema(jsonSchema);
        return schema;
    }
}
