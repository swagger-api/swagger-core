package io.swagger.jaxrs.config;

import io.swagger.config.FilterFactory;
import io.swagger.config.Scanner;
import io.swagger.config.ScannerFactory;
import io.swagger.config.SwaggerConfig;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.models.Info;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import java.util.ArrayList;
import java.util.List;

public class WebXMLReader implements SwaggerConfig {
    protected String basePath,
            host,
            filterClass,
            apiVersion,
            title;
    protected String[] schemes = new String[]{};
    private Logger LOGGER = LoggerFactory.getLogger(WebXMLReader.class);

    public WebXMLReader(ServletConfig servletConfig) {
        Scanner scanner = new DefaultJaxrsScanner();
        ScannerFactory.setScanner(scanner);
        apiVersion = servletConfig.getInitParameter("api.version");
        if (apiVersion == null) {
            apiVersion = "Swagger Server";
        }

        String shouldPrettyPrint = servletConfig.getInitParameter("swagger.pretty.print");
        if (shouldPrettyPrint != null) {
            scanner.setPrettyPrint(Boolean.parseBoolean(shouldPrettyPrint));
        }

        // we support full base path (i.e full URL to the server) or just base path
        this.host = servletConfig.getInitParameter("swagger.api.host");
        String schemesString = servletConfig.getInitParameter("swagger.api.schemes");

        // split the CSV string and update the `schemes` variable
        if(schemesString != null) {
            String[] parts = schemesString.split(",");
            List<String> schemes = new ArrayList<String>();
            for(String scheme : parts) {
                String s = scheme.trim();
                if(!s.isEmpty()) {
                    schemes.add(s);
                }
            }
            this.schemes = schemes.toArray(new String[schemes.size()]);
        }
        this.title = servletConfig.getInitParameter("swagger.api.title");

        if (title == null) {
            title = "";
        }

        this.basePath = servletConfig.getInitParameter("swagger.api.basepath");
        if (basePath != null) {
            String[] parts = basePath.split("://");
            if (parts.length > 1) {
                int pos = parts[1].indexOf("/");
                if (pos >= 0) {
                    this.schemes = new String[]{parts[0]};
                    basePath = parts[1].substring(pos);
                    host = parts[1].substring(0, pos);
                } else {
                    this.schemes = new String[]{parts[0]};
                    basePath = null;
                    host = parts[1];
                }
            }
            else {
                // it is a proper basePath, nothing to do
            }
        }

        filterClass = servletConfig.getInitParameter("swagger.filter");
        if (filterClass != null) {
            try {
                SwaggerSpecFilter filter = (SwaggerSpecFilter) Class.forName(filterClass).newInstance();
                if (filter != null) {
                    FilterFactory.setFilter(filter);
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
                    .getInfo()
                    .title(title)
                    .version(apiVersion);
            for(String scheme : this.schemes) {
                swagger.scheme(Scheme.forValue(scheme));
            }
        }
        return swagger;
    }
}
