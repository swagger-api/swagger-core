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

package io.swagger.v3.oas.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.Set;

public interface OpenApiContext {

    String OPENAPI_CONTEXT_ID_KEY = "openapi.context.id";
    String OPENAPI_CONTEXT_ID_PREFIX = OPENAPI_CONTEXT_ID_KEY + ".";
    String OPENAPI_CONTEXT_ID_DEFAULT = OPENAPI_CONTEXT_ID_PREFIX + "default";

    String getId();

    OpenApiContext init() throws OpenApiConfigurationException;

    OpenAPI read();

    OpenAPIConfiguration getOpenApiConfiguration();

    String getConfigLocation();

    OpenApiContext getParent();

    void setOpenApiScanner(OpenApiScanner openApiScanner);

    void setOpenApiReader(OpenApiReader openApiReader);

    /**
     * @since 2.0.6
     */
    void setObjectMapperProcessor(ObjectMapperProcessor objectMapperProcessor);

    /**
     * @since 2.0.6
     */
    void setModelConverters(Set<ModelConverter> modelConverters);


    /**
     * @since 2.1.6
     */
    ObjectMapper getOutputJsonMapper();

    /**
     * @since 2.1.6
     */
    ObjectMapper getOutputYamlMapper();


    /**
     * @since 2.1.6
     */
    void setOutputJsonMapper(ObjectMapper outputJsonMapper);

    /**
     * @since 2.1.6
     */
    void setOutputYamlMapper(ObjectMapper outputYamlMapper);

}
