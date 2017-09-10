package io.swagger.filter.resources;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.model.ApiDescription;
import io.swagger.oas.models.Operation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Sample filter to avoid all get operations for the resource
 **/
public class ReplaceGetOperationsFilter extends AbstractSpecFilter {
    private static final String GET = "get";
    private static final String NEW_OPERATION_ID = "New Operation";
    private static final String NEW_OPERATION_DESCRIPTION = "Replaced Operation";

    @Override
    public Optional<Operation> filterOperation(Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (GET.equals(api.getMethod())) {
            return Optional.of(new Operation().description(NEW_OPERATION_DESCRIPTION).
                    operationId(NEW_OPERATION_ID));
        }
        return Optional.of(operation);
    }
}