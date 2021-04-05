package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenAPISchema2JsonSchema {

    protected final ObjectMapper converterMapper = Json31.converterMapper();

    public void process(Schema<?> schema) {
        Map<String, Object> jsonSchema = converterMapper.convertValue(schema, Map.class);

        // handle nullable
        if (Boolean.TRUE.equals(schema.getNullable())) {
            String type = jsonSchema.get("type").toString();
            List<String> newType = new ArrayList<>();
            if (StringUtils.isNotBlank(type)) {
                newType.add(type);
            }
            newType.add("null");
            jsonSchema.put("type", newType);
            jsonSchema.remove("nullable");
        }

        // TODO handle other differences

        schema.jsonSchema(jsonSchema);
    }
}
