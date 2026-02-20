package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.Schema;

import java.util.LinkedHashSet;
import java.util.Map;

public class OpenAPISchema2JsonSchema {

    protected final ObjectMapper converterMapper = Json31.converterMapper();

    public void process(Schema<?> schema) {
        schema.specVersion(SpecVersion.V31);
        Map<String, Object> jsonSchema = converterMapper.convertValue(schema, Map.class);

        // handle nullable
        if (schema.getType() != null || Boolean.TRUE.equals(schema.getNullable())) {
            schema.types(new LinkedHashSet<>());
        }
        if (schema.getType() != null) {
            schema.getTypes().add(schema.getType());
        }
        schema.type(null);
        if (Boolean.TRUE.equals(schema.getNullable())) {
            schema.nullable(null);
            schema.getTypes().add("null");
        }

        if (schema.getMinimum() != null && Boolean.TRUE.equals(schema.getExclusiveMinimum())) {
            schema.setExclusiveMinimumValue(schema.getMinimum());
        }
        if (schema.getMaximum() != null && Boolean.TRUE.equals(schema.getExclusiveMaximum())) {
            schema.setExclusiveMaximumValue(schema.getMaximum());
        }

        schema.jsonSchema(jsonSchema);

        if (schema.getAdditionalProperties() instanceof Schema) {
            process((Schema<?>) schema.getAdditionalProperties());
        }
        if (schema.getAllOf() != null) {
            schema.getAllOf().forEach(this::process);
        }
        if (schema.getAnyOf() != null) {
            schema.getAnyOf().forEach(this::process);
        }
        if (schema.getOneOf() != null) {
            schema.getOneOf().forEach(this::process);
        }
        if (schema.getProperties() != null) {
            schema.getProperties().values().forEach(this::process);
        }
        if (schema.getItems() != null) {
            process(schema.getItems());
        }
    }
}
