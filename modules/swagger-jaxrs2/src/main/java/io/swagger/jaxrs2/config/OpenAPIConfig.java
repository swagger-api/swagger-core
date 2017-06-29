package io.swagger.jaxrs2.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.swagger.oas.models.OpenAPI;

public final class OpenAPIConfig {

    private OpenAPI openAPI;
    private Set<Class<?>> classesToBeScanned = Collections.<Class<?>>emptySet();
    private Class<?> filterClass;
    private boolean prettyPrint;
    private Collection<String> ignoredRoutes = Collections.<String>emptySet();

    public OpenAPIConfig() {
    }

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    public OpenAPIConfig setOpenAPI(OpenAPI openAPI) {
        this.openAPI = openAPI;
        return this;
    }

    public Set<Class<?>> getClasses() {
        return classesToBeScanned;
    }

    public OpenAPIConfig setClasses(Set<Class<?>> classesToBeScanned) {
        this.classesToBeScanned = classesToBeScanned == null || classesToBeScanned.isEmpty()
                ? Collections.<Class<?>>emptySet()
                : Collections.unmodifiableSet(classesToBeScanned);
        return this;
    }

    public Class<?> getFilterClass() {
        return filterClass;
    }

    public OpenAPIConfig setFilterClass(Class<?> filterClass) {
        this.filterClass = filterClass;
        return this;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public OpenAPIConfig setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }

    public Collection<String> getIgnoredRoutes() {
        return ignoredRoutes;
    }

    public OpenAPIConfig setIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes == null || ignoredRoutes.isEmpty() 
                ? Collections.<String>emptySet()
                : Collections.unmodifiableCollection(new HashSet<String>(ignoredRoutes));
        return this;
    }
}