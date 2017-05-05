package io.swagger.core.filter;

import io.swagger.models.Operation;
import io.swagger.models.media.Schema;
import io.swagger.models.parameters.Parameter;

import java.util.List;
import java.util.Map;

public interface SwaggerSpecFilter {
    boolean isOperationAllowed(
            Operation operation,
//            ApiDescription api,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers);

    boolean isParamAllowed(
            Parameter parameter,
            Operation operation,
//            ApiDescription api,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers);

    boolean isPropertyAllowed(
            Schema schema,
            Schema property,
            String propertyName,
            Map<String, List<String>> params,
            Map<String, String> cookies,
            Map<String, List<String>> headers);
}