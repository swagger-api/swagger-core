package io.swagger.filter;

import io.swagger.core.filter.AbstractSpecFilter;
import io.swagger.model.ApiDescription;
import io.swagger.models.Operation;

import java.util.List;
import java.util.Map;

/**
 * Sample filter to avoid all get operations for the resource
 **/
public class NoGetOperationsFilter extends AbstractSpecFilter {
    @Override
    public boolean isOperationAllowed(
            Operation operation,
            ApiDescription api,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers) {
        if ("get".equals(api.getMethod())) {
            return false;
        }
        return true;
    }
}