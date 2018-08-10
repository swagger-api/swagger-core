package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.OpenApiContextLocator;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;

public class JaxrsOpenApiContextBuilder<T extends JaxrsOpenApiContextBuilder> extends GenericOpenApiContextBuilder<JaxrsOpenApiContextBuilder> {

    protected Application application;
    protected ServletConfig servletConfig;

    @Override
    public OpenApiContext buildContext(boolean init) throws OpenApiConfigurationException {
        if (StringUtils.isBlank(ctxId)) {
            ctxId = OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT;
        }

        OpenApiContext ctx = OpenApiContextLocator.getInstance().getOpenApiContext(ctxId);

        if (ctx == null) {
            OpenApiContext rootCtx = OpenApiContextLocator.getInstance().getOpenApiContext(OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT);
            ctx = new XmlWebOpenApiContext()
                    .servletConfig(servletConfig)
                    .app(application)
                    .openApiConfiguration(openApiConfiguration)
                    .parent(rootCtx);

            if (ctx.getConfigLocation() == null && configLocation != null) {
                ((XmlWebOpenApiContext) ctx).configLocation(configLocation);
            }
            if (((XmlWebOpenApiContext) ctx).getResourcePackages() == null && resourcePackages != null) {
                ((XmlWebOpenApiContext) ctx).resourcePackages(resourcePackages);
            }
            if (((XmlWebOpenApiContext) ctx).getResourceClasses() == null && resourceClasses != null) {
                ((XmlWebOpenApiContext) ctx).resourceClasses(resourceClasses);
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

    public void setApplication(Application application) {
        this.application = application;
    }

    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    public T application(Application application) {
        this.application = application;
        return (T) this;
    }

    public T servletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
        return (T) this;
    }
}
