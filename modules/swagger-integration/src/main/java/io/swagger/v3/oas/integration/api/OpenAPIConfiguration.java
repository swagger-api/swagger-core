package io.swagger.v3.oas.integration.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface OpenAPIConfiguration {
    Set<String> getResourcePackages();

    Set<String> getResourceClasses();

    String getReaderClass();

    String getScannerClass();

    String getFilterClass();

    Collection<String> getIgnoredRoutes();

    OpenAPI getOpenAPI();

    Map<String, Object> getUserDefinedOptions();

    Boolean isReadAllResources();

    Boolean isPrettyPrint();

    Long getCacheTTL();

    /**
     * @since 2.0.6
     */
    public String getObjectMapperProcessorClass();

    /**
     * @since 2.0.6
     */
    public Set<String> getModelConverterClasses();

    /**
     * @since 2.1.6
     */
    Boolean isSortOutput();

    /**
     * @since 2.1.9
     */
    Boolean isAlwaysResolveAppPath();

    /**
     * @since 2.1.15
     */
    Boolean isSkipResolveAppPath();

    /**
     * @since 2.2.12
     */
    Boolean isOpenAPI31();

    /**
     * @since 2.2.12
     */
    Boolean isConvertToOpenAPI31();

    /**
     * @since 2.2.17
     */
    public String getDefaultResponseCode();

    /**
     * @since 2.2.24
     */
    public Schema.SchemaResolution getSchemaResolution();
}
