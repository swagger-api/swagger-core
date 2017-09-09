package io.swagger.filter;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.model.ApiDescription;
import io.swagger.oas.models.Operation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Sample filter to avoid all get operations for the resource
 **/
public class ChangeGetOperationsFilter extends AbstractSpecFilter {
    @Override
    public Optional<Operation> filterOperation(Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if ("get".equals(api.getMethod())) {
            return Optional.of(operation.operationId("Changed Operation").description("Changing some attributes of the operation"));
        }
        return Optional.of(operation);
    }
}