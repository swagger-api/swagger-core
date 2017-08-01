package io.swagger.oas.integration;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.web.OpenAPIConfig;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OpenApiConfiguration implements OpenAPIConfig {

    Map<String, Object> userDefinedOptions = new ConcurrentHashMap<>();
    private OpenAPI openApi = new OpenAPI();

    private String id;
    private Set<String> resourcePackages;
    private Set<String> resourceClasses;
    private String filterClass;
    private String readerClass;
    private String scannerClass;

    private Boolean prettyPrint;
    private Boolean scanAllResources;
    private Collection<String> ignoredRoutes = Collections.emptySet();
    private Long cacheTTL = -1L;

    public Long getCacheTTL() {
        return cacheTTL;
    }

    public void setCacheTTL(Long cacheTTL) {
        this.cacheTTL = cacheTTL;
    }

    public OpenApiConfiguration cacheTTL(Long cacheTTL) {
        this.cacheTTL = cacheTTL;
        return this;
    }

    public Boolean isScanAllResources() {
        return scanAllResources;
    }

    public void setScanAllResources(Boolean scanAllResources) {
        this.scanAllResources = scanAllResources;
    }

    public OpenApiConfiguration scanAllResources(Boolean scanAllResources) {
        this.scanAllResources = scanAllResources;
        return this;
    }

    public Collection<String> getIgnoredRoutes() {
        return ignoredRoutes;
    }

    public void setIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes == null || ignoredRoutes.isEmpty() ? Collections.<String>emptySet()
                : Collections.unmodifiableCollection(new HashSet<String>(ignoredRoutes));
    }

    public OpenApiConfiguration ignoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes == null || ignoredRoutes.isEmpty() ? Collections.<String>emptySet()
                : Collections.unmodifiableCollection(new HashSet<String>(ignoredRoutes));
        return this;
    }

    public Boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public OpenApiConfiguration prettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }

    @Override
    public OpenAPI getOpenAPI() {
        return openApi;
    }


    public void setOpenApi (OpenAPI openApi) {
        this.openApi = openApi;
    }
    public OpenApiConfiguration openApi (OpenAPI openApi) {
        this.openApi = openApi;
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

    public OpenApiConfiguration scannerClass(String scannerClass) {
        this.scannerClass = scannerClass;
        return this;
    }

    public OpenApiConfiguration readerClass(String readerClass) {
        this.readerClass = readerClass;
        return this;
    }

    public OpenApiConfiguration userDefinedOptions(Map<String, Object> userDefinedOptions) {
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

    public OpenApiConfiguration resourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
        return this;
    }

    public Set<String> getResourceClasses() {
        return resourceClasses;
    }

    public void setResourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
    }

    public OpenApiConfiguration resourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
        return this;
    }


    public String getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    public OpenApiConfiguration filterClass(String filterClass) {
        this.filterClass = filterClass;
        return this;
    }

    public OpenApiConfiguration id(String id) {
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
