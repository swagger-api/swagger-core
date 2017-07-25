package io.swagger.jaxrs2.integration;

import io.swagger.oas.integration.LocationOpenApiConfigBuilder;
import io.swagger.oas.integration.OpenApiConfiguration;
import io.swagger.oas.web.OpenAPIConfig;

import javax.servlet.ServletConfig;

import java.util.HashMap;
import java.util.Map;

import static io.swagger.jaxrs2.integration.ContextUtils.*;

public class ServletOpenApiConfigBuilder extends LocationOpenApiConfigBuilder {

    protected ServletConfig servletConfig;


    @Override
    public Map<String, OpenApiConfiguration> buildMultiple(String defaultId) {
        if (configLocation != null) {
            return super.buildMultiple(defaultId);
        }
        if (servletConfig == null) {
            return null;
        }
        try {

            OpenApiConfiguration configuration = new OpenApiConfiguration()
                    .resourcePackageNames(resolveResourcePackage(servletConfig))
                    .filterClassName(getInitParam(servletConfig, OPENAPI_CONFIGURATION_FILTER_KEY))
                    .resourceClassNames(getInitParam(servletConfig, OPENAPI_CONFIGURATION_RESOURCECLASSES_KEY))
                    .scanAllResources(getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_SCANALLRESOURCES_KEY))
                    .prettyPrint(getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_PRETTYPRINT_KEY))
                    .processorClassName(getInitParam(servletConfig, OPENAPI_CONFIGURATION_PROCESSOR_KEY))
                    .readerClassName(getInitParam(servletConfig, OPENAPI_CONFIGURATION_READER_KEY))
                    .scannerClassName(getInitParam(servletConfig, OPENAPI_CONFIGURATION_SCANNER_KEY));
            Map<String, OpenApiConfiguration> map = new HashMap<>();
            map.put(defaultId, configuration);
            return map;

        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            return null;
            //throw new RuntimeException("exception reading config", e);
        }
    }
    @Override
    public OpenAPIConfig build() {
        if (configLocation != null) {
            return super.build();
        }
        if (servletConfig == null) {
            return null;
        }
        try {

            OpenApiConfiguration configuration = new OpenApiConfiguration()
                    .resourcePackageNames(resolveResourcePackage(servletConfig))
                    .filterClassName(getInitParam(servletConfig, OPENAPI_CONFIGURATION_FILTER_KEY))
                    .resourceClassNames(getInitParam(servletConfig, OPENAPI_CONFIGURATION_RESOURCECLASSES_KEY))
                    .scanAllResources(getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_SCANALLRESOURCES_KEY))
                    .prettyPrint(getBooleanInitParam(servletConfig, OPENAPI_CONFIGURATION_PRETTYPRINT_KEY))
                    .processorClassName(getInitParam(servletConfig, OPENAPI_CONFIGURATION_PROCESSOR_KEY))
                    .readerClassName(getInitParam(servletConfig, OPENAPI_CONFIGURATION_READER_KEY))
                    .scannerClassName(getInitParam(servletConfig, OPENAPI_CONFIGURATION_SCANNER_KEY));
            return configuration;

        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            return null;
            //throw new RuntimeException("exception reading config", e);
        }
    }

    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    public ServletOpenApiConfigBuilder servletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
        return this;
    }
}
