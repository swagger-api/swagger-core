package io.swagger.jaxrs2.integration;

import io.swagger.oas.integration.OpenApiContext;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;

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

    public static String getContextIdFromServletConfig(ServletConfig config) {

        String ctxId = null;
        if (config != null) {
            ctxId = getInitParam(config, OpenApiContext.OPENAPI_CONTEXT_ID_KEY);
            if (StringUtils.isBlank(ctxId)) {
                ctxId = OpenApiContext.OPENAPI_CONTEXT_ID_PREFIX + "servlet." + config.getServletName();
            }
        }
        if (StringUtils.isBlank(ctxId)) {
            ctxId = OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT;
        }
        return ctxId;
    }

}
