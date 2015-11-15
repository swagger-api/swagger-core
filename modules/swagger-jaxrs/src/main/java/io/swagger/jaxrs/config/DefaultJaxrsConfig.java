package io.swagger.jaxrs.config;

import io.swagger.config.SwaggerConfig;
import io.swagger.util.PathUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultJaxrsConfig extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
        super.init(servletConfig);

        createReader(servletConfig);
        servletConfig.getServletContext().setAttribute("scanner", new DefaultJaxrsScanner());
        ReaderConfigUtils.initReaderConfig(servletConfig);
    }

    private void createReader(ServletConfig servletConfig) {
        final String swaggerBasePath = servletConfig.getInitParameter("swagger.api.basepath");
        final ServletContext context = servletConfig.getServletContext();
        if (swaggerBasePath != null) {
            final PathUtils.HostAndPath hostAndPath = PathUtils.parseSwaggerPath(swaggerBasePath);
            @SuppressWarnings("unchecked")
            Map<String, SwaggerConfig> readers = (Map<String, SwaggerConfig>) context.getAttribute("readers");
            if (readers == null) {
                readers = new ConcurrentHashMap<String, SwaggerConfig>();
                context.setAttribute("readers", readers);
            }
            readers.put(hostAndPath.getHost() + hostAndPath.getBasePath(), new WebXMLReader(servletConfig));
        } else {
            context.setAttribute("reader", new WebXMLReader(servletConfig));
        }
    }
}
