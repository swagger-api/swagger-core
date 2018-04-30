package io.swagger.v3.oas.integration;

import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.integration.api.OpenApiContextBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class GenericOpenApiContextBuilder<T extends GenericOpenApiContextBuilder> implements OpenApiContextBuilder {

    protected String ctxId;

    protected String configLocation;
    protected Set<String> resourcePackages;
    protected Set<String> resourceClasses;
    protected OpenAPIConfiguration openApiConfiguration;

    @Override
    public OpenApiContext buildContext(boolean init) throws OpenApiConfigurationException {
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
                ((GenericOpenApiContext) ctx).configLocation(configLocation);
            }
            if (((GenericOpenApiContext) ctx).getResourcePackages() == null && resourcePackages != null) {
                ((GenericOpenApiContext) ctx).resourcePackages(resourcePackages);
            }
            if (((GenericOpenApiContext) ctx).getResourceClasses() == null && resourceClasses != null) {
                ((GenericOpenApiContext) ctx).resourceClasses(resourceClasses);
            }
            if (init) {
                ctx.init(); // includes registering itself with OpenApiContextLocator
            }
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

    public Set<String> getResourcePackages() {
        return resourcePackages;
    }

    public void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
    }

    public OpenAPIConfiguration getOpenApiConfiguration() {
        return openApiConfiguration;
    }

    public void setOpenApiConfiguration(OpenAPIConfiguration openApiConfiguration) {
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

    public T resourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
        return (T) this;
    }

    public T openApiConfiguration(OpenAPIConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
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

}
