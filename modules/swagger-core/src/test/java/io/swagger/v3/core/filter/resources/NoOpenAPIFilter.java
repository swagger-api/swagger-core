package io.swagger.v3.core.filter.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Does nothing
 **/
public class NoOpenAPIFilter extends AbstractSpecFilter {

    public static final String VERSION = "3.0.1";

    @Override
    public Optional<OpenAPI> filterOpenAPI(OpenAPI openAPI, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (VERSION.equals(openAPI.getOpenapi())) {
            return Optional.empty();
        }
        return Optional.of(openAPI);

    }
}
