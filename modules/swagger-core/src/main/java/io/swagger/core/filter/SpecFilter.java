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
            PathItem path = openAPI.getPaths().get(resourcePath);

            PathItem clonedPath = new PathItem();
            Operation get = path.getGet();
            if (get != null) {
                Operation filteredOperation = filterOperation(filter, get, resourcePath, GET, params, cookies, headers);
                clonedPath.setGet(filteredOperation);
                clonedPaths.addPathItem(resourcePath, clonedPath);
            }
            Operation put = path.getPut();
            if (put != null) {
                Operation filteredOperation = filterOperation(filter, put, resourcePath, PUT, params, cookies, headers);
                clonedPath.setPut(filteredOperation);
                clonedPaths.addPathItem(resourcePath, clonedPath);
            }

            Operation post = path.getPost();
            if (post != null) {
                Operation filteredOperation = filterOperation(filter, post, resourcePath, POST, params, cookies, headers);
                clonedPath.setPost(filteredOperation);
                clonedPaths.addPathItem(resourcePath, clonedPath);
            }

            Operation delete = path.getDelete();
            if (delete != null) {
                Operation filteredOperation = filterOperation(filter, delete, resourcePath, DELETE, params, cookies, headers);
                clonedPath.setDelete(filteredOperation);
                clonedPaths.addPathItem(resourcePath, clonedPath);
            }

            Operation head = path.getHead();
            if (head != null) {
                Operation filteredOperation = filterOperation(filter, head, resourcePath, HEAD, params, cookies, headers);
                clonedPath.setHead(filteredOperation);
                clonedPaths.addPathItem(resourcePath, clonedPath);
            }

            Operation patch = path.getPatch();
            if (patch != null) {
                Operation filteredOperation = filterOperation(filter, patch, resourcePath, PATCH, params, cookies, headers);
                clonedPath.setPatch(filteredOperation);
                clonedPaths.addPathItem(resourcePath, clonedPath);
            }

            Operation options = path.getOptions();
            if (options != null) {
                Operation filteredOperation = filterOperation(filter, options, resourcePath, OPTIONS, params, cookies, headers);
                clonedPath.setOptions(filteredOperation);
                clonedPaths.addPathItem(resourcePath, clonedPath);
            }

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

            if (filter instanceof AbstractSpecFilter) {
            /*if (((AbstractSpecFilter) filter).isRemovingUnreferencedDefinitions()) {
                clone = removeBrokenReferenceDefinitions(clone);
            }*/
            }
        }
        return clone;
    }

    private Operation filterOperation(OpenAPISpecFilter filter, Operation operation, String resourcePath, String key, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {

        ApiDescription description = new ApiDescription(resourcePath, key);
        Optional<Operation> filteredOp = filter.filterOperation(operation, description, params, cookies, headers);
        if (filteredOp.isPresent()) {
            return filteredOp.get();
        }
        return operation;

    }
}
