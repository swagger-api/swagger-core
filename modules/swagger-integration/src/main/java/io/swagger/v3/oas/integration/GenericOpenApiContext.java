package io.swagger.v3.oas.integration;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.PathsSerializer;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.integration.api.ObjectMapperProcessor;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiConfigurationLoader;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.integration.api.OpenApiReader;
import io.swagger.v3.oas.integration.api.OpenApiScanner;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GenericOpenApiContext<T extends GenericOpenApiContext> implements OpenApiContext {

    private static Logger LOGGER = LoggerFactory.getLogger(GenericOpenApiContext.class);

    protected Set<String> resourcePackages;
    protected Set<String> resourceClasses;
    protected String id = OPENAPI_CONTEXT_ID_DEFAULT;
    protected OpenApiContext parent;
    protected String configLocation;
    private OpenAPIConfiguration openApiConfiguration;

    private OpenApiReader openApiReader;
    private OpenApiScanner openApiScanner;
    private OpenApiReader providedOpenApiReader;

    private ObjectMapperProcessor objectMapperProcessor;
    private Set<ModelConverter> modelConverters;

    private ObjectMapper outputJsonMapper;
    private ObjectMapper outputYamlMapper;

    private ConcurrentHashMap<String, Cache> cache = new ConcurrentHashMap<>();

    // 0 doesn't cache
    // -1 perpetual
    private long cacheTTL = -1;

    private Boolean openAPI31;

    public long getCacheTTL() {
        return cacheTTL;
    }

    public void setCacheTTL(long cacheTTL) {
        this.cacheTTL = cacheTTL;
    }

    public T cacheTTL(long cacheTTL) {
        this.cacheTTL = cacheTTL;
        return (T) this;
    }

    public OpenApiReader getOpenApiReader() {
        return openApiReader;
    }

    @Override
    public void setOpenApiReader(OpenApiReader openApiReader) {
        this.openApiReader = openApiReader;
        providedOpenApiReader = openApiReader;
    }

    public OpenApiScanner getOpenApiScanner() {
        return openApiScanner;
    }

    @Override
    public void setOpenApiScanner(OpenApiScanner openApiScanner) {
        this.openApiScanner = openApiScanner;
    }

    public final T openApiReader(OpenApiReader openApiReader) {
        setOpenApiReader(openApiReader);
        return (T) this;
    }

    public final T openApiScanner(OpenApiScanner openApiScanner) {
        this.openApiScanner = openApiScanner;
        return (T) this;
    }

    public Set<String> getResourcePackages() {
        return resourcePackages;
    }

    public void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
    }

    public T resourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
        return (T) this;
    }

    public Set<String> getResourceClasses() {
        return resourceClasses;
    }

    public void setResourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
    }

    public T resourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
        return (T) this;
    }

    public T openApiConfiguration(OpenAPIConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
        return (T) this;
    }

    @Override
    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public final T configLocation(String configLocation) {
        this.configLocation = configLocation;
        return (T) this;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public final T id(String id) {
        this.id = id;
        return (T) this;
    }

    @Override
    public OpenApiContext getParent() {
        return this.parent;
    }

    public void setParent(OpenApiContext parent) {
        this.parent = parent;
    }

    public final T parent(OpenApiContext parent) {
        this.parent = parent;
        return (T) this;
    }

    /**
     * @since 2.0.6
     */
    public ObjectMapperProcessor getObjectMapperProcessor() {
        return objectMapperProcessor;
    }

    /**
     * @since 2.0.6
     */
    @Override
    public void setObjectMapperProcessor(ObjectMapperProcessor objectMapperProcessor) {
        this.objectMapperProcessor = objectMapperProcessor;
    }

    /**
     * @since 2.0.6
     */
    public final T objectMapperProcessor(ObjectMapperProcessor objectMapperProcessor) {
        this.objectMapperProcessor = objectMapperProcessor;
        return (T) this;
    }

    /**
     * @since 2.0.6
     */
    public Set<ModelConverter> getModelConverters() {
        return modelConverters;
    }

    /**
     * @since 2.0.6
     */
    @Override
    public void setModelConverters(Set<ModelConverter> modelConverters) {
        this.modelConverters = modelConverters;
    }

    /**
     * @since 2.0.6
     */
    public final T modelConverters(Set<ModelConverter> modelConverters) {
        this.modelConverters = modelConverters;
        return (T) this;
    }

    /**
     * @since 2.1.6
     */
    public ObjectMapper getOutputJsonMapper() {
        return outputJsonMapper;
    }

    /**
     * @since 2.1.6
     */
    @Override
    public void setOutputJsonMapper(ObjectMapper outputJsonMapper) {
        this.outputJsonMapper = outputJsonMapper;
    }

    /**
     * @since 2.1.6
     */
    public final T outputJsonMapper(ObjectMapper outputJsonMapper) {
        this.outputJsonMapper = outputJsonMapper;
        return (T) this;
    }

    /**
     * @since 2.1.6
     */
    public ObjectMapper getOutputYamlMapper() {
        return outputYamlMapper;
    }

    /**
     * @since 2.1.6
     */
    @Override
    public void setOutputYamlMapper(ObjectMapper outputYamlMapper) {
        this.outputYamlMapper = outputYamlMapper;
    }

    /**
     * @since 2.1.6
     */
    public final T outputYamlMapper(ObjectMapper outputYamlMapper) {
        this.outputYamlMapper = outputYamlMapper;
        return (T) this;
    }

    /**
     * @since 2.1.8
     */
    public Boolean isOpenAPI31() {
        return openAPI31;
    }

    /**
     * @since 2.1.8
     */
    public void setOpenAPI31(Boolean v) {
        this.openAPI31 = openAPI31;
    }

    /**
     * @since 2.1.8
     */
    public T openAPI31(Boolean openAPI31) {
        this.openAPI31 = openAPI31;
        return (T) this;
    }


    protected void register() {
        OpenApiContextLocator.getInstance().putOpenApiContext(id, this);
    }

    @Override
    public OpenAPIConfiguration getOpenApiConfiguration() {
        return openApiConfiguration;
    }

    public void setOpenApiConfiguration(OpenAPIConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
    }

    protected OpenApiReader buildReader(final OpenAPIConfiguration openApiConfiguration) throws Exception {
        OpenApiReader reader;
        if (StringUtils.isNotBlank(openApiConfiguration.getReaderClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getReaderClass());
            reader = (OpenApiReader) cls.newInstance();
        } else {
            reader = new OpenApiReader() {

                OpenAPIConfiguration openApiConfiguration;

                @Override
                public void setConfiguration(OpenAPIConfiguration openApiConfiguration) {
                    this.openApiConfiguration = openApiConfiguration;
                }

                @Override
                public OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources) {
                    return openApiConfiguration.getOpenAPI();
                }
            };
        }
        reader.setConfiguration(openApiConfiguration);
        return reader;
    }

    protected OpenApiScanner buildScanner(final OpenAPIConfiguration openApiConfiguration) throws Exception {
        OpenApiScanner scanner;
        if (StringUtils.isNotBlank(openApiConfiguration.getScannerClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getScannerClass());
            scanner = (OpenApiScanner) cls.newInstance();
        } else {
            scanner = new GenericOpenApiScanner();
        }
        scanner.setConfiguration(openApiConfiguration);
        return scanner;
    }

    protected ObjectMapperProcessor buildObjectMapperProcessor(final OpenAPIConfiguration openApiConfiguration) throws Exception {
        ObjectMapperProcessor objectMapperProcessor = null;
        if (StringUtils.isNotBlank(openApiConfiguration.getObjectMapperProcessorClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getObjectMapperProcessorClass());
            objectMapperProcessor = (ObjectMapperProcessor) cls.newInstance();
        }
        return objectMapperProcessor;
    }

    protected Set<ModelConverter> buildModelConverters(final OpenAPIConfiguration openApiConfiguration) throws Exception {
        if (openApiConfiguration.getModelConverterClasses() != null && !openApiConfiguration.getModelConverterClasses().isEmpty()) {
            LinkedHashSet<ModelConverter> modelConverters = new LinkedHashSet<>();
            for (String converterClass: openApiConfiguration.getModelConverterClasses()) {
                Class cls = getClass().getClassLoader().loadClass(converterClass);
                ModelConverter converter = (ModelConverter) cls.newInstance();
                modelConverters.add(converter);
            }
            return modelConverters;
        }
        return null;
    }

    protected List<ImmutablePair<String, String>> getKnownLocations() {
        return Arrays.asList(
                new ImmutablePair<>("classpath", "openapi-configuration.yaml"),
                new ImmutablePair<>("classpath", "openapi-configuration.json"),
                new ImmutablePair<>("file", "openapi-configuration.yaml"),
                new ImmutablePair<>("file", "openapi-configuration.json"),
                new ImmutablePair<>("classpath", "openapi.yaml"),
                new ImmutablePair<>("classpath", "openapi.json"),
                new ImmutablePair<>("file", "openapi.yaml"),
                new ImmutablePair<>("file", "openapi.json"),
                new ImmutablePair<>("service", "")
        );
    }

    protected Map<String, OpenApiConfigurationLoader> getLocationLoaders() {
        Map<String, OpenApiConfigurationLoader> map = new HashMap<>();
        map.put("classpath", new ClasspathOpenApiConfigurationLoader());
        map.put("file", new FileOpenApiConfigurationLoader());
        map.put("url", new URLOpenApiConfigurationLoader());
        map.put("service", new ServiceOpenApiConfigurationLoader());
        return map;
    }

    protected OpenAPIConfiguration loadConfiguration() throws OpenApiConfigurationException {

        Map<String, OpenApiConfigurationLoader> loaders = getLocationLoaders();
        try {

            if (StringUtils.isNotEmpty(configLocation)) {
                if (loaders.get("classpath").exists(configLocation)) {
                    return loaders.get("classpath").load(configLocation);
                }
                if (loaders.get("file").exists(configLocation)) {
                    return loaders.get("file").load(configLocation);
                }
            }
            // check known locations
            List<ImmutablePair<String, String>> knownLocations = getKnownLocations();
            for (ImmutablePair<String, String> location : knownLocations) {
                if (loaders.get(location.left).exists(location.right)) {
                    try {
                        return loaders.get(location.left).load(location.right);
                    } catch (IOException ioe) {
                        // try next one
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new OpenApiConfigurationException(e.getMessage(), e);
        }
    }

    @Override
    public T init() throws OpenApiConfigurationException {

        if (openApiConfiguration == null) {
            openApiConfiguration = loadConfiguration();
        }

        if (openApiConfiguration == null) {
            openApiConfiguration = new SwaggerConfiguration().resourcePackages(resourcePackages).resourceClasses(resourceClasses);
            ((SwaggerConfiguration) openApiConfiguration).setId(id);
            ((SwaggerConfiguration) openApiConfiguration).setOpenAPI31(openAPI31);
        }

        openApiConfiguration = mergeParentConfiguration(openApiConfiguration, parent);

        try {
            if (openApiReader == null) {
                openApiReader = buildReader(ContextUtils.deepCopy(openApiConfiguration));
            }
            if (openApiScanner == null) {
                openApiScanner = buildScanner(ContextUtils.deepCopy(openApiConfiguration));
            }
            if (objectMapperProcessor == null) {
                objectMapperProcessor = buildObjectMapperProcessor(ContextUtils.deepCopy(openApiConfiguration));
            }
            if (modelConverters == null || modelConverters.isEmpty()) {
                modelConverters = buildModelConverters(ContextUtils.deepCopy(openApiConfiguration));
            }
            if (outputJsonMapper == null) {
                if (Boolean.TRUE.equals(openApiConfiguration.isOpenAPI31())) {
                    outputJsonMapper = Json31.mapper().copy();
                } else {
                    outputJsonMapper = Json.mapper().copy();
                }
            }
            if (outputYamlMapper == null) {
                if (Boolean.TRUE.equals(openApiConfiguration.isOpenAPI31())) {
                    outputYamlMapper = Yaml31.mapper().copy();
                } else {
                    outputYamlMapper = Yaml.mapper().copy();
                }
            }
            if (openApiConfiguration.isSortOutput() != null && openApiConfiguration.isSortOutput()) {
                outputJsonMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
                outputJsonMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
                outputYamlMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
                outputYamlMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
                if (Boolean.TRUE.equals(openApiConfiguration.isOpenAPI31())) {
                    outputJsonMapper.addMixIn(OpenAPI.class, SortedOpenAPIMixin31.class);
                    outputJsonMapper.addMixIn(Schema.class, SortedSchemaMixin31.class);
                    outputYamlMapper.addMixIn(OpenAPI.class, SortedOpenAPIMixin31.class);
                    outputYamlMapper.addMixIn(Schema.class, SortedSchemaMixin31.class);
                } else {
                    outputJsonMapper.addMixIn(OpenAPI.class, SortedOpenAPIMixin.class);
                    outputJsonMapper.addMixIn(Schema.class, SortedSchemaMixin.class);
                    outputYamlMapper.addMixIn(OpenAPI.class, SortedOpenAPIMixin.class);
                    outputYamlMapper.addMixIn(Schema.class, SortedSchemaMixin.class);
                }
            }
        } catch (Exception e) {
            LOGGER.error("error initializing context: " + e.getMessage(), e);
            throw new OpenApiConfigurationException("error initializing context: " + e.getMessage(), e);
        }


        try {
            if (objectMapperProcessor != null) {
                ObjectMapper mapper = IntegrationObjectMapperFactory.createJson();
                objectMapperProcessor.processJsonObjectMapper(mapper);
                ModelConverters.getInstance().addConverter(new ModelResolver(mapper));

                objectMapperProcessor.processOutputJsonObjectMapper(outputJsonMapper);
                objectMapperProcessor.processOutputYamlObjectMapper(outputYamlMapper);
            }
        } catch (Exception e) {
            LOGGER.error("error configuring objectMapper: " + e.getMessage(), e);
            throw new OpenApiConfigurationException("error configuring objectMapper: " + e.getMessage(), e);
        }

        try {
            if (modelConverters != null && !modelConverters.isEmpty()) {
                for (ModelConverter converter: modelConverters) {
                    ModelConverters.getInstance().addConverter(converter);
                }
            }
        } catch (Exception e) {
            LOGGER.error("error configuring model converters: " + e.getMessage(), e);
            throw new OpenApiConfigurationException("error configuring model converters: " + e.getMessage(), e);
        }

        // set cache TTL if present in configuration
        if (openApiConfiguration.getCacheTTL() != null) {
            this.cacheTTL = openApiConfiguration.getCacheTTL();
        }
        register();
        return (T) this;
    }

    private OpenAPIConfiguration mergeParentConfiguration(OpenAPIConfiguration config, OpenApiContext parent) {
        if (parent == null || parent.getOpenApiConfiguration() == null) {
            return config;
        }
        OpenAPIConfiguration parentConfig = parent.getOpenApiConfiguration();

        SwaggerConfiguration merged = null;

        if (config instanceof SwaggerConfiguration) {
            merged = (SwaggerConfiguration) config;
        } else {
            merged = (SwaggerConfiguration) ContextUtils.deepCopy(config);
        }

        if (merged.getResourceClasses() == null) {
            merged.setResourceClasses(parentConfig.getResourceClasses());
        }
        if (merged.getFilterClass() == null) {
            merged.setFilterClass(parentConfig.getFilterClass());
        }
        if (merged.getIgnoredRoutes() == null) {
            merged.setIgnoredRoutes(parentConfig.getIgnoredRoutes());
        }
        if (merged.getOpenAPI() == null) {
            merged.setOpenAPI(parentConfig.getOpenAPI());
        }
        if (merged.getReaderClass() == null) {
            merged.setReaderClass(parentConfig.getReaderClass());
        }
        if (merged.getResourcePackages() == null) {
            merged.setResourcePackages(parentConfig.getResourcePackages());
        }
        if (merged.getScannerClass() == null) {
            merged.setScannerClass(parentConfig.getScannerClass());
        }
        if (merged.getCacheTTL() == null) {
            merged.setCacheTTL(parentConfig.getCacheTTL());
        }
        if (merged.getUserDefinedOptions() == null) {
            merged.setUserDefinedOptions(parentConfig.getUserDefinedOptions());
        }
        if (merged.isPrettyPrint() == null) {
            merged.setPrettyPrint(parentConfig.isPrettyPrint());
        }
        if (merged.isSortOutput() == null) {
            merged.setSortOutput(parentConfig.isSortOutput());
        }
        if (merged.isAlwaysResolveAppPath() == null) {
            merged.setAlwaysResolveAppPath(parentConfig.isAlwaysResolveAppPath());
        }
        if (merged.isReadAllResources() == null) {
            merged.setReadAllResources(parentConfig.isReadAllResources());
        }
        if (merged.getObjectMapperProcessorClass() == null) {
            merged.setObjectMapperProcessorClass(parentConfig.getObjectMapperProcessorClass());
        }
        if (merged.getModelConverterClasses() == null) {
            merged.setModelConverterClassess(parentConfig.getModelConverterClasses());
        }
        if (merged.isOpenAPI31() == null) {
            merged.setOpenAPI31(parentConfig.isOpenAPI31());
        }

        return merged;
    }

    @Override
    public OpenAPI read() {

        if (cacheTTL == 0) {
            resetReader();
            return getOpenApiReader().read(getOpenApiScanner().classes(), getOpenApiScanner().resources());
        }
        Cache cached = cache.get("openapi");
        if (cached == null || cached.isStale(cacheTTL)) {
            cached = new Cache();
            cached.createdAt = System.currentTimeMillis();
            resetReader();
            cached.openApi = getOpenApiReader().read(getOpenApiScanner().classes(), getOpenApiScanner().resources());
            cache.put("openapi", cached);
        }
        return cached.openApi;
    }

    protected void resetReader() {
        if (providedOpenApiReader == null) {
            try {
                openApiReader = buildReader(ContextUtils.deepCopy(openApiConfiguration));
            } catch (Exception e) {
                LOGGER.error("error building reader: " + e.getMessage(), e);
                // keep previous reader
            }
        }
    }

    static class Cache {
        long createdAt = 0;
        OpenAPI openApi;

        boolean isStale(long cacheTTL) {
            return (cacheTTL > 0 && System.currentTimeMillis() - createdAt > cacheTTL);
        }
    }

    @JsonPropertyOrder(value = {"openapi", "info", "externalDocs", "servers", "security", "tags", "paths", "components"}, alphabetic = true)
    static abstract class SortedOpenAPIMixin {

        @JsonAnyGetter
        @JsonPropertyOrder(alphabetic = true)
        public abstract Map<String, Object> getExtensions();

        @JsonAnySetter
        public abstract void addExtension(String name, Object value);

        @JsonSerialize(using = PathsSerializer.class)
        public abstract Paths getPaths();
    }

    @JsonPropertyOrder(value = {"type", "format"}, alphabetic = true)
    static abstract class SortedSchemaMixin {

        @JsonAnyGetter
        @JsonPropertyOrder(alphabetic = true)
        public abstract Map<String, Object> getExtensions();

        @JsonAnySetter
        public abstract void addExtension(String name, Object value);

        @JsonIgnore
        public abstract boolean getExampleSetFlag();

        @JsonInclude(JsonInclude.Include.CUSTOM)
        public abstract Object getExample();

        @JsonIgnore
        public abstract Map<String, Object> getJsonSchema();

        @JsonIgnore
        public abstract BigDecimal getExclusiveMinimumValue();

        @JsonIgnore
        public abstract BigDecimal getExclusiveMaximumValue();

        @JsonIgnore
        public abstract Map<String, Schema> getPatternProperties();

        @JsonIgnore
        public abstract Schema getContains();
        @JsonIgnore
        public abstract String get$id();
        @JsonIgnore
        public abstract String get$anchor();
        @JsonIgnore
        public abstract String get$schema();
        @JsonIgnore
        public abstract Set<String> getTypes();

        @JsonIgnore
        public abstract Object getJsonSchemaImpl();


    }

    @JsonPropertyOrder(value = {"openapi", "info", "externalDocs", "servers", "security", "tags", "paths", "components", "webhooks"}, alphabetic = true)
    static abstract class SortedOpenAPIMixin31 {

        @JsonAnyGetter
        @JsonPropertyOrder(alphabetic = true)
        public abstract Map<String, Object> getExtensions();

        @JsonAnySetter
        public abstract void addExtension(String name, Object value);

        @JsonSerialize(using = PathsSerializer.class)
        public abstract Paths getPaths();
    }

    @JsonPropertyOrder(value = {"type", "format"}, alphabetic = true)
    static abstract class SortedSchemaMixin31 {

        @JsonAnyGetter
        @JsonPropertyOrder(alphabetic = true)
        public abstract Map<String, Object> getExtensions();

        @JsonIgnore
        public abstract Map<String, Object> getJsonSchema();

        @JsonIgnore
        public abstract Boolean getNullable();

        @JsonIgnore
        public abstract Boolean getExclusiveMinimum();

        @JsonIgnore
        public abstract Boolean getExclusiveMaximum();

        @JsonProperty("exclusiveMinimum")
        public abstract BigDecimal getExclusiveMinimumValue();

        @JsonProperty("exclusiveMaximum")
        public abstract BigDecimal getExclusiveMaximumValue();

        @JsonIgnore
        public abstract String getType();

        @JsonProperty("type")
        public abstract Set<String> getTypes();

        @JsonAnySetter
        public abstract void addExtension(String name, Object value);

        @JsonIgnore
        public abstract boolean getExampleSetFlag();

        @JsonInclude(JsonInclude.Include.CUSTOM)
        public abstract Object getExample();

        @JsonIgnore
        public abstract Object getJsonSchemaImpl();

    }

}
