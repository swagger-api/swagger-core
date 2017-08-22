package io.swagger.jaxrs2.integration;

import io.swagger.jaxrs2.integration.api.WebOpenApiContext;
import io.swagger.oas.integration.api.OpenApiConfigurationLoader;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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

        if (!ServletConfigContextUtils.isServletConfigAvailable(servletConfig)) return (T)this;
        this.servletConfig = servletConfig;
        this.servletContext = servletConfig.getServletContext();
        id(OPENAPI_CONTEXT_ID_PREFIX + "servlet." + servletConfig.getServletName());
        return (T)this;
    }


    @Override
    protected List<ImmutablePair<String, String>> getKnownLocations() {

        List<ImmutablePair<String, String>> locations = new LinkedList<>(Arrays.asList(
                new ImmutablePair<>("servlet", ServletConfigContextUtils.OPENAPI_CONFIGURATION_LOCATION_KEY),
                new ImmutablePair<>("file", "WEBINF/openapi-configuration.yaml"),
                new ImmutablePair<>("file", "WEBINF/openapi-configuration.json")
        ));
        locations.addAll(super.getKnownLocations());
        locations.add(new ImmutablePair<>("servlet", ""));  // get config from init params
        return locations;
    }

    @Override
    protected Map<String, OpenApiConfigurationLoader> getLocationLoaders() {
        Map<String, OpenApiConfigurationLoader> map = super.getLocationLoaders();
        map.put("servlet", new ServletOpenApiConfigurationLoader(servletConfig));
        return map;
    }

}
