package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SwaggerLoader {

    private String outputFormat;

    private String resourcePackages;
    private String resourceClasses;
    private String filterClass;
    private String readerClass;
    private String scannerClass;
    private Boolean prettyPrint = false;
    private Boolean readAllResources = Boolean.TRUE;
    private String ignoredRoutes;

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

        SwaggerConfiguration config = new SwaggerConfiguration()
                .filterClass(filterClass)
                .ignoredRoutes(ignoredRoutesSet)
                .prettyPrint(prettyPrint)
                .readAllResources(readAllResources)
                .readerClass(readerClass)
                .scannerClass(scannerClass)
                .resourceClasses(resourceClassesSet)
                .resourcePackages(resourcePackagesSet);
        try {

            OpenAPI openAPI = new JaxrsOpenApiContextBuilder()
                .openApiConfiguration(config)
                .buildContext(true)
                .read();
            String openapiJson = null;
            String openapiYaml = null;
            if ("JSON".equals(outputFormat) || "JSONANDYAML".equals(outputFormat)) {
                if (prettyPrint) {
                    openapiJson = Json.pretty(openAPI);
                } else {
                    openapiJson = Json.mapper().writeValueAsString(openAPI);
                }
            }
            if ("YAML".equals(outputFormat) || "JSONANDYAML".equals(outputFormat)) {
                if (prettyPrint) {
                    openapiYaml = Yaml.pretty(openAPI);
                } else {
                    openapiYaml = Yaml.mapper().writeValueAsString(openAPI);
                }

            }
            HashMap<String, String> map = new HashMap();
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
