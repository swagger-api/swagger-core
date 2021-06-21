package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class OpenAPISchema2JsonSchema {

    protected final ObjectMapper converterMapper = Json31.converterMapper();

    public void process(Schema<?> schema) {
        if (schema.getSpecVersion() == SpecVersion.V30) {
            return;
        }
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

        // TODO handle other differences

        schema.jsonSchema(jsonSchema);
    }
}
