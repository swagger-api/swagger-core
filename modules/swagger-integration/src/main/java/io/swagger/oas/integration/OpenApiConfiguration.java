package io.swagger.oas.integration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.web.OpenAPIConfig;
import io.swagger.oas.web.OpenApiReader;
import io.swagger.oas.web.OpenApiScanner;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OpenApiConfiguration implements OpenAPIConfig {

    private OpenAPI openApi = new OpenAPI();

    Map<String, Object> userDefinedOptions = new ConcurrentHashMap<>();
    private boolean basePathAsKey; // TODO

    private String id;
    private String resourcePackageNames;
    private String resourceClassNames;
    private String filterClassName;
    private String readerClassName;
    private String scannerClassName;
    private String processorClassName;

    private boolean prettyPrint;
    private boolean scanAllResources;
    private Collection<String> ignoredRoutes = Collections.emptySet();

    public boolean isScanAllResources() {
        return scanAllResources;
    }

    public void setScanAllResources(boolean scanAllResources) {
        this.scanAllResources = scanAllResources;
    }

    public OpenApiConfiguration withScanAllResources(boolean scanAllResources) {
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

    public OpenApiConfiguration withIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes == null || ignoredRoutes.isEmpty() ? Collections.<String>emptySet()
                : Collections.unmodifiableCollection(new HashSet<String>(ignoredRoutes));
        return this;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public OpenApiConfiguration withPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }

    @Override
    public Set<String> getResourcePackages() {
        if (StringUtils.isBlank(resourcePackageNames)) {
            return null;
        }
        Set<String> packages = new HashSet<>();
        String[] parts = resourcePackageNames.split(",");
        for (String pkg : parts) {
            if (!"".equals(pkg)) {
                packages.add(pkg);
            }
        }
        return Collections.unmodifiableSet(packages);
    }

    @Override
    public OpenAPI getOpenAPI() {
        return openApi;
    }

    @Override
    public Set<Class<?>> getResourceClasses() {
        return resourceClasses;
    }

    @Override
    public Class<OpenApiReader> getReaderClass() {
        // TODO
        if (StringUtils.isBlank(readerClassName)) {
            return null;
        }
        try {
            return (Class<OpenApiReader>) this.getClass().getClassLoader().loadClass(readerClassName);
        } catch (Exception e) {
            // TODO
            return null;
        }
    }

    @Override
    public Class<OpenApiScanner> getScannerClass() {
        // TODO
        if (StringUtils.isBlank(scannerClassName)) {
            return null;
        }
        try {
            return (Class<OpenApiScanner>) this.getClass().getClassLoader().loadClass(scannerClassName);
        } catch (Exception e) {
            // TODO
            return null;
        }
    }

    @Override
    public Class<?> getFilterClass() {
        // TODO use SwaggerSpecFilter
        if (StringUtils.isBlank(filterClassName)) {
            return null;
        }
        try {
            return (Class<?>) this.getClass().getClassLoader().loadClass(filterClassName);
        } catch (Exception e) {
            // TODO
            return null;
        }
    }

    public void setResourceClasses(Set<Class<?>> resourceClasses) {
        this.resourceClasses = resourceClasses;
    }

    @JsonIgnore
    private Set<Class<?>> resourceClasses;


    private boolean pathAsProcessorKey;

    public void setOpenApi (OpenAPI openApi) {
        this.openApi = openApi;
    }
    public OpenApiConfiguration openApi (OpenAPI openApi) {
        this.openApi = openApi;
        return this;
    }

    public boolean isPathAsProcessorKey() {
        return pathAsProcessorKey;
    }

    public void setPathAsProcessorKey(boolean pathAsProcessorKey) {
        this.pathAsProcessorKey = pathAsProcessorKey;
    }

    public OpenApiConfiguration withPathAsProcessorKey(boolean pathAsProcessorKey) {
        this.pathAsProcessorKey = pathAsProcessorKey;
        return this;
    }

    public String getReaderClassName() {
        return readerClassName;
    }

    public void setReaderClassName(String readerClassName) {
        this.readerClassName = readerClassName;
    }

    public String getScannerClassName() {
        return scannerClassName;
    }

    public void setScannerClassName(String scannerClassName) {
        this.scannerClassName = scannerClassName;
    }

    public String getProcessorClassName() {
        return processorClassName;
    }

    public void setProcessorClassName(String processorClassName) {
        this.processorClassName = processorClassName;
    }

    public Map<String, Object> getUserDefinedOptions() {
        return userDefinedOptions;
    }

    public void setUserDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
    }

    public OpenApiConfiguration withScannerClass(String scannerClass) {
        this.scannerClassName = scannerClass;
        return this;
    }

    public OpenApiConfiguration withReaderClassName(String readerClass) {
        this.readerClassName = readerClass;
        return this;
    }

    public OpenApiConfiguration withProcessorClass(String processorClass) {
        this.processorClassName = processorClass;
        return this;
    }

    public OpenApiConfiguration withUserDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
        return this;
    }

    public boolean isBasePathAsKey() {
        return basePathAsKey;
    }

    public void setBasePathAsKey(boolean basePathAsKey) {
        this.basePathAsKey = basePathAsKey;
    }

    public OpenApiConfiguration withBasePathAsKey(boolean basePathAsKey) {
        this.basePathAsKey = basePathAsKey;
        return this;
    }

    public String getResourcePackageNames() {
        return resourcePackageNames;
    }

    public void setResourcePackageNames(String resourcePackageNames) {
        this.resourcePackageNames = resourcePackageNames;
    }

    public OpenApiConfiguration withResourcePackageNames(String resourcePackage) {
        this.resourcePackageNames = resourcePackage;
        return this;
    }

    public String getResourceClassNames() {
        return resourceClassNames;
    }

    public void setResourceClassNames(String resourcePackageNames) {
        this.resourceClassNames = resourceClassNames;
    }

    public OpenApiConfiguration withResourceClassNames(String resourceClassNames) {
        this.resourceClassNames = resourceClassNames;
        return this;
    }


    public String getFilterClassName() {
        return filterClassName;
    }

    public void setFilterClassName(String filterClassName) {
        this.filterClassName = filterClassName;
    }

    public OpenApiConfiguration withFilterClass(String filterClass) {
        this.filterClassName = filterClass;
        return this;
    }


    public OpenApiConfiguration withId(String id) {
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
