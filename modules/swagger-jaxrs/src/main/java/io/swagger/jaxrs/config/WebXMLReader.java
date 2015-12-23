package io.swagger.jaxrs.config;

import io.swagger.config.FilterFactory;
import io.swagger.config.Scanner;
import io.swagger.config.ScannerFactory;
import io.swagger.config.SwaggerConfig;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.models.Info;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;

public class WebXMLReader implements SwaggerConfig {
    protected String basePath, host, filterClass, apiVersion, title, scheme = "http";
    private Logger LOGGER = LoggerFactory.getLogger(WebXMLReader.class);

    public WebXMLReader(ServletConfig servletConfig) {
        basePath = servletConfig.getInitParameter("swagger.api.basepath");
        title = servletConfig.getInitParameter("swagger.api.title");
        if (title == null) {
            title = "";
        }

        final PathUtils.HostAndPath hostAndPath = PathUtils.parseSwaggerPath(basePath);

        scheme = hostAndPath.getScheme();
        host = hostAndPath.getHost();
        basePath = hostAndPath.getBasePath();

        Scanner scanner = new DefaultJaxrsScanner();
        ScannerFactory.setScanner(scanner, host, basePath);
        apiVersion = servletConfig.getInitParameter("api.version");
        if (apiVersion == null) {
            apiVersion = "Swagger Server";
        }

        String shouldPrettyPrint = servletConfig.getInitParameter("swagger.pretty.print");
        if (shouldPrettyPrint != null) {
            scanner.setPrettyPrint(Boolean.parseBoolean(shouldPrettyPrint));
        }

        filterClass = servletConfig.getInitParameter("swagger.filter");
        if (filterClass != null) {
            try {
                SwaggerSpecFilter filter = (SwaggerSpecFilter) Class.forName(filterClass).newInstance();
                if (filter != null) {
                    FilterFactory.setFilter(host, basePath, filter);
                }
            } catch (Exception e) {
                LOGGER.error("failed to load filter", e);
            }
        }
    }

    public String getFilterClass() {
        return filterClass;
    }

    public Swagger configure(Swagger swagger) {
        if (swagger != null) {
            Info info = swagger.getInfo();
            if (info == null) {
                swagger.info(new Info());
            }

            swagger.basePath(basePath)
                    .host(host)
                    .scheme(Scheme.forValue(scheme))
                    .getInfo()
                    .title(title)
                    .version(apiVersion);
        }
        return swagger;
    }
}
