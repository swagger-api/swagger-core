package io.swagger.oas.web;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.swagger.oas.models.OpenAPI;

public final class BasicOpenAPIConfig implements OpenAPIConfig {
    private Set<Class<?>> resourceClasses;
    private Set<String> resourcePackages;
    private Map<String, Object> environment;
    private Class<?> filterClass;
    private Collection<String> ignoredRoutes;
    private OpenAPI openAPI;
    private boolean scanAllResources;
    private OpenApiReader readerClass;
    private OpenApiScanner scannerClass;
    private boolean prettyPrint;

    public BasicOpenAPIConfig setReaderClass(OpenApiReader readerClass) {
        this.readerClass = readerClass;
        return this;
    }

    public BasicOpenAPIConfig setScannerClass(OpenApiScanner scannerClass) {
        this.scannerClass = scannerClass;
        return this;
    }

    public BasicOpenAPIConfig setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }

    public BasicOpenAPIConfig() {
        resourceClasses = Collections.emptySet();
        resourcePackages = Collections.emptySet();
        environment = Collections.<String, Object>emptyMap();
        ignoredRoutes = Collections.<String>emptySet();
    }

    @Override
    public Set<String> getResourcePackages() {
        return resourcePackages;
    }

    public OpenAPIConfig setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages == null || resourcePackages.isEmpty()
                ? Collections.emptySet() : Collections.unmodifiableSet(resourcePackages);
        return this;
    }

    @Override
    public OpenApiReader getReaderClass() {
        return readerClass;
    }

    @Override
    public OpenApiScanner getScannerClass() {
        return scannerClass;
    }

    @Override
    public Set<Class<?>> getResourceClasses() {
        return resourceClasses;
    }


    public OpenAPIConfig setResourceClasses(Set<Class<?>> resourceClasses) {
        this.resourceClasses = resourceClasses == null || resourceClasses.isEmpty()
                ? Collections.<Class<?>>emptySet() : Collections.unmodifiableSet(resourceClasses);
        return this;
    }

    @Override
    public Class<?> getFilterClass() {
        return filterClass;
    }

    public OpenAPIConfig setFilterClass(Class<?> filterClass) {
        this.filterClass = filterClass;
        return this;
    }

    @Override
    public Collection<String> getIgnoredRoutes() {
        return ignoredRoutes;
    }

    public OpenAPIConfig setIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes == null || ignoredRoutes.isEmpty() ? Collections.<String>emptySet()
                : Collections.unmodifiableCollection(new HashSet<String>(ignoredRoutes));
        return this;
    }

    @Override
    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    public OpenAPIConfig setOpenAPI(OpenAPI openAPI) {
        this.openAPI = openAPI;
        return this;
    }

    @Override
    public Map<String, Object> getUserDefinedOptions() {
        return environment;
    }

    public OpenAPIConfig setUserDefinedOptions(Map<String, Object> environment) {
        this.environment = environment == null || environment.isEmpty() ? Collections.<String, Object>emptyMap()
                : Collections.unmodifiableMap(new HashMap<>(environment));
        return this;
    }

    @Override
    public boolean isScanAllResources() {
        return scanAllResources;
    }

    @Override
    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setScanAllResources(boolean scanAllResources) {
        this.scanAllResources = scanAllResources;
    }
}