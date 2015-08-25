package io.swagger.jaxrs.config;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import io.swagger.config.SwaggerConfigMap;

public class DefaultJaxrsConfig extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
        super.init(servletConfig);

        String swaggerId = servletConfig.getInitParameter("swagger.id");
        if (swaggerId == null) {
            swaggerId = "default";
        }

        SwaggerConfigMap.storeConfig(swaggerId, new WebXMLReader(servletConfig));
        ReaderConfigUtils.initReaderConfig(servletConfig);
    }
}
