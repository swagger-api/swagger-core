package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.oas.integration.ClasspathOpenApiConfigurationLoader;
import io.swagger.v3.oas.integration.FileOpenApiConfigurationLoader;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfigBuilder;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiConfigurationLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import java.io.IOException;

import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_BUILDER_KEY;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_CACHE_TTL_KEY;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_FILTER_KEY;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_OBJECT_MAPPER_PROCESSOR_KEY;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_OPENAPI_31_KEY;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_PRETTYPRINT_KEY;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_READALLRESOURCES_KEY;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_READER_KEY;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_SCANNER_KEY;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_SORTOUTPUT_KEY;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_ALWAYSRESOLVEAPPPATH_KEY;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.getBooleanInitParam;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.getInitParam;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.getLongInitParam;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.resolveModelConverterClasses;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.resolveResourceClasses;
import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.resolveResourcePackages;

public class ServletOpenApiConfigurationLoader implements OpenApiConfigurationLoader {

    private static Logger LOGGER = LoggerFactory.getLogger(ServletOpenApiConfigurationLoader.class);

    private ServletConfig servletConfig;

    private FileOpenApiConfigurationLoader fileOpenApiConfigurationLoader = new FileOpenApiConfigurationLoader();
    private ClasspathOpenApiConfigurationLoader classpathOpenApiConfigurationLoader = new ClasspathOpenApiConfigurationLoader();

    public ServletOpenApiConfigurationLoader(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    public OpenAPIConfiguration load(String path) throws IOException {
        if (servletConfig == null) {
            return null;
        }
        if (StringUtils.isBlank(path)) { // we want to resolve from servlet params
            SwaggerConfiguration configuration = new SwaggerConfiguration()
                    .resourcePackages(resolveResourcePackages(servletConfig))
                    .filterClass(getInitParam(servletConfig, OPENAPI_CONFIGURATION_FILTER_KEY))
                    .resourceClasses(resolveResourceClasses(servletConfig))
                    .readAllResources(getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_READALLRESOURCES_KEY))
                    .prettyPrint(getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_PRETTYPRINT_KEY))
                    .sortOutput(getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_SORTOUTPUT_KEY))
                    .alwaysResolveAppPath(getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_ALWAYSRESOLVEAPPPATH_KEY))
                    .readerClass(getInitParam(servletConfig, OPENAPI_CONFIGURATION_READER_KEY))
                    .cacheTTL(getLongInitParam(servletConfig, OPENAPI_CONFIGURATION_CACHE_TTL_KEY))
                    .scannerClass(getInitParam(servletConfig, OPENAPI_CONFIGURATION_SCANNER_KEY))
                    .objectMapperProcessorClass(getInitParam(servletConfig, OPENAPI_CONFIGURATION_OBJECT_MAPPER_PROCESSOR_KEY))
                    .openAPI31(getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_OPENAPI_31_KEY))
                    .modelConverterClasses(resolveModelConverterClasses(servletConfig));

            return configuration;

        }
        String location = ServletConfigContextUtils.getInitParam(servletConfig, path);
        if (!StringUtils.isBlank(location)) {
            if (classpathOpenApiConfigurationLoader.exists(location)) {
                return classpathOpenApiConfigurationLoader.load(location);
            } else if (fileOpenApiConfigurationLoader.exists(location)) {
                return fileOpenApiConfigurationLoader.load(location);
            }
        }

        String builderClassName = getInitParam(servletConfig, OPENAPI_CONFIGURATION_BUILDER_KEY);
        if (StringUtils.isNotBlank(builderClassName)) {
            try {
                Class cls = getClass().getClassLoader().loadClass(builderClassName);
                // TODO instantiate with configuration
                OpenAPIConfigBuilder builder = (OpenAPIConfigBuilder) cls.newInstance();
                return builder.build();
            } catch (Exception e) {
                LOGGER.error("error loading builder: " + e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public boolean exists(String path) {

        if (servletConfig == null) {
            return false;
        }
        if (StringUtils.isBlank(path)) {
            if (resolveResourcePackages(servletConfig) != null) {
                return true;
            }
            if (getInitParam(servletConfig, OPENAPI_CONFIGURATION_FILTER_KEY) != null) {
                return true;
            }
            if (resolveResourceClasses(servletConfig) != null) {
                return true;
            }
            if (getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_READALLRESOURCES_KEY) != null) {
                return true;
            }
            if (getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_PRETTYPRINT_KEY) != null) {
                return true;
            }
            if (getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_SORTOUTPUT_KEY) != null) {
                return true;
            }
            if (getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_ALWAYSRESOLVEAPPPATH_KEY) != null) {
                return true;
            }
            if (getInitParam(servletConfig, OPENAPI_CONFIGURATION_READER_KEY) != null) {
                return true;
            }
            if (getLongInitParam(servletConfig, OPENAPI_CONFIGURATION_CACHE_TTL_KEY) != null) {
                return true;
            }
            if (getInitParam(servletConfig, OPENAPI_CONFIGURATION_SCANNER_KEY) != null) {
                return true;
            }
            if (getInitParam(servletConfig, OPENAPI_CONFIGURATION_OBJECT_MAPPER_PROCESSOR_KEY) != null) {
                return true;
            }
            if (getInitParam(servletConfig, OPENAPI_CONFIGURATION_OPENAPI_31_KEY) != null) {
                return true;
            }
            return resolveModelConverterClasses(servletConfig) != null;
        }
        String location = ServletConfigContextUtils.getInitParam(servletConfig, path);
        if (!StringUtils.isBlank(location)) {
            if (classpathOpenApiConfigurationLoader.exists(location)) {
                return true;
            }
            return fileOpenApiConfigurationLoader.exists(location);
        }
        return false;
    }
}
