package io.swagger.core.filter;

import io.swagger.model.ApiDescription;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.Paths;
import io.swagger.oas.models.tags.Tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SpecFilter {
    private final static String GET = "get";
    private final static String HEAD = "head";
    private final static String PUT = "put";
    private final static String POST = "post";
    private final static String DELETE = "delete";
    private final static String PATCH = "patch";
    private final static String OPTIONS = "options";

    public OpenAPI filter(OpenAPI openAPI, OpenAPISpecFilter filter, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        OpenAPI clone = new OpenAPI();
        clone.info(openAPI.getInfo());
        clone.openapi(openAPI.getOpenapi());
        clone.tags(openAPI.getTags() == null ? null : new ArrayList<>(openAPI.getTags()));

        final Set<String> filteredTags = new HashSet<>();
        final Set<String> allowedTags = new HashSet<>();
        Paths clonedPaths = new Paths();
        for (String resourcePath : openAPI.getPaths().keySet()) {
            PathItem pathItem = openAPI.getPaths().get(resourcePath);

            PathItem clonedPathItem = new PathItem();

            clonedPathItem.setGet(filterOperation(filter, pathItem.getGet(), GET, resourcePath, params, cookies, headers));
            clonedPathItem.setPost(filterOperation(filter, pathItem.getPost(), POST, resourcePath, params, cookies, headers));
            clonedPathItem.setPut(filterOperation(filter, pathItem.getPut(), PUT, resourcePath, params, cookies, headers));
            clonedPathItem.setDelete(filterOperation(filter, pathItem.getDelete(), DELETE, resourcePath, params, cookies, headers));
            clonedPathItem.setPatch(filterOperation(filter, pathItem.getPatch(), PATCH, resourcePath, params, cookies, headers));
            clonedPathItem.setHead(filterOperation(filter, pathItem.getHead(), HEAD, resourcePath, params, cookies, headers));
            clonedPathItem.setOptions(filterOperation(filter, pathItem.getOptions(), OPTIONS, resourcePath, params, cookies, headers));
            clonedPaths.addPathItem(resourcePath, clonedPathItem);

            clone.paths(clonedPaths);
            clone.setComponents(openAPI.getComponents());

            final List<Tag> tags = clone.getTags();
            filteredTags.removeAll(allowedTags);
            if (tags != null && !filteredTags.isEmpty()) {
                for (Iterator<Tag> it = tags.iterator(); it.hasNext(); ) {
                    if (filteredTags.contains(it.next().getName())) {
                        it.remove();
                    }
                }
                if (clone.getTags().isEmpty()) {
                    clone.setTags(null);
                }
            }
            clone.setSecurity(openAPI.getSecurity());
        }
        return clone;
    }

    private Operation filterOperation(OpenAPISpecFilter filter, Operation operation, String resourcePath, String key, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (operation != null) {
            ApiDescription description = new ApiDescription(resourcePath, key);
            Optional<Operation> filteredOp = filter.filterOperation(operation, description, params, cookies, headers);
            if (filteredOp.isPresent()) {
                return filteredOp.get();
            }
        }
        return operation;
    }
}
