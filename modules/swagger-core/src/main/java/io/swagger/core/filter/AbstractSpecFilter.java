package io.swagger.core.filter;

import io.swagger.model.ApiDescription;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractSpecFilter implements OpenAPISpecFilter {
    public boolean isOperationAllowed(
            Operation operation,
            ApiDescription api,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers) {
        return true;
    }

    @Override
    public Optional<Operation> filterOperation(Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        return Optional.empty();
    }

    @Override
    public Optional<OpenAPI> filterOpenAPI(OpenAPI openAPI, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        return Optional.empty();
    }

    @Override
    public Optional<Parameter> filterParameter(Parameter parameter, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        return Optional.empty();
    }

    @Override
    public Optional<PathItem> filterPathItem(PathItem pathItem, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        return Optional.empty();
    }

    @Override
    public Optional<Parameter> filterProperty(Schema schema, Schema property, String propertyName, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        return Optional.empty();
    }

    public boolean isRemovingUnreferencedDefinitions() {
        return false;
    }
}