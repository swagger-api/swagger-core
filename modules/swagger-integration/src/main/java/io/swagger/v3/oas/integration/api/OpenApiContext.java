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
