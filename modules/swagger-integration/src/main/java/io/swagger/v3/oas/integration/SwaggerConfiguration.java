package io.swagger.v3.oas.integration;

import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.models.OpenAPI;

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

    private Boolean prettyPrint;

    // read all operations also with no @Operation; set to false to read only methods annotated with @Operation
    private Boolean readAllResources = Boolean.TRUE;

    private Collection<String> ignoredRoutes;
    private Long cacheTTL = -1L;

    private Set<String> modelConverterClasses;
    private String objectMapperProcessorClass;

    private Boolean sortOutput;

    private Boolean alwaysResolveAppPath;

    private Boolean openAPI31;

    public Long getCacheTTL() {
        return cacheTTL;
    }

    public void setCacheTTL(Long cacheTTL) {
        this.cacheTTL = cacheTTL;
    }

    public SwaggerConfiguration cacheTTL(Long cacheTTL) {
        this.cacheTTL = cacheTTL;
        return this;
    }

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

    public Boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public SwaggerConfiguration prettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }

    @Override
    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    public void setOpenAPI(OpenAPI openAPI) {
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

    /**
     * @since 2.0.6
     */
    public SwaggerConfiguration objectMapperProcessorClass(String objectMapperProcessorClass) {
        this.objectMapperProcessorClass = objectMapperProcessorClass;
        return this;
    }

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
    public Set<String> getModelConverterClasses() {
        return modelConverterClasses;
    }

    /**
     * @since 2.0.6
     */
    public void setModelConverterClassess(Set<String> modelConverterClasses) {
        this.modelConverterClasses = modelConverterClasses;
    }

    /**
     * @since 2.0.6
     */
    public SwaggerConfiguration modelConverterClasses(Set<String> modelConverterClasses) {
        this.modelConverterClasses = modelConverterClasses;
        return this;
    }

    /**
     * @since 2.1.6
     */
    @Override
    public Boolean isSortOutput() {
        return sortOutput;
    }

    /**
     * @since 2.1.6
     */
    public void setSortOutput(Boolean sortOutput) {
        this.sortOutput = sortOutput;
    }

    /**
     * @since 2.1.6
     */
    public SwaggerConfiguration sortOutput(Boolean sortOutput) {
        setSortOutput(sortOutput);
        return this;
    }

    /**
     * @since 2.1.9
     */
    @Override
    public Boolean isAlwaysResolveAppPath() {
        return alwaysResolveAppPath;
    }

    /**
     * @since 2.1.9
     */
    public void setAlwaysResolveAppPath(Boolean alwaysResolveAppPath) {
        this.alwaysResolveAppPath = alwaysResolveAppPath;
    }

    /**
     * @since 2.1.9
     */
    public SwaggerConfiguration alwaysResolveAppPath(Boolean alwaysResolveAppPath) {
        setAlwaysResolveAppPath(alwaysResolveAppPath);
        return this;
    }

    /**
     * @since 2.1.9
     */
    public Boolean isOpenAPI31() {
        return openAPI31;
    }

    /**
     * @since 2.1.9
     */
    public void setOpenAPI31(Boolean openAPI31) {
        this.openAPI31 = openAPI31;
    }

    /**
     * @since 2.1.9
     */
    public SwaggerConfiguration openAPI31(Boolean openAPI31) {
        this.openAPI31 = openAPI31;
        return this;
    }
}
