package io.swagger.jaxrs2.integration;

import io.swagger.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.oas.integration.OpenApiContext;
import io.swagger.oas.integration.OpenApiContextLocator;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;

public class JaxrsOpenApiContextBuilder<T extends JaxrsOpenApiContextBuilder> extends GenericOpenApiContextBuilder<JaxrsOpenApiContextBuilder> {

    protected Application application;
    protected ServletConfig servletConfig;

    @Override
    public OpenApiContext buildContext(boolean init) {
        if (StringUtils.isBlank(ctxId)) {
            ctxId = OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT;
        }

        OpenApiContext ctx = OpenApiContextLocator.getInstance().getOpenApiContext(ctxId);

        if (ctx == null) {
            OpenApiContext rootCtx = OpenApiContextLocator.getInstance().getOpenApiContext(OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT);
            ctx = new XmlWebOpenApiContext()
                    .withServletConfig(servletConfig)
                    .withApp(application)
                    .withOpenApiConfiguration(openApiConfiguration)
                    .withParent(rootCtx);

            if (ctx.getConfigLocation() == null && configLocation != null) {
                ((XmlWebOpenApiContext)ctx).withConfigLocation(configLocation);
            }
/*
                if (basePath != null) {
                    ((XmlWebOpenApiContext)ctx).withBasePath(basePath);
                }
*/
            if (((XmlWebOpenApiContext)ctx).getResourcePackage() == null && resourcePackage != null) {
                ((XmlWebOpenApiContext)ctx).withResourcePackage(resourcePackage);
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
