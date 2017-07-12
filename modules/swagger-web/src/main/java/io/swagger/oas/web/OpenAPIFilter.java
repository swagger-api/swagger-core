package io.swagger.oas.web;

import java.util.List;
import java.util.Map;

import io.swagger.oas.models.Operation;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;

public interface OpenAPIFilter {
    boolean isOperationAllowed(
            Operation operation,
            String path,
            String method,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers);

    boolean isParamAllowed(
            Parameter parameter,
            Operation operation,
            String path,
            String method,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers);

    boolean isPropertyAllowed(
            Schema<?> schema,
            Schema<?> property,
            String propertyName,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers);
}
