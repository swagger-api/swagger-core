package io.swagger.filter;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.model.ApiDescription;
import io.swagger.models.Operation;
import io.swagger.models.parameters.Parameter;

import java.util.List;
import java.util.Map;

/**
 * Sample filter to parameters if "internal" has been set and the header
 * "super-user" is not passed
 **/
public class RemoveInternalParamsFilter extends AbstractSpecFilter {
    @Override
    public boolean isParamAllowed(
            Parameter parameter,
            Operation operation,
            ApiDescription api,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers) {
        if (parameter.getDescription() != null
                && parameter.getDescription().startsWith("secret:")) {
            if (headers != null) {
                if (headers.containsKey("super-user")) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}