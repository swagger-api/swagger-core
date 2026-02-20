package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;

import java.util.Map;
import java.util.Set;

public class OpenAPI30To31 {

    public void process(OpenAPI openAPI) {
        openAPI.openapi("3.1.0")
                .jsonSchemaDialect("https://spec.openapis.org/oas/3.1/dialect/base")
                .specVersion(SpecVersion.V31);

        removeReservedExtensionsName(openAPI.getExtensions());

        if (openAPI.getInfo() != null) {
            removeReservedExtensionsName(openAPI.getInfo().getExtensions());
        }

        if (openAPI.getPaths() != null) {
            removeReservedExtensionsName(openAPI.getPaths().getExtensions());
        }

        if (openAPI.getComponents() != null) {
            removeReservedExtensionsName(openAPI.getComponents().getExtensions());
        }
    }

    private void removeReservedExtensionsName(Map<String, Object> extensions) {
        if (extensions == null || extensions.isEmpty()) {
            return;
        }
        final Set<String> keys = extensions.keySet();
        for (String key : keys) {
            if (key.startsWith("x-oas-") || key.startsWith("x-oai-")) {
                extensions.remove(key);
            }
        }
    }
}
