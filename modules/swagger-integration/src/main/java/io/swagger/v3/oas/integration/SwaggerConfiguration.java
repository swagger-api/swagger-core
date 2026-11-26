package io.swagger.v3.oas.integration;

import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;

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

    private Boolean skipResolveAppPath;

    private Boolean openAPI31 = false;

    private Boolean convertToOpenAPI31;

    private Schema.SchemaResolution schemaResolution = Schema.SchemaResolution.DEFAULT;

    private String openAPIVersion;

    private Configuration.GroupsValidationStrategy groupsValidationStrategy = Configuration.GroupsValidationStrategy.DEFAULT;

    private String validatorProcessorClass;

    private Boolean ignoreHidden = Boolean.FALSE;


    @Override
    public String getDefaultResponseCode() {
        return defaultResponseCode;
    }

    public void setDefaultResponseCode(String defaultResponseCode) {
        this.defaultResponseCode = defaultResponseCode;
    }

    public SwaggerConfiguration defaultResponseCode(String defaultResponseCode) {
        this.defaultResponseCode = defaultResponseCode;
        return this;
    }

    private String defaultResponseCode;

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
     * @since 2.1.15
     */
    @Override
    public Boolean isSkipResolveAppPath() {
        return skipResolveAppPath;
    }

    /**
     * @since 2.1.15
     */
    public void setSkipResolveAppPath(Boolean skipResolveAppPath) {
        this.skipResolveAppPath = skipResolveAppPath;
    }

    /**
     * @since 2.1.15
     */
    public SwaggerConfiguration skipResolveAppPath(Boolean skipResolveAppPath) {
        setSkipResolveAppPath(skipResolveAppPath);
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

    /**
     * @since 2.2.12
     */
    public Boolean isConvertToOpenAPI31() {
        return convertToOpenAPI31;
    }

    /**
     * @since 2.2.12
     */
    public void setConvertToOpenAPI31(Boolean convertToOpenAPI31) {
        this.convertToOpenAPI31 = convertToOpenAPI31;
        if (Boolean.TRUE.equals(convertToOpenAPI31)) {
            this.openAPI31 = true;
        }
    }

    /**
     * @since 2.2.12
     */
    public SwaggerConfiguration convertToOpenAPI31(Boolean convertToOpenAPI31) {
        this.setConvertToOpenAPI31(convertToOpenAPI31);
        return this;
    }

    @Override
    public Schema.SchemaResolution getSchemaResolution() {
        return schemaResolution;
    }

    public void setSchemaResolution(Schema.SchemaResolution schemaResolution) {
        this.schemaResolution = schemaResolution;
    }

    public SwaggerConfiguration schemaResolution(Schema.SchemaResolution schemaResolution) {
        this.setSchemaResolution(schemaResolution);
        return this;
    }

    /**
     * @since 2.2.28
     */
    @Override
    public String getOpenAPIVersion() {
        return openAPIVersion;
    }

    /**
     * @since 2.2.28
     */
    public void setOpenAPIVersion(String openAPIVersion) {
        this.openAPIVersion = openAPIVersion;
    }

    /**
     * @since 2.2.28
     */
    public SwaggerConfiguration openAPIVersion(String openAPIVersion) {
        this.setOpenAPIVersion(openAPIVersion);
        return this;
    }

    /**
     * @since 2.2.29
     */
    @Override
    public Configuration.GroupsValidationStrategy getGroupsValidationStrategy() {
        return groupsValidationStrategy;
    }

    /**
     * @since 2.2.29
     */
    public void setGroupsValidationStrategy(Configuration.GroupsValidationStrategy groupsValidationStrategy) {
        this.groupsValidationStrategy = groupsValidationStrategy;
    }

    /**
     * @since 2.2.29
     */
    public SwaggerConfiguration groupsValidationStrategy(Configuration.GroupsValidationStrategy groupsValidationStrategy) {
        this.groupsValidationStrategy = groupsValidationStrategy;
        return this;
    }

    /**
     * see io.swagger.v3.core.util.ValidatorProcessor
     *
     * @since 2.2.29
     */

    @Override
    public String getValidatorProcessorClass() {
        return validatorProcessorClass;
    }

    /**
     * @since 2.2.29
     */
    public void setValidatorProcessorClass(String validatorProcessorClass) {
        this.validatorProcessorClass = validatorProcessorClass;
    }

    /**
     * @since 2.2.29
     */
    public SwaggerConfiguration validatorProcessorClass(String validatorProcessorClass) {
        this.validatorProcessorClass = validatorProcessorClass;
        return this;
    }

    /**
     * @since 2.2.40
     */
    @Override
    public Boolean isIgnoreHidden() {
        return ignoreHidden;
    }

    /**
     * @since 2.2.40
     */
    public void setIgnoreHidden(Boolean ignoreHidden) {
        this.ignoreHidden = ignoreHidden;
    }

    /**
     * @since 2.2.40
     */
    public SwaggerConfiguration ignoreHidden(Boolean ignoreHidden) {
        this.ignoreHidden = ignoreHidden;
        return this;
    }

    public Configuration toConfiguration() {
        Configuration configuration = new Configuration();

        configuration.setOpenAPI(getOpenAPI());
        configuration.setUserDefinedOptions(getUserDefinedOptions());
        configuration.setModelConverterClasses(getModelConverterClasses());
        configuration.setObjectMapperProcessorClass(getObjectMapperProcessorClass());
        configuration.setOpenAPI31(isOpenAPI31());
        configuration.setSchemaResolution(getSchemaResolution());
        configuration.setOpenAPIVersion(getOpenAPIVersion());
        configuration.setGroupsValidationStrategy(getGroupsValidationStrategy());
        configuration.setValidatorProcessorClass(getValidatorProcessorClass());
        configuration.setIgnoreHidden(isIgnoreHidden());

        return configuration;
    }

}
