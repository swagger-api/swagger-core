package io.swagger.jaxrs.config;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

public class DefaultJaxrsConfig extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
        super.init(servletConfig);

        servletConfig.getServletContext().setAttribute("reader", new WebXMLReader(servletConfig));
        servletConfig.getServletContext().setAttribute("scanner", new DefaultJaxrsScanner());
        ReaderConfigUtils.initReaderConfig(servletConfig);
    }
}
