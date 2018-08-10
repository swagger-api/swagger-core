package io.swagger.v3.core.filter.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.core.model.ApiDescription;
import io.swagger.v3.oas.models.Operation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Sample filter to avoid all resources for the /user resource
 **/
public class NoPetOperationsFilter extends AbstractSpecFilter {

    public static final String PET_RESOURCE = "/pet";

    @Override
    public Optional<Operation> filterOperation(Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (api.getPath().startsWith(PET_RESOURCE)) {
            return Optional.empty();
        }
        return Optional.of(operation);
    }
}