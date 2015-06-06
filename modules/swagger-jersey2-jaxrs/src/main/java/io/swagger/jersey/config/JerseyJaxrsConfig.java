package io.swagger.jersey.config;

import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import io.swagger.jaxrs.config.ReaderConfigUtils;
import io.swagger.jaxrs.config.WebXMLReader;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

public class JerseyJaxrsConfig extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
        super.init(servletConfig);

        servletConfig.getServletContext().setAttribute("reader", new WebXMLReader(servletConfig));
        servletConfig.getServletContext().setAttribute("scanner", new DefaultJaxrsScanner());
        ReaderConfigUtils.initReaderConfig(servletConfig);
    }
}
