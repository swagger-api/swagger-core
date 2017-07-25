package io.swagger.oas.integration;

import org.apache.commons.lang3.StringUtils;

public class GenericOpenApiContextBuilder<T extends GenericOpenApiContextBuilder> implements OpenApiContextBuilder {

    protected String ctxId;

    protected String configLocation;
    protected String resourcePackageNames;
    protected OpenApiConfiguration openApiConfiguration;


    @Override
    public OpenApiContext buildContext(boolean init) {
        if (StringUtils.isBlank(ctxId)) {
            ctxId = OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT;
        }

        OpenApiContext ctx = OpenApiContextLocator.getInstance().getOpenApiContext(ctxId);

        if (ctx == null) {
            OpenApiContext rootCtx = OpenApiContextLocator.getInstance().getOpenApiContext(OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT);
            ctx = new GenericOpenApiContext()
                    .openApiConfiguration(openApiConfiguration)
                    .parent(rootCtx);

            if (ctx.getConfigLocation() == null && configLocation != null) {
                ((GenericOpenApiContext)ctx).configLocation(configLocation);
            }
/*
                if (basePath != null) {
                    ((XmlWebOpenApiContext)ctx).basePath(basePath);
                }
*/
            if (((GenericOpenApiContext)ctx).getResourcePackageNames() == null && resourcePackageNames != null) {
                ((GenericOpenApiContext)ctx).resourcePackageNames(resourcePackageNames);
            }
            if (init) {
                ctx.init(); // includes registering itself with OpenApiContextLocator
            }
/*
            } else {
                OpenApiContextLocator.getInstance().putOpenApiContext(ctxId, ctx);
            }
*/
        }
        return ctx;
    }

    public String getCtxId() {
        return ctxId;
    }

    public void setCtxId(String ctxId) {
        this.ctxId = ctxId;
    }

    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public String getResourcePackageNames() {
        return resourcePackageNames;
    }

    public void setResourcePackageNames(String resourcePackageNames) {
        this.resourcePackageNames = resourcePackageNames;
    }

    public OpenApiConfiguration getOpenApiConfiguration() {
        return openApiConfiguration;
    }

    public void setOpenApiConfiguration(OpenApiConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
    }


    public T ctxId(String ctxId) {
        this.ctxId = ctxId;
        return (T) this;
    }

    public T configLocation(String configLocation) {
        this.configLocation = configLocation;
        return (T) this;
    }

    public T resourcePackageNames(String resourcePackageNames) {
        this.resourcePackageNames = resourcePackageNames;
        return (T) this;
    }

    public T openApiConfiguration(OpenApiConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
        return (T) this;
    }

}
