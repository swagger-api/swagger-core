package io.swagger.oas.integration;


import com.google.common.base.Objects;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.info.Info;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GenericOpenApiProcessor implements OpenApiProcessor {

    protected String id = OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT;
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

    public GenericOpenApiProcessor cacheTTL(long cacheTTL) {
        this.cacheTTL = cacheTTL;
        return this;
    }



    public OpenApiReader getOpenApiReader() {
        return openApiReader;
    }

    public void setOpenApiReader(OpenApiReader openApiReader) {
        this.openApiReader = openApiReader;
    }

    public OpenApiScanner getOpenApiScanner() {
        return openApiScanner;
    }

    public void setOpenApiScanner(OpenApiScanner openApiScanner) {
        this.openApiScanner = openApiScanner;
    }

    @Override
    public OpenApiConfiguration getOpenApiConfiguration() {
        return openApiConfiguration;
    }

    public void setOpenApiConfiguration(OpenApiConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
    }

    private OpenApiConfiguration openApiConfiguration = new OpenApiConfiguration();

    // TODO used to be basePath, use urls from servers as in comment in configuration
    @Override
    public String getId() {
        return this.id;
/*
        if (openApiConfiguration !=  null) {
            if (openApiConfiguration.getOpenApi().getInfo() != null &&
                    !StringUtils.isEmpty(openApiConfiguration.getOpenApi().getInfo().getTitle())){
                return openApiConfiguration.getOpenApi().getInfo().getTitle();
            }
        }
        return null;
*/
    }

    public void setId (String id) {
  /*      if (openApiConfiguration !=  null) {
            openApiConfiguration.getOpenApi().setInfo(
                    (openApiConfiguration.getOpenApi().getInfo() == null ?
                            new Info() :
                            openApiConfiguration.getOpenApi().getInfo()).title(id)
            );
        }*/
        this.id = id;
    }
    public GenericOpenApiProcessor withId (String id) {
        setId(id);
        return this;
    }

    public final GenericOpenApiProcessor withOpenApiReader(OpenApiReader openApiReader) {
        this.openApiReader = openApiReader;
        return this;
    }

    public final GenericOpenApiProcessor withOpenApiScanner(OpenApiScanner openApiScanner) {
        this.openApiScanner = openApiScanner;
        return this;
    }

    public final GenericOpenApiProcessor withOpenApiConfiguration(OpenApiConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
        return this;
    }

    public final GenericOpenApiProcessor withOpenApiConfigurationFromMap(Map<String, String> properties) {
        this.openApiConfiguration = new OpenApiConfiguration().withProperties(properties);
        return this;
    }
    @Override
    public OpenApiProcessor init() {

        if (openApiConfiguration == null) {
            openApiConfiguration = new OpenApiConfiguration();
        }
        if (openApiScanner == null) {
            openApiScanner = new GenericOpenApiScanner(openApiConfiguration);
        }
        if (openApiReader == null) {
            // TODO use a real generic openApi reader only reading openApi annotations..
            openApiReader = new OpenApiReader() {
                @Override
                public OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources) {
                    OpenAPI openApi = openApiConfiguration.getOpenApi();
                    return openApi;

                }
            };
        }
        return this;
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
