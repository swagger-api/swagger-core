package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.oas.integration.api.OpenApiContext;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ServletConfigContextUtils {

    public static final String OPENAPI_CONFIGURATION_RESOURCEPACKAGE_KEY = "openApi.configuration.resourcePackages";
    public static final String OPENAPI_CONFIGURATION_LOCATION_KEY = "openApi.configuration.location";
    public static final String JERSEY1_PACKAGE_KEY = "com.sun.jersey.config.property.packages";
    public static final String JERSEY2_PACKAGE_KEY = "jersey.config.server.provider.packages";
    public static final String JERSEY2_CLASSES_KEY = "jersey.config.server.provider.classnames";

    public static final String OPENAPI_CONFIGURATION_READER_KEY = "openApi.configuration.readerClass";
    public static final String OPENAPI_CONFIGURATION_SCANNER_KEY = "openApi.configuration.scannerClass";
    public static final String OPENAPI_CONFIGURATION_BUILDER_KEY = "openApi.configuration.builderClass";
    public static final String OPENAPI_CONFIGURATION_PRETTYPRINT_KEY = "openApi.configuration.prettyPrint";
    public static final String OPENAPI_CONFIGURATION_READALLRESOURCES_KEY = "openApi.configuration.readAllResources";
    public static final String OPENAPI_CONFIGURATION_RESOURCECLASSES_KEY = "openApi.configuration.resourceClasses";
    public static final String OPENAPI_CONFIGURATION_FILTER_KEY = "openApi.configuration.filterClass";
    public static final String OPENAPI_CONFIGURATION_CACHE_TTL_KEY = "openApi.configuration.cacheTTL";

    /**
     * @since 2.1.6
     */
    public static final String OPENAPI_CONFIGURATION_SORTOUTPUT_KEY = "openApi.configuration.sortOutput";

    /**
     * @since 2.1.9
     */
    public static final String OPENAPI_CONFIGURATION_ALWAYSRESOLVEAPPPATH_KEY = "openApi.configuration.alwaysResolveAppPath";

    /**
     * @since 2.0.6
     */
    public static final String OPENAPI_CONFIGURATION_OBJECT_MAPPER_PROCESSOR_KEY = "openApi.configuration.objectMapperProcessorClass";

    /**
     * @since 2.0.6
     */
    public static final String OPENAPI_CONFIGURATION_MODEL_CONVERTERS_KEY = "openApi.configuration.modelConverterClasses";

    /**
     * @since 2.2.0
     */
    public static final String OPENAPI_CONFIGURATION_OPENAPI_31_KEY = "openApi.configuration.openAPI31";

    public static Set<String> resolveResourcePackages(ServletConfig servletConfig) {
        if (!isServletConfigAvailable(servletConfig)) {
            return null;
        }
        String resourcePackage = getInitParam(servletConfig, OPENAPI_CONFIGURATION_RESOURCEPACKAGE_KEY);
        if (resourcePackage == null) {
            // jersey 1
            resourcePackage = getInitParam(servletConfig, JERSEY1_PACKAGE_KEY);
            if (resourcePackage != null) {
                resourcePackage = resourcePackage.replace(';', ',');
            }
        }
        if (resourcePackage == null) {
            // jersey 2
            resourcePackage = getInitParam(servletConfig, JERSEY2_PACKAGE_KEY);
            if (resourcePackage != null) {
                resourcePackage = resourcePackage.replace(';', ',');
            }
        }
        if (StringUtils.isBlank(resourcePackage)) {
            return null;
        }
        return Arrays.stream(resourcePackage.split(",")).collect(Collectors.toSet());

    }

    public static Set<String> resolveResourceClasses(ServletConfig servletConfig) {
        if (!isServletConfigAvailable(servletConfig)) {
            return null;
        }
        String resourceClasses = getInitParam(servletConfig, OPENAPI_CONFIGURATION_RESOURCECLASSES_KEY);
        if (resourceClasses == null) {
            // jersey 2
            resourceClasses = getInitParam(servletConfig, JERSEY2_CLASSES_KEY);
            if (resourceClasses != null) {
                resourceClasses = resourceClasses.replace(';', ',');
            }
        }
        if (StringUtils.isBlank(resourceClasses)) {
            return null;
        }
        return Arrays.stream(resourceClasses.split(",")).collect(Collectors.toSet());

    }

    /**
     * @since 2.0.6
     */
    public static Set<String> resolveModelConverterClasses(ServletConfig servletConfig) {
        if (!isServletConfigAvailable(servletConfig)) {
            return null;
        }
        String modelConverterClasses = getInitParam(servletConfig, OPENAPI_CONFIGURATION_MODEL_CONVERTERS_KEY);
        if (modelConverterClasses != null) {
            modelConverterClasses = modelConverterClasses.replace(';', ',');
        }
        if (StringUtils.isBlank(modelConverterClasses)) {
            return null;
        }
        return new LinkedHashSet<>(Arrays.stream(modelConverterClasses.split(",")).collect(Collectors.toSet()));

    }

    public static String getInitParam(ServletConfig sc, String paramKey) {
        if (!isServletConfigAvailable(sc)) {
            return null;
        }
        return sc.getInitParameter(paramKey);
    }

    public static Boolean getBooleanInitParam(ServletConfig sc, String paramKey) {
        String param = getInitParam(sc, paramKey);
        if (StringUtils.isBlank(param)) {
            return null;
        }
        return Boolean.valueOf(Boolean.parseBoolean(param));
    }

    public static Long getLongInitParam(ServletConfig sc, String paramKey) {
        String param = getInitParam(sc, paramKey);
        if (StringUtils.isBlank(param)) {
            return null;
        }
        try {
            return Long.parseLong(param);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String getContextIdFromServletConfig(ServletConfig config) {

        String ctxId = null;
        if (isServletConfigAvailable(config)) {
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

    public static boolean isServletConfigAvailable(ServletConfig sc) {
        if (sc == null) {
            return false;
        }
        try {
            sc.getInitParameter("test");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
