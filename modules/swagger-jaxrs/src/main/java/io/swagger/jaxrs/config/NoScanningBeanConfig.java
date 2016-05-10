package io.swagger.jaxrs.config;

import io.swagger.config.FilterFactory;
import io.swagger.config.Scanner;
import io.swagger.config.SwaggerConfig;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.jaxrs.Reader;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import java.util.Set;

public abstract class NoScanningBeanConfig extends AbstractScanner implements Scanner, SwaggerConfig {
    Logger LOGGER = LoggerFactory.getLogger(NoScanningBeanConfig.class);

    Reader reader = new Reader(new Swagger());

    ServletConfig servletConfig;

    String resourcePackage;
    String[] schemes;
    String title;
    String version;
    String description;
    String termsOfServiceUrl;
    String contact;
    String license;
    String licenseUrl;
    String filterClass;

    Info info;
    String host;
    String basePath;

    String scannerId;
    String configId;
    String contextId;

    public String getResourcePackage() {
        return this.resourcePackage;
    }

    public void setResourcePackage(String resourcePackage) {
        this.resourcePackage = resourcePackage;
    }

    public String[] getSchemes() {
        return schemes;
    }

    public void setSchemes(String[] schemes) {
        this.schemes = schemes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getScannerId() {
        return scannerId;
    }

    public void setScannerId(String scannerId) {
        this.scannerId = scannerId;
    }

    public String getConfigId() {
        return configId;
    }

    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        if (!"".equals(basePath) && basePath != null) {
            if (!basePath.startsWith("/")) {
                this.basePath = "/" + basePath;
            } else {
                this.basePath = basePath;
            }
        }
    }

    public void setPrettyPrint(String prettyPrint) {
        if (prettyPrint != null) {
            this.prettyPrint = Boolean.parseBoolean(prettyPrint);
        }
    }

    public boolean getScan() {
        return true;
    }

    public void setScan(boolean shouldScan) {
        scanAndRead();
        new SwaggerContextService()
                .withConfigId(configId)
                .withScannerId(scannerId)
                .withContextId(contextId)
                .withServletConfig(servletConfig)
                .withSwaggerConfig(this)
                .withScanner(this)
                .initConfig()
                .initScanner();
    }

    public void setScan() {
        setScan(true);
    }

    public void scanAndRead() {
        Set<Class<?>> classes = classes();
        if (classes != null) {
            Swagger swagger = reader.read(classes);
            if (StringUtils.isNotBlank(host)) {
                swagger.setHost(host);
            }

            if (StringUtils.isNotBlank(basePath)) {
                swagger.setBasePath(basePath);
            }

            updateInfoFromConfig();
        }
    }

    private void updateInfoFromConfig() {
        info = getSwagger().getInfo();
        if (info == null) {
            info = new Info();
        }

        if (StringUtils.isNotBlank(description)) {
            info.description(description);
        }

        if (StringUtils.isNotBlank(title)) {
            info.title(title);
        }

        if (StringUtils.isNotBlank(version)) {
            info.version(version);
        }

        if (StringUtils.isNotBlank(termsOfServiceUrl)) {
            info.termsOfService(termsOfServiceUrl);
        }

        if (contact != null) {
            this.info.contact(new Contact()
                    .name(contact));
        }
        if (license != null && licenseUrl != null) {
            this.info.license(new License()
                    .name(license)
                    .url(licenseUrl));
        }
        if (schemes != null) {
            for (String scheme : schemes) {
                reader.getSwagger().scheme(Scheme.forValue(scheme));
            }
        }

        reader.getSwagger().setInfo(info);
    }

    public Swagger getSwagger() {
        return reader.getSwagger();
    }

    public Swagger configure(Swagger swagger) {
        if (schemes != null) {
            for (String scheme : schemes) {
                swagger.scheme(Scheme.forValue(scheme));
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
        return swagger.info(info)
                .host(host)
                .basePath(basePath);
    }
}
