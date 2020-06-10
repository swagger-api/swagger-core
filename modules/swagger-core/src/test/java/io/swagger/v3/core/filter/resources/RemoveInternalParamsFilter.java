package io.swagger.v3.core.filter.resources;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.core.model.ApiDescription;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Sample filter to parameters if "internal" has been set and the header
 * "super-user" is not passed
 **/
public class RemoveInternalParamsFilter extends AbstractSpecFilter {
    @Override
    public Optional<Parameter> filterParameter(Parameter parameter, Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (parameter.getDescription() != null && parameter.getDescription().startsWith("secret:")) {
            if (headers != null) {
                if (headers.containsKey("super-user")) {
                    return Optional.of(parameter);
                }
            }
            return Optional.empty();
        }
        return Optional.of(parameter);
    }
}
