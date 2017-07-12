package io.swagger.oas.web;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.swagger.oas.models.OpenAPI;

public final class OpenAPIConfig {
    private Set<Class<?>> classesToBeScanned;
    private Map<String, Object> environment;
    private Class<?> filterClass;
    private Collection<String> ignoredRoutes;
    private OpenAPI openAPI;
    private boolean prettyPrint;
    private boolean scanAllResources;

    public OpenAPIConfig() {
        classesToBeScanned = Collections.<Class<?>>emptySet();
        environment = Collections.<String, Object>emptyMap();
        ignoredRoutes = Collections.<String>emptySet();
    }

    public Set<Class<?>> getClasses() {
        return classesToBeScanned;
    }

    public OpenAPIConfig setClasses(Set<Class<?>> classesToBeScanned) {
        this.classesToBeScanned = classesToBeScanned == null || classesToBeScanned.isEmpty()
                ? Collections.<Class<?>>emptySet() : Collections.unmodifiableSet(classesToBeScanned);
        return this;
    }

    public Class<?> getFilterClass() {
        return filterClass;
    }

    public OpenAPIConfig setFilterClass(Class<?> filterClass) {
        this.filterClass = filterClass;
        return this;
    }

    public Collection<String> getIgnoredRoutes() {
        return ignoredRoutes;
    }

    public OpenAPIConfig setIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes == null || ignoredRoutes.isEmpty() ? Collections.<String>emptySet()
                : Collections.unmodifiableCollection(new HashSet<String>(ignoredRoutes));
        return this;
    }

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    public OpenAPIConfig setOpenAPI(OpenAPI openAPI) {
        this.openAPI = openAPI;
        return this;
    }

    public Map<String, Object> getUserDefinedOptions() {
        return environment;
    }

    public OpenAPIConfig setUserDefinedOptions(Map<String, Object> environment) {
        this.environment = environment == null || environment.isEmpty() ? Collections.<String, Object>emptyMap()
                : Collections.unmodifiableMap(new HashMap<>(environment));
        return this;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public OpenAPIConfig setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }

    public boolean isScanAllResources() {
        return scanAllResources;
    }

    public void setScanAllResources(boolean scanAllResources) {
        this.scanAllResources = scanAllResources;
    }
}