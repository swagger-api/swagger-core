package io.swagger.jersey.config;

import javax.servlet.ServletConfig;

import io.swagger.jaxrs.config.DefaultJaxrsConfig;

public class JerseyJaxrsConfig extends DefaultJaxrsConfig {
    @Override
    public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
        super.init(servletConfig);
    }
}
