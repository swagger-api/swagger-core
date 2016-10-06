package io.swagger.jaxrs.config;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

public class DefaultJaxrsConfig extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
        super.init(servletConfig);
        String basePath = null;
        if (SwaggerContextService.isServletConfigAvailable(servletConfig)) {
            basePath = servletConfig.getInitParameter("swagger.api.basepath");
            if (basePath != null) {
                String[] parts = basePath.split("://");
                if (parts.length > 1) {
                    int pos = parts[1].indexOf("/");
                    if (pos >= 0) {
                        basePath = parts[1].substring(pos);
                    } else {
                        basePath = null;
                    }
                }
            }

        }

        new SwaggerContextService().withServletConfig(servletConfig).withBasePath(basePath).initConfig().initScanner();
        ReaderConfigUtils.initReaderConfig(servletConfig);
    }

}
