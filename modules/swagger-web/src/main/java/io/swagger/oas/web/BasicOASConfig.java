package io.swagger.oas.web;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.swagger.oas.models.OpenAPI;

public final class BasicOASConfig implements OASConfig {
    private Class<?> filterClass;
    private Set<String> ignoredClasses;
    private OpenAPI openAPI;
    private Map<String, Object> options;
    private Set<String> resourcePackages;
    private boolean scanDisabled;

    public BasicOASConfig() {
        ignoredClasses = Collections.<String>emptySet();
        resourcePackages = Collections.<String>emptySet();
        options = Collections.<String, Object>emptyMap();
    }

    @Override
    public Class<?> getFilterClass() {
        return filterClass;
    }

    public BasicOASConfig setFilterClass(Class<?> filterClass) {
        this.filterClass = filterClass;
        return this;
    }

    @Override
    public Set<String> getIgnoredClasses() {
        return ignoredClasses;
    }

    public BasicOASConfig setIgnoredClasses(Set<String> ignoredClasses) {
        this.ignoredClasses = ignoredClasses == null || ignoredClasses.isEmpty()
                ? Collections.<String>emptySet()
                : Collections.unmodifiableSet(ignoredClasses);
        return this;
    }

    @Override
    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    public BasicOASConfig setOpenAPI(OpenAPI openAPI) {
        this.openAPI = openAPI;
        return this;
    }

    @Override
    public Map<String, Object> getOptions() {
        return options;
    }

    public BasicOASConfig setOptions(Map<String, Object> options) {
        this.options = options == null || options.isEmpty()
                ? Collections.<String, Object>emptyMap()
                : Collections.unmodifiableMap(new HashMap<>(options));
        return this;
    }

    @Override
    public Set<String> getResourcePackages() {
        return resourcePackages;
    }

    public BasicOASConfig setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages == null || resourcePackages.isEmpty()
                ? Collections.<String>emptySet()
                : Collections.unmodifiableSet(resourcePackages);
        return this;
    }

    @Override
    public boolean isScanDisabled() {
        return scanDisabled;
    }

    public BasicOASConfig setScanDisabled(boolean scanDisabled) {
        this.scanDisabled = scanDisabled;
        return this;
    }
}