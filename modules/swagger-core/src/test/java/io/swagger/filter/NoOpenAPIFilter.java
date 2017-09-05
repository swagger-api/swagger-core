package io.swagger.filter;

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
    @Override
    public Optional<OpenAPI> filterOpenAPI(OpenAPI openAPI, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if ("3.0.0-rc2".equals(openAPI.getOpenapi())) {
            return Optional.empty();
        }
        return Optional.of(openAPI);
    }
}