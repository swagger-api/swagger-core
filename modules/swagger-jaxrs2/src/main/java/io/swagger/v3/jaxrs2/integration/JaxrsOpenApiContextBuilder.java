package io.swagger.v3.jaxrs2.integration;

import javax.ws.rs.core.Application;

import org.apache.commons.lang3.StringUtils;

import io.swagger.v3.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.OpenApiContextLocator;
import io.swagger.v3.oas.integration.api.OpenApiContext;

public class JaxrsOpenApiContextBuilder<T extends JaxrsOpenApiContextBuilder> extends GenericOpenApiContextBuilder<JaxrsOpenApiContextBuilder> {

    protected Application application;

    @Override
    public OpenApiContext buildContext(final boolean init) throws OpenApiConfigurationException {
        if (StringUtils.isBlank(ctxId)) {
            ctxId = OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT;
        }

        OpenApiContext ctx = OpenApiContextLocator.getInstance().getOpenApiContext(ctxId);

        if (ctx == null) {
            final OpenApiContext rootCtx = OpenApiContextLocator.getInstance().getOpenApiContext(OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT);
            ctx = new WebOpenApiContext()
                    .app(application)
                    .openApiConfiguration(openApiConfiguration)
                    .id(ctxId)
                    .parent(rootCtx);

            if (ctx.getConfigLocation() == null && configLocation != null) {
                ((WebOpenApiContext) ctx).configLocation(configLocation);
            }
            if (((WebOpenApiContext) ctx).getResourcePackages() == null && resourcePackages != null) {
                ((WebOpenApiContext) ctx).resourcePackages(resourcePackages);
            }
            if (((WebOpenApiContext) ctx).getResourceClasses() == null && resourceClasses != null) {
                ((WebOpenApiContext) ctx).resourceClasses(resourceClasses);
            }
            if (init) {
                ctx.init(); // includes registering itself with OpenApiContextLocator
            }
        }
        return ctx;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(final Application application) {
        this.application = application;
    }

    public T application(final Application application) {
        this.application = application;
        return (T) this;
    }
}
