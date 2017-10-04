package io.swagger.filter.resources;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.model.ApiDescription;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.parameters.Parameter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Does nothing
 **/
public class NoParametersWithoutQueryInFilter extends AbstractSpecFilter {
    private static final String QUERY_IN = "query";

    @Override
    public Optional<Parameter> filterParameter(Parameter parameter, Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (QUERY_IN.equals(parameter.getIn())) {
            return Optional.empty();
        }
        return Optional.of(parameter);

    }
}
