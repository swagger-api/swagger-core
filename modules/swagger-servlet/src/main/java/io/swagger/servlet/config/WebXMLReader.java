package io.swagger.servlet.config;

import io.swagger.config.FilterFactory;
import io.swagger.config.SwaggerConfig;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.models.Info;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;

public class WebXMLReader implements SwaggerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebXMLReader.class);
    private final String filterClass;
    private final String apiVersion;
    private final String title;
    private String basePath;
    private String host;
    private Scheme scheme = Scheme.HTTP;

    public WebXMLReader(ServletConfig servletConfig) {
        apiVersion = getInitParameter(servletConfig, "api.version", "Swagger Server");
        basePath = getInitParameter(servletConfig, "swagger.api.basepath", null);
        title = getInitParameter(servletConfig, "swagger.api.title", "");
        filterClass = getInitParameter(servletConfig, "swagger.filter", null);

        if (basePath != null) {
            final String[] parts = basePath.split("://");
            if (parts.length > 1) {
                scheme = Scheme.forValue(parts[0]);
                final int pos = parts[1].indexOf("/");
                if (pos >= 0) {
                    basePath = parts[1].substring(pos);
                    host = parts[1].substring(0, pos);
                } else {
                    basePath = null;
                    host = parts[1];
                }
            }
        }

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

    private static String getInitParameter(ServletConfig servletConfig, String parameterName, String defaultValue) {
        final String value = servletConfig.getInitParameter(parameterName);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    @Override
    public Swagger configure(Swagger swagger) {
        if (swagger == null) {
            return null;
        }
        if (StringUtils.isNotBlank(basePath)) {
            swagger.basePath(basePath);
        }
        if (StringUtils.isNotBlank(host)) {
            swagger.host(host);
        }
        if (scheme != null) {
            swagger.scheme(scheme);
        }
        Info info = swagger.getInfo();
        if (info == null) {
            info = new Info();
            swagger.info(info);
        }
        if (StringUtils.isNotBlank(title)) {
            info.title(title);
        }
        if (StringUtils.isNotBlank(apiVersion)) {
            info.version(apiVersion);
        }
        return swagger;
    }

    @Override
    public String getFilterClass() {
        return filterClass;
    }
}
