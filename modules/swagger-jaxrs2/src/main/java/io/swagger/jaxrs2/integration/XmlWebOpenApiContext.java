package io.swagger.jaxrs2.integration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import java.net.URL;

import static io.swagger.jaxrs2.integration.ContextUtils.*;

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

    public T withServletConfig(ServletConfig servletConfig) {

        if (servletConfig == null) return (T)this;
        this.servletConfig = servletConfig;
        this.servletContext = servletConfig.getServletContext();
        withId(OPENAPI_CONTEXT_ID_PREFIX + "servlet." + servletConfig.getServletName());
        String location = getInitParam (servletConfig, OPENAPI_CONFIGURATION_LOCATION_KEY);
        if (location != null) {
            withConfigLocation(location);
        }
        String resourcePackage = resolveResourcePackage(servletConfig);
        if (resourcePackage != null) {
            withResourcePackageNames(resourcePackage);
        }
        String basePath = getInitParam (servletConfig, OPENAPI_CONFIGURATION_BASEPATH_KEY);
        if (basePath != null) {
            withBasePath(basePath);
        }
        return (T)this;
    }

    // TODO DRAFT
    @Override
    protected URL locateConfig() {

        if (StringUtils.isNotEmpty(configLocation)) {
            return super.locateConfig();
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

}
