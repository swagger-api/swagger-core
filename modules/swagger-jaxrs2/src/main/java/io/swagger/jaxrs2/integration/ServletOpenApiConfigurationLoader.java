package io.swagger.jaxrs2.integration;

import io.swagger.oas.integration.impl.ClasspathOpenApiConfigurationLoader;
import io.swagger.oas.integration.OpenAPIConfiguration;
import io.swagger.oas.integration.OpenAPIConfigurationBuilder;
import io.swagger.oas.integration.ext.OpenApiConfigurationLoader;
import io.swagger.oas.integration.impl.FileOpenApiConfigurationLoader;
import io.swagger.oas.integration.impl.SwaggerConfiguration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import java.io.IOException;

import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_CACHE_TTL_KEY;
import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_FILTER_KEY;
import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_PRETTYPRINT_KEY;
import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_READER_KEY;
import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_READALLRESOURCES_KEY;
import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_SCANNER_KEY;
import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.OPENAPI_CONFIGURATION_BUILDER_KEY;
import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.getBooleanInitParam;
import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.getInitParam;
import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.getLongInitParam;
import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.resolveResourceClasses;
import static io.swagger.jaxrs2.integration.ServletConfigContextUtils.resolveResourcePackages;

public class ServletOpenApiConfigurationLoader implements OpenApiConfigurationLoader {

    private static Logger LOGGER = LoggerFactory.getLogger(ServletOpenApiConfigurationLoader.class);

    private ServletConfig servletConfig;

    private FileOpenApiConfigurationLoader fileOpenApiConfigurationLoader = new FileOpenApiConfigurationLoader();
    private ClasspathOpenApiConfigurationLoader classpathOpenApiConfigurationLoader = new ClasspathOpenApiConfigurationLoader();

    public ServletOpenApiConfigurationLoader(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    public OpenAPIConfiguration load(String path)  throws IOException {
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
                    .readerClass(getInitParam(servletConfig, OPENAPI_CONFIGURATION_READER_KEY))
                    .cacheTTL(getLongInitParam(servletConfig, OPENAPI_CONFIGURATION_CACHE_TTL_KEY))
                    .scannerClass(getInitParam(servletConfig, OPENAPI_CONFIGURATION_SCANNER_KEY));

            return configuration;

        }
        String location = ServletConfigContextUtils.getInitParam(servletConfig, path);
        if (!StringUtils.isBlank(location)) {
            if (classpathOpenApiConfigurationLoader.exists(location)) {
                return classpathOpenApiConfigurationLoader.load(location);
            }else if (fileOpenApiConfigurationLoader.exists(location)) {
                return fileOpenApiConfigurationLoader.load(location);
            }
        }


        String builderClassName = getInitParam(servletConfig, OPENAPI_CONFIGURATION_BUILDER_KEY);
        if (StringUtils.isNotBlank(builderClassName)) {
            try {
                Class cls = getClass().getClassLoader().loadClass(builderClassName);
                // TODO instantiate with configuration
                OpenAPIConfigurationBuilder builder = (OpenAPIConfigurationBuilder) cls.newInstance();
                return builder.build(null);
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
