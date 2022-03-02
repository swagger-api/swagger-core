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

package io.swagger.v3.oas.integration;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface StringOpenApiConfigurationLoader extends OpenApiConfigurationLoader {

    Logger LOGGER = LoggerFactory.getLogger(StringOpenApiConfigurationLoader.class);

    default OpenAPIConfiguration deserializeConfig(String path, String configAsString) {

        try {
            if (path.toLowerCase().endsWith("json")) {
                return Json.mapper().readValue(configAsString, SwaggerConfiguration.class);
            } else { // assume yaml
                return Yaml.mapper().readValue(configAsString, SwaggerConfiguration.class);
            }

        } catch (Exception e) {
            LOGGER.error("exception reading config: " + e.getMessage(), e);
            return null;
        }

    }

}
