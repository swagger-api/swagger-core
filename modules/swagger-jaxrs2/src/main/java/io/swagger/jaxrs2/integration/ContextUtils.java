package io.swagger.jaxrs2.integration;

import io.swagger.oas.integration.OpenApiConfiguration;
import io.swagger.oas.integration.OpenApiContext;
import io.swagger.oas.integration.OpenApiContextLocator;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;

public class ContextUtils {

    public static final String OPENAPI_CONFIGURATION_RESOURCEPACKAGE_KEY = "openApi.configuration.resourcePackage";
    public static final String OPENAPI_CONFIGURATION_BASEPATH_KEY = "openApi.configuration.basePath";
    public static final String OPENAPI_CONFIGURATION_LOCATION_KEY = "openApi.configuration.location";
    public static final String JERSEY1_PACKAGE_KEY = "com.sun.jersey.config.property.packages";
    public static final String JERSEY2_PACKAGE_KEY = "jersey.config.server.provider.packages";


    public static String resolveResourcePackage (ServletConfig servletConfig) {
        String resourcePackage = getInitParam (servletConfig, OPENAPI_CONFIGURATION_RESOURCEPACKAGE_KEY);
        if (resourcePackage == null) {
            // jersey 1
            resourcePackage = getInitParam (servletConfig, JERSEY1_PACKAGE_KEY);
            if (resourcePackage != null) {
                resourcePackage = resourcePackage.replace(';', ',');
            }
        }
        if (resourcePackage == null) {
            // jersey 2
            resourcePackage = getInitParam (servletConfig, JERSEY2_PACKAGE_KEY);
            if (resourcePackage != null) {
                resourcePackage = resourcePackage.replace(';', ',');
            }
        }
        return resourcePackage;
    }

    public static String getInitParam(ServletConfig sc, String paramKey) {
        return sc.getInitParameter(paramKey) == null?
                sc.getInitParameter(paramKey) :
                sc.getInitParameter(paramKey);
    }

    public static OpenApiContext getOrBuildContext(OpenApiConfiguration openApiConfiguration) {
        return getOrBuildContext(null, null, null, null, null, openApiConfiguration);
    }

    public static OpenApiContext getOrBuildContext(String ctxId, Application app, ServletConfig sc, String configLocation, String resourcePackage, OpenApiConfiguration openApiConfiguration) {
        // TODO USE instead servletContext attribute? or possibly both

        if (StringUtils.isBlank(ctxId)) {
            ctxId = OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT;
        }

        OpenApiContext ctx = OpenApiContextLocator.getInstance().getOpenApiContext(ctxId);

        if (ctx == null) {
            OpenApiContext rootCtx = OpenApiContextLocator.getInstance().getOpenApiContext(OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT);
            ctx = new XmlWebOpenApiContext()
                    .withServletConfig(sc)
                    .withApp(app)
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
            if (resourcePackage != null) {
                ((XmlWebOpenApiContext)ctx).withResourcePackage(resourcePackage);
            }
            ctx.init(); // includes registering itself with OpenApiContextLocator
/*
            } else {
                OpenApiContextLocator.getInstance().putOpenApiContext(ctxId, ctx);
            }
*/
        }
        return ctx;
    }


}
