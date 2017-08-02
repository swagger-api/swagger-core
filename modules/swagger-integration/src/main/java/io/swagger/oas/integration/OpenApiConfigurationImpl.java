package io.swagger.oas.integration;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.integration.api.OpenAPIConfiguration;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class OpenApiConfigurationImpl implements OpenAPIConfiguration {

    Map<String, Object> userDefinedOptions;
    private OpenAPI openAPI;

    private String id;
    private Set<String> resourcePackages;
    private Set<String> resourceClasses;
    private String filterClass;
    private String readerClass;
    private String scannerClass;

    private Boolean prettyPrint;
    private Boolean scanAllResources;
    private Collection<String> ignoredRoutes;
    private Long cacheTTL = -1L;

    public Long getCacheTTL() {
        return cacheTTL;
    }

    public void setCacheTTL(Long cacheTTL) {
        this.cacheTTL = cacheTTL;
    }

    public OpenApiConfigurationImpl cacheTTL(Long cacheTTL) {
        this.cacheTTL = cacheTTL;
        return this;
    }

    public Boolean isScanAllResources() {
        return scanAllResources;
    }

    public void setScanAllResources(Boolean scanAllResources) {
        this.scanAllResources = scanAllResources;
    }

    public OpenApiConfigurationImpl scanAllResources(Boolean scanAllResources) {
        this.scanAllResources = scanAllResources;
        return this;
    }

    public Collection<String> getIgnoredRoutes() {
        return ignoredRoutes;
    }

    public void setIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes;
    }

    public OpenApiConfigurationImpl ignoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes;
        return this;
    }

    public Boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public OpenApiConfigurationImpl prettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }

    @Override
    public OpenAPI getOpenAPI() {
        return openAPI;
    }


    public void setOpenAPI (OpenAPI openAPI) {
        this.openAPI = openAPI;
    }
    public OpenApiConfigurationImpl openAPI(OpenAPI openAPI) {
        this.openAPI = openAPI;
        return this;
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

    public Map<String, Object> getUserDefinedOptions() {
        return userDefinedOptions;
    }

    public void setUserDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
    }

    public OpenApiConfigurationImpl scannerClass(String scannerClass) {
        this.scannerClass = scannerClass;
        return this;
    }

    public OpenApiConfigurationImpl readerClass(String readerClass) {
        this.readerClass = readerClass;
        return this;
    }

    public OpenApiConfigurationImpl userDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
        return this;
    }

    @Override
    public Set<String> getResourcePackages() {
        return resourcePackages;
    }

    public void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
    }

    public OpenApiConfigurationImpl resourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
        return this;
    }

    public Set<String> getResourceClasses() {
        return resourceClasses;
    }

    public void setResourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
    }

    public OpenApiConfigurationImpl resourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
        return this;
    }


    public String getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    public OpenApiConfigurationImpl filterClass(String filterClass) {
        this.filterClass = filterClass;
        return this;
    }

    public OpenApiConfigurationImpl id(String id) {
        this.id = id;
        return this;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
