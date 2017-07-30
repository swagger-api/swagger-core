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

public interface OpenAPISpecFilter {
    Optional<Operation> filterOperation(
            Operation operation,
            ApiDescription api,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers);

    Optional<OpenAPI> filterOpenAPI(
            OpenAPI openAPI,
            ApiDescription api,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers);

    Optional<PathItem> filterPathItem(
            PathItem pathItem,
            ApiDescription api,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers);

    Optional<Parameter> filterParameter(
            Operation operation,
            Parameter parameter,
            ApiDescription api,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers);

    Optional<Parameter> filterProperty(
            Schema schema,
            Schema property,
            String propertyName,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers);
}