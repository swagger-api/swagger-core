package io.swagger.jaxrs2.integration;

import io.swagger.oas.integration.OpenApiConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import java.net.URL;
import java.util.Map;

public class XmlWebOpenApiContext<T extends XmlWebOpenApiContext<T>> extends JaxrsOpenApiContext<T> implements WebOpenApiContext {

    private ServletContext servletContext;
    private ServletConfig servletConfig;

    Logger LOGGER = LoggerFactory.getLogger(XmlWebOpenApiContext.class);

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public T servletConfig(ServletConfig servletConfig) {

        if (servletConfig == null) return (T)this;
        this.servletConfig = servletConfig;
        this.servletContext = servletConfig.getServletContext();
        id(OPENAPI_CONTEXT_ID_PREFIX + "servlet." + servletConfig.getServletName());
        return (T)this;
    }

    // TODO DRAFT
    @Override
    protected URL locateConfig() {

        if (StringUtils.isNotEmpty(configLocation)) {
            return super.locateConfig();
        }
        if (servletConfig != null) {
            String location = ContextUtils.getInitParam(servletConfig, ContextUtils.OPENAPI_CONFIGURATION_LOCATION_KEY);
            if (!StringUtils.isBlank(location)) {
                // TODO..
                this.configLocation = location;
                return buildConfigLocationURL(location);
            }
        }
        // check known locations
        //  /WEB-INF/openApi/openApiconfig.properties
        //  /WEB-INF/openApi/openApiconfig.json
        //  /WEB-INF/openApi/openApiconfig.yaml
        //  /WEB-INF/openApi/openApiconfig...

        //

        // super locate at the end
        return super.locateConfig();

        //return OpenApiConfiguration.fromUri(location, "props");

    }

    @Override
    protected Map<String, OpenApiConfiguration> loadConfigurations() {
        if (StringUtils.isNotEmpty(configLocation)) {
            return new ServletOpenApiConfigBuilder()
                    .servletConfig(servletConfig)
                    .configLocation(locateConfig())
                    .buildMultiple(id);
        }
        // TODO check known location in classpath, or same dir or whatever..
        return null;
    }

}
