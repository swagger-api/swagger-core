package io.swagger.v3.core.filter.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.core.model.ApiDescription;
import io.swagger.v3.oas.models.Operation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Sample filter to remove all operations tagged as internal
 **/
public class NoInternalOperationsFilter extends AbstractSpecFilter {

    public static final String INTERNAL_TAG = "internal";

    @Override
    public Optional<Operation> filterOperation(Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (operation.getTags() != null && operation.getTags().contains(INTERNAL_TAG)) {
            return Optional.empty();
        }
        return Optional.of(operation);
    }
}
