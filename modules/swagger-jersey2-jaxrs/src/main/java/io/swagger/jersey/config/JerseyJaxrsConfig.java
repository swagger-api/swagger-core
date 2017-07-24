package io.swagger.jersey.config;

import io.swagger.jaxrs.config.DefaultJaxrsConfig;
import javax.servlet.ServletConfig;

public class JerseyJaxrsConfig extends DefaultJaxrsConfig {
    @Override
    public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
        super.init(servletConfig);
    }
}
