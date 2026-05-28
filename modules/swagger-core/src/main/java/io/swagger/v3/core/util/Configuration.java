package io.swagger.v3.core.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Map;
import java.util.Set;

public class Configuration {

    public enum GroupsValidationStrategy {
        @JsonProperty("default")
        DEFAULT("default"),
        @JsonProperty("never")
        NEVER("never"),
        @JsonProperty("always")
        ALWAYS("always"),
        @JsonProperty("neverIfNoContext")
        NEVER_IF_NO_CONTEXT("neverIfNoContext");

        private String value;

        GroupsValidationStrategy(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private Map<String, Object> userDefinedOptions;
    private OpenAPI openAPI;
    private Set<String> modelConverterClasses;
    private String objectMapperProcessorClass;
    private Boolean openAPI31 = false;
    private Schema.SchemaResolution schemaResolution = Schema.SchemaResolution.DEFAULT;
    private String openAPIVersion = "3.0.1";
    private GroupsValidationStrategy groupsValidationStrategy = GroupsValidationStrategy.DEFAULT;
    private String validatorProcessorClass;

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    public void setOpenAPI(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    public Configuration openAPI(OpenAPI openAPI) {
        this.openAPI = openAPI;
        return this;
    }

    public Map<String, Object> getUserDefinedOptions() {
        return userDefinedOptions;
    }

    public void setUserDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
    }

    public Configuration userDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
        return this;
    }

    public Configuration objectMapperProcessorClass(String objectMapperProcessorClass) {
        this.objectMapperProcessorClass = objectMapperProcessorClass;
        return this;
    }

    public String getObjectMapperProcessorClass() {
        return objectMapperProcessorClass;
    }

    public void setObjectMapperProcessorClass(String objectMapperProcessorClass) {
        this.objectMapperProcessorClass = objectMapperProcessorClass;
    }

    public Set<String> getModelConverterClasses() {
        return modelConverterClasses;
    }

    public void setModelConverterClassess(Set<String> modelConverterClasses) {
        this.modelConverterClasses = modelConverterClasses;
    }

    public Configuration modelConverterClasses(Set<String> modelConverterClasses) {
        this.modelConverterClasses = modelConverterClasses;
        return this;
    }

    public Boolean isOpenAPI31() {
        return openAPI31;
    }

    public void setOpenAPI31(Boolean openAPI31) {
        this.openAPI31 = openAPI31;
    }

    public Configuration openAPI31(Boolean openAPI31) {
        this.openAPI31 = openAPI31;
        return this;
    }

    public Schema.SchemaResolution getSchemaResolution() {
        return schemaResolution;
    }

    public void setSchemaResolution(Schema.SchemaResolution schemaResolution) {
        this.schemaResolution = schemaResolution;
    }

    public Configuration schemaResolution(Schema.SchemaResolution schemaResolution) {
        this.setSchemaResolution(schemaResolution);
        return this;
    }

    public String getOpenAPIVersion() {
        return openAPIVersion;
    }

    public void setOpenAPIVersion(String openAPIVersion) {
        this.openAPIVersion = openAPIVersion;
    }

    public Configuration openAPIVersion(String openAPIVersion) {
        this.setOpenAPIVersion(openAPIVersion);
        return this;
    }

    public void setModelConverterClasses(Set<String> modelConverterClasses) {
        this.modelConverterClasses = modelConverterClasses;
    }

    public GroupsValidationStrategy getGroupsValidationStrategy() {
        return groupsValidationStrategy;
    }

    public void setGroupsValidationStrategy(GroupsValidationStrategy groupsValidationStrategy) {
        this.groupsValidationStrategy = groupsValidationStrategy;
    }

    public Configuration groupsValidationStrategy(GroupsValidationStrategy groupsValidationStrategy) {
        this.groupsValidationStrategy = groupsValidationStrategy;
        return this;
    }

    public String getValidatorProcessorClass() {
        return validatorProcessorClass;
    }

    public void setValidatorProcessorClass(String validatorProcessorClass) {
        this.validatorProcessorClass = validatorProcessorClass;
    }

    public Configuration validatorProcessorClass(String validatorProcessorClass) {
        this.validatorProcessorClass = validatorProcessorClass;
        return this;
    }
}
