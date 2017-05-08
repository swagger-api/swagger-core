package io.swagger.core.filter;

import io.swagger.model.ApiDescription;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;

import java.util.List;
import java.util.Map;

public abstract class AbstractSpecFilter implements SwaggerSpecFilter {
    public boolean isOperationAllowed(
            Operation operation,
            ApiDescription api,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers) {
        return true;
    }

    public boolean isParamAllowed(
            Parameter parameter,
            Operation operation,
            ApiDescription api,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers) {
        return true;
    }

    public boolean isPropertyAllowed(
            Schema model,
            Schema property,
            String propertyName,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers) {
        return true;
    }

    public boolean isRemovingUnreferencedDefinitions() {
        return false;
    }
}