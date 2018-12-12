package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.oas.integration.StringOpenApiConfigurationLoader;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import java.io.IOException;

public class ServletPathConfigurationLoader implements StringOpenApiConfigurationLoader {

    private static Logger LOGGER = LoggerFactory.getLogger(ServletPathConfigurationLoader.class);

    private ServletConfig servletConfig;

    public ServletPathConfigurationLoader(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    public OpenAPIConfiguration load(String path) throws IOException {
        if (servletConfig == null) {
            return null;
        }
        if (StringUtils.isBlank(path)) {
            return null;
        }
        String sanitized = (path.startsWith("/") ? path : "/" + path);
        String configString = readInputStreamToString(servletConfig.getServletContext().getResourceAsStream(sanitized));
        return deserializeConfig(path, configString);
    }

    @Override
    public boolean exists(String path) {

        if (servletConfig == null) {
            return false;
        }
        if (StringUtils.isBlank(path)) {
            return false;
        }
        String sanitized = (path.startsWith("/") ? path : "/" + path);
        return servletConfig.getServletContext().getResourceAsStream(sanitized) != null;
    }
}
