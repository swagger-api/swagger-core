package io.swagger.filter.resources;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.model.ApiDescription;
import io.swagger.oas.models.OpenAPI;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Does nothing
 **/
public class NoOpenAPIFilter extends AbstractSpecFilter {

    public static final String VERSION = "3.0.0-rc2";

    @Override
    public Optional<OpenAPI> filterOpenAPI(OpenAPI openAPI, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (VERSION.equals(openAPI.getOpenapi())) {
            return Optional.empty();
        }
        return Optional.of(openAPI);
    }
}