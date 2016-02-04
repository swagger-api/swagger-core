package io.swagger.jaxrs.config;

import io.swagger.config.FilterFactory;
import io.swagger.config.SwaggerConfig;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebXMLReader implements SwaggerConfig {
    protected String basePath,
        host,
        filterClass,
        apiVersion,
        title;
    protected String[] schemes = new String[]{};
    private Logger LOGGER = LoggerFactory.getLogger(WebXMLReader.class);

    private final Map<String, String> otherConfigs = new HashMap<String, String>();

    public WebXMLReader(ServletConfig servletConfig) {
        apiVersion = servletConfig.getInitParameter("api.version");
        if (apiVersion == null) {
            apiVersion = "Swagger Server";
        }

        // we support full base path (i.e full URL to the server) or just base path
        this.host = servletConfig.getInitParameter("swagger.api.host");
        String schemesString = servletConfig.getInitParameter("swagger.api.schemes");

        // split the CSV string and update the `schemes` variable
        if (schemesString != null) {
            String[] parts = schemesString.split(",");
            List<String> schemes = new ArrayList<String>();
            for (String scheme : parts) {
                String s = scheme.trim();
                if (!s.isEmpty()) {
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
            } else {
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

        final Enumeration initParameterNames = servletConfig.getInitParameterNames();
        while (initParameterNames != null && initParameterNames.hasMoreElements()) {
            final String name = String.valueOf(initParameterNames.nextElement());
            // skip already handled params
            if ("api.version".equals(name) || "swagger.api.host".equals(name) || "swagger.api.schemes".equals(name)
                || "swagger.api.title".equals(name) || "swagger.api.basepath".equals(name) || "swagger.filter".equals(name)) {
                continue;
            }

            final String value = servletConfig.getInitParameter(name);
            if (value != null) {
                otherConfigs.put(name, value);
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
            for (String scheme : this.schemes) {
                swagger.scheme(Scheme.forValue(scheme));
            }

            for (final Map.Entry<String, String> entry : otherConfigs.entrySet()) {
                final String key = entry.getKey();
                if (key.startsWith("swagger.info.contact.")) {
                    Contact contact = swagger.getInfo().getContact();
                    if (contact == null) {
                        contact = new Contact();
                        swagger.getInfo().setContact(contact);
                    }
                    setValue(contact, key.substring("swagger.info.contact.".length()), entry.getValue());
                } else if (key.startsWith("swagger.info.license.")) {
                    License license = swagger.getInfo().getLicense();
                    if (license == null) {
                        license = new License();
                        swagger.getInfo().setLicense(license);
                    }
                    setValue(license, key.substring("swagger.info.license.".length()), entry.getValue());
                } else if (key.startsWith("swagger.info.")) {
                    setValue(swagger.getInfo(), key.substring("swagger.info.".length()), entry.getValue());
                }
            }
        }
        return swagger;
    }

    private static void setValue(final Object on, final String method, final String value) {
        try {
            Info.class.getMethod(method, String.class).invoke(on, value);
        } catch (final IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (final InvocationTargetException e) {
            throw new IllegalArgumentException(e.getCause());
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
