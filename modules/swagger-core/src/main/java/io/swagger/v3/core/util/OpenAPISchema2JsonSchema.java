/**
 * Copyright 2021 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

        // TODO handle other differences

        schema.jsonSchema(jsonSchema);

        // TODO handle all nested schemas
        if (schema.getAdditionalProperties() instanceof Schema) {
            process((Schema<?>) schema.getAdditionalProperties());
        }
        if (schema.getAllOf() != null) {
            schema.getAllOf().forEach(this::process);
        }
        if (schema.getProperties() != null) {
            schema.getProperties().values().forEach(this::process);
        }
        if (schema.getItems() != null) {
            process(schema.getItems());
        }
    }
}
