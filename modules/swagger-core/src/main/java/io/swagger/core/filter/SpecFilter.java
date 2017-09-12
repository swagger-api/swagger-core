package io.swagger.core.filter;

import io.swagger.model.ApiDescription;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.Paths;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpecFilter {
    private final static String GET = "get";
    private final static String HEAD = "head";
    private final static String PUT = "put";
    private final static String POST = "post";
    private final static String DELETE = "delete";
    private final static String PATCH = "patch";
    private final static String OPTIONS = "options";

    public OpenAPI filter(OpenAPI openAPI, OpenAPISpecFilter filter, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        OpenAPI filteredOpenAPI = filterOpenAPI(filter, openAPI, params, cookies, headers);
        if (filteredOpenAPI == null) {
            return filteredOpenAPI;
        }
        OpenAPI clone = new OpenAPI();
        clone.info(openAPI.getInfo());
        clone.openapi(openAPI.getOpenapi());
        clone.tags(openAPI.getTags() == null ? null : new ArrayList<>(openAPI.getTags()));

        Paths clonedPaths = new Paths();
        for (String resourcePath : openAPI.getPaths().keySet()) {
            PathItem pathItem = openAPI.getPaths().get(resourcePath);

            PathItem filteredPathItem = filterPathItem(filter, pathItem, resourcePath, null, params, cookies, headers);
            if (filteredPathItem != null) {
                filteredPathItem.setGet(filterOperation(filter, pathItem.getGet(), resourcePath, GET, params, cookies, headers));
                filteredPathItem.setPost(filterOperation(filter, pathItem.getPost(), resourcePath, POST, params, cookies, headers));
                filteredPathItem.setPut(filterOperation(filter, pathItem.getPut(), resourcePath, PUT, params, cookies, headers));
                filteredPathItem.setDelete(filterOperation(filter, pathItem.getDelete(), resourcePath, DELETE, params, cookies, headers));
                filteredPathItem.setPatch(filterOperation(filter, pathItem.getPatch(), resourcePath, PATCH, params, cookies, headers));
                filteredPathItem.setHead(filterOperation(filter, pathItem.getHead(), resourcePath, HEAD, params, cookies, headers));
                filteredPathItem.setOptions(filterOperation(filter, pathItem.getOptions(), resourcePath, OPTIONS, params, cookies, headers));
                filteredPathItem.setDescription(pathItem.getDescription());
                filteredPathItem.set$ref(pathItem.get$ref());
                filteredPathItem.setSummary(pathItem.getSummary());
                clonedPaths.addPathItem(resourcePath, filteredPathItem);
            }
            clone.paths(clonedPaths);
            clone.setComponents(openAPI.getComponents());
            clone.setSecurity(openAPI.getSecurity());
        }
        return clone;
    }

    private OpenAPI filterOpenAPI(OpenAPISpecFilter filter, OpenAPI openAPI, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (openAPI != null) {
            Optional<OpenAPI> filteredOpenAPI = filter.filterOpenAPI(openAPI, null, params, cookies, headers);
            if (filteredOpenAPI.isPresent()) {
                return filteredOpenAPI.get();
            }
        }
        return null;
    }

    private Operation filterOperation(OpenAPISpecFilter filter, Operation operation, String resourcePath, String key, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (operation != null) {
            ApiDescription description = new ApiDescription(resourcePath, key);
            Optional<Operation> filteredOperation = filter.filterOperation(operation, description, params, cookies, headers);
            if (filteredOperation.isPresent()) {
                List<Parameter> filteredParameters = new ArrayList<>();
                Operation filteredOperationGet = filteredOperation.get();
                List<Parameter> parameters = filteredOperationGet.getParameters();
                if (parameters != null) {
                    for (Parameter parameter : parameters) {
                        Parameter filteredParameter = filterParameter(filter, operation, parameter, resourcePath, key, params, cookies, headers);
                        if (filteredParameter != null) {
                            filteredParameter.setSchema(filterProperty(filter, parameter.getSchema(), resourcePath, key, params, cookies, headers));
                            filteredParameters.add(filteredParameter);
                        }
                    }
                }
                filteredOperationGet.setParameters(filteredParameters);
                return filteredOperationGet;
            }
        }
        return null;
    }

    private PathItem filterPathItem(OpenAPISpecFilter filter, PathItem pathItem, String resourcePath, String key, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        ApiDescription description = new ApiDescription(resourcePath, key);
        Optional<PathItem> filteredPathItem = filter.filterPathItem(pathItem, description, params, cookies, headers);
        if (filteredPathItem.isPresent()) {
            return filteredPathItem.get();
        }
        return null;
    }

    private Parameter filterParameter(OpenAPISpecFilter filter, Operation operation, Parameter parameter, String resourcePath, String key, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (parameter != null) {
            ApiDescription description = new ApiDescription(resourcePath, key);
            Optional<Parameter> filteredParameter = filter.filterParameter(operation, parameter, description, params, cookies, headers);
            if (filteredParameter.isPresent()) {
                return filteredParameter.get();
            }
        }
        return null;

    }

    private Schema filterProperty(OpenAPISpecFilter filter, Schema property, String resourcePath, String key, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (property != null) {
            Optional<Schema> filteredProperty = filter.filterProperty(property, property, "", params, cookies, headers);
            if (filteredProperty.isPresent()) {
                return filteredProperty.get();
            }
        }
        return null;
    }
}
