package io.swagger.oas.integration;

import io.swagger.oas.web.OpenAPIConfig;
import io.swagger.oas.web.OpenAPIConfigBuilder;

import java.io.IOException;
import java.util.ServiceLoader;

// TODO doesn't support multiple configs
public class ServiceOpenApiConfigurationLoader implements OpenApiConfigurationLoader {

    @Override
    public OpenApiConfiguration load(String path)  throws IOException {

        ServiceLoader<OpenAPIConfigBuilder> loader = ServiceLoader.load(OpenAPIConfigBuilder.class);
        if (loader.iterator().hasNext()) {
            return cloneFromInterface(loader.iterator().next().build());
        }
        throw new IOException("Error loading OpenAPIConfigBuilder service implementation.");
    }

    @Override
    public boolean exists(String path) {

        try {
            ServiceLoader<OpenAPIConfigBuilder> loader = ServiceLoader.load(OpenAPIConfigBuilder.class);
            if (loader.iterator().hasNext()) {
                loader.iterator().next();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private OpenApiConfiguration cloneFromInterface(OpenAPIConfig configInterface) {

        return new OpenApiConfiguration()
                .openApi(configInterface.getOpenAPI())
                .userDefinedOptions(configInterface.getUserDefinedOptions())
                .filterClass(configInterface.getFilterClass())
                .prettyPrint(configInterface.isPrettyPrint())
                .readerClass(configInterface.getReaderClass())
                .resourcePackages(configInterface.getResourcePackages())
                .resourceClasses(configInterface.getResourceClasses())
                .scanAllResources(configInterface.isScanAllResources())
                .scannerClass(configInterface.getScannerClass());
    }
}
