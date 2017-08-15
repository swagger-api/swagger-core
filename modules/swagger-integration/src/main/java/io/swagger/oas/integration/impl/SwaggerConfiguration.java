package io.swagger.oas.integration.impl;

import io.swagger.oas.integration.OpenAPIConfiguration;
import io.swagger.oas.models.OpenAPI;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SwaggerConfiguration implements OpenAPIConfiguration {

    Map<String, Object> userDefinedOptions;
    private OpenAPI openAPI;

    private String id;
    private Set<String> resourcePackages;
    private Set<String> resourceClasses;
    private String filterClass;
    private String readerClass;
    private String scannerClass;

    private Boolean readAllResources;
    private Collection<String> ignoredRoutes;

    public Boolean isReadAllResources() {
        return readAllResources;
    }

    public void setReadAllResources(Boolean readAllResources) {
        this.readAllResources = readAllResources;
    }

    public SwaggerConfiguration readAllResources(Boolean readAllResources) {
        this.readAllResources = readAllResources;
        return this;
    }

    public Collection<String> getIgnoredRoutes() {
        return ignoredRoutes;
    }

    public void setIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes;
    }

    public SwaggerConfiguration ignoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes;
        return this;
    }

    @Override
    public OpenAPI getOpenAPI() {
        return openAPI;
    }


    public void setOpenAPI (OpenAPI openAPI) {
        this.openAPI = openAPI;
    }
    public SwaggerConfiguration openAPI(OpenAPI openAPI) {
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

    public SwaggerConfiguration scannerClass(String scannerClass) {
        this.scannerClass = scannerClass;
        return this;
    }

    public SwaggerConfiguration readerClass(String readerClass) {
        this.readerClass = readerClass;
        return this;
    }

    public SwaggerConfiguration userDefinedOptions(Map<String, Object> userDefinedOptions) {
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

    public SwaggerConfiguration resourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
        return this;
    }

    public Set<String> getResourceClasses() {
        return resourceClasses;
    }

    public void setResourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
    }

    public SwaggerConfiguration resourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
        return this;
    }


    public String getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    public SwaggerConfiguration filterClass(String filterClass) {
        this.filterClass = filterClass;
        return this;
    }

    public SwaggerConfiguration id(String id) {
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
