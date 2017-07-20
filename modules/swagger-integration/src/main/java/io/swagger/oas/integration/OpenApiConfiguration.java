package io.swagger.oas.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.info.Info;
import io.swagger.util.Json;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class OpenApiConfiguration {

    private static Logger LOGGER = LoggerFactory.getLogger(OpenApiConfiguration.class);

    private OpenAPI openApi = new OpenAPI();

    Map<String, String> rawProperties = new ConcurrentHashMap<String, String>();
    private boolean basePathAsKey;

    private String id;
    private String resourcePackage;
    private String filterClass;
    private String readerClass;
    private String scannerClass;
    private String processorClass;

    public String getResourceClasses() {
        return resourceClasses;
    }

    public void setResourceClasses(String resourceClasses) {
        this.resourceClasses = resourceClasses;
    }

    private String resourceClasses;

    private boolean pathAsProcessorKey;

    public static Map<String, OpenApiConfiguration> fromUrl(URL location, String defaultId) {

        // get file as string (for the moment, TODO use commons config)
        // load from classpath etc, look from file..
        try {
            Map<String, OpenApiConfiguration> configurationMap = new HashMap<>();

            String configAsString = readUrl(location);
            List<OpenApiConfiguration> configurations = Json.mapper().readValue(configAsString, new TypeReference<List<OpenApiConfiguration>>() {
            });
            for (OpenApiConfiguration config : configurations) {
                // TODO we have multiple base paths in 3.0? how to handle?
                // TODO use urls as kyes, but need to resolve the url with placeholders and use that?
                // for the moment use title instead,
/*
                if (config.openApi.getInfo() == null || StringUtils.isEmpty(config.openApi.getInfo().getTitle())){
                    if (StringUtils.isEmpty(id)) {
                        config.openApi.setInfo(
                                (config.openApi.getInfo() == null ?
                                        new Info() :
                                        config.openApi.getInfo()).title("/")
                        );
                    } else {
                        config.openApi.setInfo(
                                (config.openApi.getInfo() == null ?
                                        new Info() :
                                        config.openApi.getInfo()).title(id)
                        );
                    }
                }

                configurationMap.put(config.openApi.getInfo().getTitle(), config);
*/

                configurationMap.put((config.getId() == null) ? defaultId : config.getId(), config);

/*
                if (StringUtils.isEmpty(config.openApi.basePath("/"))){
                    if (StringUtils.isEmpty(basePath)) {
                        config.openApi.basePath("/");
                    } else {
                        config.openApi.basePath(basePath);
                    }
                }
                configurationMap.put(config.openApi.getBasePath(), config);
*/
            }
            return configurationMap;

        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            throw new RuntimeException("exception reading config", e);
        }

    }

    private static Properties loadProperties(URI uri) throws IOException {
        FileReader reader = new FileReader(new File(uri));
        Properties props = new Properties();
        props.load(reader);
        reader.close();
        return props;
    }

    private static String readUrl(URL url) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine).append("\n");
        }
        in.close();
        return sb.toString();
    }

    public OpenAPI getOpenApi() {
        return openApi;
    }
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

    public String getProcessorClass() {
        return processorClass;
    }

    public void setProcessorClass(String processorClass) {
        this.processorClass = processorClass;
    }

    public Map<String, String> getRawProperties() {
        return rawProperties;
    }

    public void setRawProperties(Map<String, String> rawProperties) {
        this.rawProperties = rawProperties;
    }

    public OpenApiConfiguration withScannerClass(String scannerClass) {
        this.scannerClass = scannerClass;
        return this;
    }

    public OpenApiConfiguration withReaderClass(String readerClass) {
        this.readerClass = readerClass;
        return this;
    }

    public OpenApiConfiguration withProcessorClass(String processorClass) {
        this.processorClass = processorClass;
        return this;
    }

    public OpenApiConfiguration withProperties(Map<String, String> properties) {
        if (properties != null) {
            rawProperties.putAll(properties);
            loadRawProperties();
        }
        return this;
    }

    // TODO ENUM ETC
    private void loadRawProperties() {
        for (String key : rawProperties.keySet()) {
            switch (key.charAt(0)) {
/*                case (RESOURCE_PACKAGE_KEY):
                    resourcePackage = rawProperties.get(key);
                    break;*/
                default:
                    resourcePackage = rawProperties.get(key);
            }
        }

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

    public String getResourcePackage() {
        return resourcePackage;
    }

    public void setResourcePackage(String resourcePackage) {
        this.resourcePackage = resourcePackage;
    }

    public OpenApiConfiguration withResourcePackage(String resourcePackage) {
        this.resourcePackage = resourcePackage;
        return this;
    }


    public String getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    public OpenApiConfiguration withFilterClass(String filterClass) {
        this.filterClass = filterClass;
        return this;
    }


    public OpenApiConfiguration withJavaProperties(Properties properties) {
        if (properties != null) {
            Map<String, String> map = (Map) properties;
            return withProperties(map);
        }
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


    public enum CONFIG_FILE_FORMAT {PROPERTIES, INI, JSON, YAML, XML}
}
