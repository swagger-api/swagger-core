package io.swagger.v3.jaxrs2.integration;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import io.swagger.v3.core.filter.OpenAPISpecFilter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SwaggerLoader {

    private String outputFormat;

    private String resourcePackages;
    private String resourceClasses;
    private String filterClass;
    private String readerClass;
    private String contextId;
    private String scannerClass;
    private Boolean prettyPrint = false;
    private Boolean readAllResources = Boolean.TRUE;
    private String ignoredRoutes;

    private String openapiAsString;

    private String objectMapperProcessorClass;
    private String modelConverterClasses;

    private Boolean sortOutput = false;

    private Boolean alwaysResolveAppPath = false;

    private Boolean openAPI31 = false;

    /**
     * @since 2.0.6
     */
    public String getObjectMapperProcessorClass() {
        return objectMapperProcessorClass;
    }

    /**
     * @since 2.0.6
     */
    public void setObjectMapperProcessorClass(String objectMapperProcessorClass) {
        this.objectMapperProcessorClass = objectMapperProcessorClass;
    }

    /**
     * @since 2.0.6
     */
    public String getModelConverterClasses() {
        return modelConverterClasses;
    }

    /**
     * @since 2.0.6
     */
    public void setModelConverterClasses(String modelConverterClasses) {
        this.modelConverterClasses = modelConverterClasses;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getResourcePackages() {
        return resourcePackages;
    }

    public void setResourcePackages(String resourcePackages) {
        this.resourcePackages = resourcePackages;
    }

    public String getResourceClasses() {
        return resourceClasses;
    }

    public void setResourceClasses(String resourceClasses) {
        this.resourceClasses = resourceClasses;
    }

    public String getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    /**
     * @since 2.0.6
     */
    public String getContextId() {
        return contextId;
    }

    /**
     * @since 2.0.6
     */
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }


    public String getReaderClass() {
        return readerClass;
    }

    public void setReaderClass(String readerClass) {
        this.readerClass = readerClass;
    }

    public String getScannerClass() {
        return scannerClass;
    }

    public void setScannerClass(String scannerClass) {
        this.scannerClass = scannerClass;
    }

    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public Boolean getReadAllResources() {
        return readAllResources;
    }

    public void setReadAllResources(Boolean readAllResources) {
        this.readAllResources = readAllResources;
    }

    public String getIgnoredRoutes() {
        return ignoredRoutes;
    }

    public void setIgnoredRoutes(String ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes;
    }

    public String getOpenapiAsString() {
        return openapiAsString;
    }

    public void setOpenapiAsString(String openapiAsString) {
        this.openapiAsString = openapiAsString;
    }

    /**
     * @since 2.1.6
     */
    public Boolean getSortOutput() {
        return sortOutput;
    }

    /**
     * @since 2.1.6
     */
    public void setSortOutput(Boolean sortOutput) {
        this.sortOutput = sortOutput;
    }

    /**
     * @since 2.1.9
     */
    public Boolean getAlwaysResolveAppPath() {
        return alwaysResolveAppPath;
    }

    /**
     * @since 2.1.9
     */
    public void setAlwaysResolveAppPath(Boolean alwaysResolveAppPath) {
        this.alwaysResolveAppPath = alwaysResolveAppPath;
    }

    public Boolean getOpenAPI31() {
        return openAPI31;
    }

    /**
     *  @since 2.2.0
     */
    public void setOpenAPI31(Boolean openAPI31) {
        this.openAPI31 = openAPI31;
    }

    public Map<String, String> resolve() throws Exception{

        Set<String> ignoredRoutesSet = null;
        if (StringUtils.isNotBlank(ignoredRoutes)) {
            ignoredRoutesSet = new HashSet<>(Arrays.asList(ignoredRoutes.split(",")));
        }
        Set<String> resourceClassesSet = null;
        if (StringUtils.isNotBlank(resourceClasses)) {
            resourceClassesSet = new HashSet<>(Arrays.asList(resourceClasses.split(",")));
        }
        Set<String> resourcePackagesSet = null;
        if (StringUtils.isNotBlank(resourcePackages)) {
            resourcePackagesSet = new HashSet<>(Arrays.asList(resourcePackages.split(",")));
        }

        LinkedHashSet<String> modelConverterSet = null;
        if (StringUtils.isNotBlank(modelConverterClasses)) {
            modelConverterSet = new LinkedHashSet<>(Arrays.asList(modelConverterClasses.split(",")));
        }

        OpenAPI openAPIInput = null;
        if (StringUtils.isNotBlank(openapiAsString)) {
            try {
                openAPIInput = Json.mapper().readValue(openapiAsString, OpenAPI.class);
            } catch (Exception e) {
                try {
                    openAPIInput = Yaml.mapper().readValue(openapiAsString, OpenAPI.class);
                } catch (Exception e1) {
                    throw new Exception("Error reading/deserializing openapi input: " + e.getMessage(), e);
                }
            }
        }

        SwaggerConfiguration config = new SwaggerConfiguration()
                .filterClass(filterClass)
                .ignoredRoutes(ignoredRoutesSet)
                .prettyPrint(prettyPrint)
                .readAllResources(readAllResources)
                .openAPI(openAPIInput)
                .readerClass(readerClass)
                .scannerClass(scannerClass)
                .resourceClasses(resourceClassesSet)
                .resourcePackages(resourcePackagesSet)
                .objectMapperProcessorClass(objectMapperProcessorClass)
                .modelConverterClasses(modelConverterSet)
                .sortOutput(sortOutput)
                .alwaysResolveAppPath(alwaysResolveAppPath)
                .openAPI31(openAPI31);
        try {
            GenericOpenApiContextBuilder builder = new JaxrsOpenApiContextBuilder()
                    .openApiConfiguration(config);
            if (StringUtils.isNotBlank(contextId)) {
                builder.ctxId(contextId);
            }

            OpenApiContext context = builder.buildContext(true);
            OpenAPI openAPI = context.read();
            if (StringUtils.isNotBlank(filterClass)) {
                try {
                    OpenAPISpecFilter filterImpl = (OpenAPISpecFilter) this.getClass().getClassLoader().loadClass(filterClass).newInstance();
                    SpecFilter f = new SpecFilter();
                    openAPI = f.filter(openAPI, filterImpl, new HashMap<>(), new HashMap<>(),
                            new HashMap<>());
                } catch (Exception e) {
                    throw new Exception("Error applying filter to API specification: " + e.getMessage(), e);
                }
            }

            String openapiJson = null;
            String openapiYaml = null;
            if ("JSON".equals(outputFormat) || "JSONANDYAML".equals(outputFormat)) {
                if (prettyPrint != null && prettyPrint) {
                    openapiJson = context.getOutputJsonMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(openAPI);
                } else {
                    openapiJson = context.getOutputJsonMapper().writeValueAsString(openAPI);
                }
            }
            if ("YAML".equals(outputFormat) || "JSONANDYAML".equals(outputFormat)) {
                if (prettyPrint != null && prettyPrint) {
                    openapiYaml = context.getOutputYamlMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(openAPI);
                } else {
                    openapiYaml = context.getOutputYamlMapper().writeValueAsString(openAPI);
                }
            }
            Map<String, String> map = new HashMap<>();
            map.put("JSON", openapiJson);
            map.put("YAML", openapiYaml);
            return map;
        } catch (OpenApiConfigurationException e) {
                throw new Exception("Error resolving API specification: " + e.getMessage(), e);
        } catch (Exception e) {
                throw new Exception("Error resolving API specification: " + e.getMessage(), e);
        }

    }
}
