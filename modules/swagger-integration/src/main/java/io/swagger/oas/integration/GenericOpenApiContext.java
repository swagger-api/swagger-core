package io.swagger.oas.integration;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.web.OpenApiReader;
import io.swagger.oas.web.OpenApiScanner;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
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
    private OpenApiConfiguration openApiConfiguration;

    private OpenApiReader openApiReader;
    private OpenApiScanner openApiScanner;

    private ConcurrentHashMap<String, Cache> cache = new ConcurrentHashMap<>();

    // 0 doesn't cache
    // -1 perpetual
    private long cacheTTL = -1;

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
    }

    public OpenApiScanner getOpenApiScanner() {
        return openApiScanner;
    }

    @Override
    public void setOpenApiScanner(OpenApiScanner openApiScanner) {
        this.openApiScanner = openApiScanner;
    }

    public final T openApiReader(OpenApiReader openApiReader) {
        this.openApiReader = openApiReader;
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

    public T openApiConfiguration(OpenApiConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
        return (T) this;
    }

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

    protected void register() {
        OpenApiContextLocator.getInstance().putOpenApiContext(id, this);
    }

    @Override
    public OpenApiConfiguration getOpenApiConfiguration() {
        return openApiConfiguration;
    }

    public void setOpenApiConfiguration(OpenApiConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
    }

    protected OpenApiReader buildReader(final OpenApiConfiguration openApiConfiguration) throws Exception {
        OpenApiReader reader;
        if (StringUtils.isNotBlank(openApiConfiguration.getReaderClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getReaderClass());
            // TODO instantiate with configuration
            reader = (OpenApiReader) cls.newInstance();
        } else {
            reader = (classes, resources) -> {
                OpenAPI openApi = openApiConfiguration.getOpenAPI();
                return openApi;

            };
        }
        return reader;
    }

    protected OpenApiScanner buildScanner(final OpenApiConfiguration openApiConfiguration) throws Exception {
        OpenApiScanner scanner;
        if (StringUtils.isNotBlank(openApiConfiguration.getScannerClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getScannerClass());
            // TODO instantiate with configuration
            scanner = (OpenApiScanner) cls.newInstance();
        } else {
            scanner = new GenericOpenApiScanner(openApiConfiguration);
        }
        return scanner;
    }

    protected List<ImmutablePair<String, String>> getKnownLocations() {
        return Arrays.asList(
                new ImmutablePair<>("classpath", "openapi-configiguration.yaml"),
                new ImmutablePair<>("classpath", "openapi-configuration.json"),
                new ImmutablePair<>("file", "openapi-configuration.yaml"),
                new ImmutablePair<>("file", "openapi-configuration.json"),
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

    protected OpenApiConfiguration loadConfiguration() throws OpenApiConfigurationException{

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
            List<ImmutablePair<String, String>> knownLocations  = getKnownLocations();
            for (ImmutablePair<String, String> location: knownLocations) {
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
    public T init() throws OpenApiConfigurationException{

        if (openApiConfiguration == null) {
            openApiConfiguration = loadConfiguration();
        }

        if (openApiConfiguration == null) {
            openApiConfiguration = new OpenApiConfiguration().resourcePackages(resourcePackages);
            openApiConfiguration.setId(id);
        }

        mergeParentConfiguration(openApiConfiguration, parent);

        try {
            if (openApiReader == null) {
                openApiReader = buildReader(ContextUtils.deepCopy(openApiConfiguration));
            }
            if (openApiScanner == null) {
                openApiScanner = buildScanner(ContextUtils.deepCopy(openApiConfiguration));
            }
        } catch (Exception e) {
            LOGGER.error("error initializing context: " + e.getMessage(), e);
            throw new OpenApiConfigurationException("error initializing context: " + e.getMessage(), e);
        }

        // set cache TTL if present in configuration
        if (openApiConfiguration.getCacheTTL() != null) {
            this.cacheTTL = openApiConfiguration.getCacheTTL();
        }
        register();
        return (T) this;
    }

    private void mergeParentConfiguration (OpenApiConfiguration config, OpenApiContext parent) {
        if (parent == null || parent.getOpenApiConfiguration() == null) {
            return;
        }
        OpenApiConfiguration parentConfig = parent.getOpenApiConfiguration();
        if (config.getResourceClasses() == null) {
            config.setResourceClasses(parentConfig.getResourceClasses());
        }
        if (config.getFilterClass() == null) {
            config.setFilterClass(parentConfig.getFilterClass());
        }
        if (config.getIgnoredRoutes() == null) {
            config.setIgnoredRoutes(parentConfig.getIgnoredRoutes());
        }
        if (config.getOpenAPI() == null) {
            config.setOpenApi(parentConfig.getOpenAPI());
        }
        if (config.getReaderClass() == null) {
            config.setReaderClass(parentConfig.getReaderClass());
        }
        if (config.getResourcePackages() == null) {
            config.setResourcePackages(parentConfig.getResourcePackages());
        }
        if (config.getScannerClass() == null) {
            config.setScannerClass(parentConfig.getScannerClass());
        }
        if (config.getUserDefinedOptions() == null) {
            config.setUserDefinedOptions(parentConfig.getUserDefinedOptions());
        }
        if (config.isPrettyPrint() == null) {
            config.setPrettyPrint(parentConfig.isPrettyPrint());
        }
        if (config.isScanAllResources() == null) {
            config.setScanAllResources(parentConfig.isScanAllResources());
        }

    }

    @Override
    public OpenAPI read() {

        if (cacheTTL == 0) {
            return getOpenApiReader().read(getOpenApiScanner().classes(), getOpenApiScanner().resources());
        }
        Cache cached = cache.get("openapi");
        if (cached == null || cached.isStale(cacheTTL)) {
            cached = new Cache();
            cached.createdAt = System.currentTimeMillis();
            cached.openApi = getOpenApiReader().read(getOpenApiScanner().classes(), getOpenApiScanner().resources());
            cache.put("openapi", cached);
        }
        return cached.openApi;
    }

    static class Cache {
        long createdAt = 0;
        OpenAPI openApi;

        boolean isStale(long cacheTTL) {
            return (cacheTTL > 0 && System.currentTimeMillis() - createdAt > cacheTTL);
        }
    }



}
