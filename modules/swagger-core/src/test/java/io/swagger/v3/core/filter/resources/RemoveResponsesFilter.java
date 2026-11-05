package io.swagger.v3.core.filter.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.core.model.ApiDescription;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Sample filter that hides any response whose description equals "Invalid ID supplied".
 **/
public class RemoveResponsesFilter extends AbstractSpecFilter {
    @Override
    public Optional<ApiResponse> filterResponse(ApiResponse response, Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (response.getDescription() != null && response.getDescription().equals("Invalid ID supplied")) {
            return Optional.empty();
        }
        return Optional.of(response);
    }
}
