package io.swagger.oas.integration;


import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.web.OpenApiReader;
import io.swagger.oas.web.OpenApiScanner;

import java.util.Map;
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
            if (openApiConfiguration.getOpenAPI().getInfo() != null &&
                    !StringUtils.isEmpty(openApiConfiguration.getOpenAPI().getInfo().getTitle())){
                return openApiConfiguration.getOpenAPI().getInfo().getTitle();
            }
        }
        return null;
*/
    }

    public void setId (String id) {
  /*      if (openApiConfiguration !=  null) {
            openApiConfiguration.getOpenAPI().setInfo(
                    (openApiConfiguration.getOpenAPI().getInfo() == null ?
                            new Info() :
                            openApiConfiguration.getOpenAPI().getInfo()).title(id)
            );
        }*/
        this.id = id;
    }
    public GenericOpenApiProcessor id (String id) {
        setId(id);
        return this;
    }

    public final GenericOpenApiProcessor withOpenApiReader(OpenApiReader openApiReader) {
        this.openApiReader = openApiReader;
        return this;
    }

    public final GenericOpenApiProcessor openApiScanner(OpenApiScanner openApiScanner) {
        this.openApiScanner = openApiScanner;
        return this;
    }

    public final GenericOpenApiProcessor openApiConfiguration(OpenApiConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
        return this;
    }

    @Override
    public OpenApiProcessor init() {

        if (openApiConfiguration == null) {
            openApiConfiguration = new OpenApiConfiguration();
        }
        // TODO handle exceptions
        try {
            if (openApiScanner == null) {
                openApiScanner = new GenericOpenApiContext().buildScanner(openApiConfiguration);
            }
            if (openApiReader == null) {
                // TODO use a real generic openApi reader only reading openApi annotations..
                openApiReader = new GenericOpenApiContext().buildReader(openApiConfiguration);
            }
        } catch(Exception e){
            // TODO
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
