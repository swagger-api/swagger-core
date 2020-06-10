package io.swagger.v3.core.filter;

import io.swagger.v3.core.model.ApiDescription;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.RefUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SpecFilter {

    public OpenAPI filter(OpenAPI openAPI, OpenAPISpecFilter filter, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        OpenAPI filteredOpenAPI = filterOpenAPI(filter, openAPI, params, cookies, headers);
        if (filteredOpenAPI == null) {
            return filteredOpenAPI;
        }

        OpenAPI clone = new OpenAPI();
        clone.info(filteredOpenAPI.getInfo());
        clone.openapi(filteredOpenAPI.getOpenapi());
        clone.setExtensions(filteredOpenAPI.getExtensions());
        clone.setExternalDocs(filteredOpenAPI.getExternalDocs());
        clone.setSecurity(filteredOpenAPI.getSecurity());
        clone.setServers(filteredOpenAPI.getServers());
        clone.tags(filteredOpenAPI.getTags() == null ? null : new ArrayList<>(openAPI.getTags()));

        final Set<String> allowedTags = new HashSet<>();
        final Set<String> filteredTags = new HashSet<>();

        Paths clonedPaths = new Paths();
        if (filteredOpenAPI.getPaths() != null) {
            for (String resourcePath : filteredOpenAPI.getPaths().keySet()) {
                PathItem pathItem = filteredOpenAPI.getPaths().get(resourcePath);

                PathItem filteredPathItem = filterPathItem(filter, pathItem, resourcePath, params, cookies, headers);

                if (filteredPathItem != null) {

                    PathItem clonedPathItem = new PathItem();
                    clonedPathItem.set$ref(filteredPathItem.get$ref());
                    clonedPathItem.setDescription(filteredPathItem.getDescription());
                    clonedPathItem.setSummary(filteredPathItem.getSummary());
                    clonedPathItem.setExtensions(filteredPathItem.getExtensions());
                    clonedPathItem.setParameters(filteredPathItem.getParameters());
                    clonedPathItem.setServers(filteredPathItem.getServers());

                    Map<PathItem.HttpMethod, Operation> ops = filteredPathItem.readOperationsMap();

                    for (PathItem.HttpMethod key : ops.keySet()) {
                        Operation op = ops.get(key);
                        List<String> opTagsBeforeFilter = null;
                        if (op.getTags() != null) {
                            opTagsBeforeFilter = new ArrayList<>(op.getTags());
                        } else {
                            opTagsBeforeFilter = new ArrayList<>();
                        }
                        op = filterOperation(filter, op, resourcePath, key.toString(), params, cookies, headers);
                        clonedPathItem.operation(key, op);
                        if (op == null) {
                            filteredTags.addAll(opTagsBeforeFilter);
                        } else {
                            if (op.getTags() != null) {
                                opTagsBeforeFilter.removeAll(op.getTags());
                                allowedTags.addAll(op.getTags());
                            }
                            filteredTags.addAll(opTagsBeforeFilter);
                        }

                    }
                    if (!clonedPathItem.readOperations().isEmpty()) {
                        clonedPaths.addPathItem(resourcePath, clonedPathItem);
                    }
                }
            }
            clone.paths(clonedPaths);
        }

        filteredTags.removeAll(allowedTags);

        final List<Tag> tags = clone.getTags();
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

        if (filteredOpenAPI.getComponents() != null) {
            clone.components(new Components());
            clone.getComponents().setSchemas(filterComponentsSchema(filter, filteredOpenAPI.getComponents().getSchemas(), params, cookies, headers));
            clone.getComponents().setSecuritySchemes(filteredOpenAPI.getComponents().getSecuritySchemes());
            clone.getComponents().setCallbacks(filteredOpenAPI.getComponents().getCallbacks());
            clone.getComponents().setExamples(filteredOpenAPI.getComponents().getExamples());
            clone.getComponents().setExtensions(filteredOpenAPI.getComponents().getExtensions());
            clone.getComponents().setHeaders(filteredOpenAPI.getComponents().getHeaders());
            clone.getComponents().setLinks(filteredOpenAPI.getComponents().getLinks());
            clone.getComponents().setParameters(filteredOpenAPI.getComponents().getParameters());
            clone.getComponents().setRequestBodies(filteredOpenAPI.getComponents().getRequestBodies());
            clone.getComponents().setResponses(filteredOpenAPI.getComponents().getResponses());


        }

        if (filter.isRemovingUnreferencedDefinitions()) {
            clone = removeBrokenReferenceDefinitions(clone);
        }

        return clone;
    }

    protected OpenAPI filterOpenAPI(OpenAPISpecFilter filter, OpenAPI openAPI, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (openAPI != null) {
            Optional<OpenAPI> filteredOpenAPI = filter.filterOpenAPI(openAPI, params, cookies, headers);
            if (filteredOpenAPI.isPresent()) {
                return filteredOpenAPI.get();
            }
        }
        return null;
    }

    protected Operation filterOperation(OpenAPISpecFilter filter, Operation operation, String resourcePath, String key, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (operation != null) {
            ApiDescription description = new ApiDescription(resourcePath, key);
            Optional<Operation> filteredOperation = filter.filterOperation(operation, description, params, cookies, headers);
            if (filteredOperation.isPresent()) {
                List<Parameter> filteredParameters = new ArrayList<>();
                Operation filteredOperationGet = filteredOperation.get();

                Operation clone = new Operation();
                clone.setCallbacks(filteredOperationGet.getCallbacks());
                clone.setDeprecated(filteredOperationGet.getDeprecated());
                clone.setDescription(filteredOperationGet.getDescription());
                clone.setExtensions(filteredOperationGet.getExtensions());
                clone.setExternalDocs(filteredOperationGet.getExternalDocs());
                clone.setOperationId(filteredOperationGet.getOperationId());
                clone.setSecurity(filteredOperationGet.getSecurity());
                clone.setServers(filteredOperationGet.getServers());
                clone.setSummary(filteredOperationGet.getSummary());
                clone.setTags(filteredOperationGet.getTags());

                List<Parameter> parameters = filteredOperationGet.getParameters();
                if (parameters != null) {
                    for (Parameter parameter : parameters) {
                        Parameter filteredParameter = filterParameter(filter, operation, parameter, resourcePath, key, params, cookies, headers);
                        if (filteredParameter != null) {
                            filteredParameters.add(filteredParameter);
                        }
                    }
                    clone.setParameters(filteredParameters);
                }

                RequestBody requestBody = filteredOperation.get().getRequestBody();
                if (requestBody != null) {
                    RequestBody filteredRequestBody = filterRequestBody(filter, operation, requestBody, resourcePath, key, params, cookies, headers);
                    clone.setRequestBody(filteredRequestBody);

                }

                ApiResponses responses = filteredOperation.get().getResponses();
                ApiResponses clonedResponses = responses;
                if (responses != null) {
                    responses.forEach((responseKey, response) -> {
                        ApiResponse filteredResponse = filterResponse(filter, operation, response, resourcePath, key, params, cookies, headers);
                        if (filteredResponse != null) {
                            clonedResponses.addApiResponse(responseKey, filteredResponse);
                        }
                    });
                    clone.setResponses(clonedResponses);
                }

                return clone;
            }
        }
        return null;
    }

    protected PathItem filterPathItem(OpenAPISpecFilter filter, PathItem pathItem, String resourcePath, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        ApiDescription description = new ApiDescription(resourcePath, null);
        Optional<PathItem> filteredPathItem = filter.filterPathItem(pathItem, description, params, cookies, headers);
        if (filteredPathItem.isPresent()) {
            return filteredPathItem.get();
        }
        return null;
    }

    protected Parameter filterParameter(OpenAPISpecFilter filter, Operation operation, Parameter parameter, String resourcePath, String key, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (parameter != null) {
            ApiDescription description = new ApiDescription(resourcePath, key);
            Optional<Parameter> filteredParameter = filter.filterParameter(parameter, operation, description, params, cookies, headers);
            if (filteredParameter.isPresent()) {
                return filteredParameter.get();
            }
        }
        return null;

    }

    protected RequestBody filterRequestBody(OpenAPISpecFilter filter, Operation operation, RequestBody requestBody, String resourcePath, String key, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (requestBody != null) {
            ApiDescription description = new ApiDescription(resourcePath, key);
            Optional<RequestBody> filteredRequestBody = filter.filterRequestBody(requestBody, operation, description, params, cookies, headers);
            if (filteredRequestBody.isPresent()) {
                return filteredRequestBody.get();
            }
        }
        return null;

    }

    protected ApiResponse filterResponse(OpenAPISpecFilter filter, Operation operation, ApiResponse response, String resourcePath, String key, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (response != null) {
            ApiDescription description = new ApiDescription(resourcePath, key);
            Optional<ApiResponse> filteredResponse = filter.filterResponse(response, operation, description, params, cookies, headers);
            if (filteredResponse.isPresent()) {
                return filteredResponse.get();
            }
        }
        return null;

    }

    protected Map<String, Schema> filterComponentsSchema(OpenAPISpecFilter filter, Map<String, Schema> schemasMap, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (schemasMap == null) {
            return null;
        }
        Map<String, Schema> clonedComponentsSchema = new LinkedHashMap<>();

        for (String key : schemasMap.keySet()) {
            Schema definition = schemasMap.get(key);
            Optional<Schema> filteredDefinition = filter.filterSchema(definition, params, cookies, headers);
            if (!filteredDefinition.isPresent()) {
                continue;
            } else {
                Map<String, Schema> clonedProperties = new LinkedHashMap<>();
                if (filteredDefinition.get().getProperties() != null) {
                    for (Object propName : filteredDefinition.get().getProperties().keySet()) {
                        Schema property = (Schema) filteredDefinition.get().getProperties().get((String) propName);
                        if (property != null) {
                            Optional<Schema> filteredProperty = filter.filterSchemaProperty(property, definition, (String) propName, params, cookies, headers);
                            if (filteredProperty.isPresent()) {
                                clonedProperties.put((String) propName, filteredProperty.get());
                            }
                        }
                    }
                }

                try {
                    // TODO solve this, and generally handle clone and passing references
                    Schema clonedModel = Json.mapper().readValue(Json.pretty(definition), Schema.class);
                    if (clonedModel.getProperties() != null) {
                        clonedModel.getProperties().clear();
                    }
                    if (!clonedProperties.isEmpty()) {
                        clonedModel.setProperties(clonedProperties);
                    }
                    clonedComponentsSchema.put(key, clonedModel);

                } catch (IOException e) {
                    continue;
                }
            }
        }
        return clonedComponentsSchema;
    }

    private void addSchemaRef(Schema schema, Set<String> referencedDefinitions) {

        if (schema == null) {
            return;
        }
        if (!StringUtils.isBlank(schema.get$ref())) {
            referencedDefinitions.add(schema.get$ref());
            return;
        }
        if (schema.getDiscriminator() != null && schema.getDiscriminator().getMapping() != null) {
            for (Map.Entry<String, String> mapping: schema.getDiscriminator().getMapping().entrySet()) {
                referencedDefinitions.add(mapping.getValue());
            }
        }

        if (schema.getProperties() != null) {
            for (Object propName : schema.getProperties().keySet()) {
                Schema property = (Schema) schema.getProperties().get((String) propName);
                addSchemaRef(property, referencedDefinitions);
            }
        }

        if (schema.getAdditionalProperties() != null && (schema.getAdditionalProperties() instanceof Schema)) {
            addSchemaRef((Schema)schema.getAdditionalProperties(), referencedDefinitions);
        }

        if (schema instanceof ArraySchema &&
                ((ArraySchema) schema).getItems() != null) {
            addSchemaRef(((ArraySchema) schema).getItems(), referencedDefinitions);
        } else if (schema instanceof ComposedSchema) {
            ComposedSchema composedSchema = (ComposedSchema) schema;
            if (composedSchema.getAllOf() != null) {
                for (Schema ref : composedSchema.getAllOf()) {
                    addSchemaRef(ref, referencedDefinitions);
                }
            }
            if (composedSchema.getAnyOf() != null) {
                for (Schema ref : composedSchema.getAnyOf()) {
                    addSchemaRef(ref, referencedDefinitions);
                }
            }
            if (composedSchema.getOneOf() != null) {
                for (Schema ref : composedSchema.getOneOf()) {
                    addSchemaRef(ref, referencedDefinitions);
                }
            }
        }
    }

    private void addContentSchemaRef(Content content, Set<String> referencedDefinitions) {
        if (content != null) {
            for (String keyBodyContent : content.keySet()) {
                MediaType mediaType = content.get(keyBodyContent);
                addSchemaRef(mediaType.getSchema(), referencedDefinitions);
            }
        }
    }

    private void addPathItemSchemaRef(PathItem pathItem, Set<String> referencedDefinitions) {
        if (pathItem.getParameters() != null) {
            for (Parameter parameter : pathItem.getParameters()) {
                addSchemaRef(parameter.getSchema(), referencedDefinitions);
                addContentSchemaRef(parameter.getContent(), referencedDefinitions);
            }
        }
        Map<PathItem.HttpMethod, Operation> ops = pathItem.readOperationsMap();
        for (PathItem.HttpMethod key : ops.keySet()) {
            Operation op = ops.get(key);
            if (op.getRequestBody() != null) {
                addContentSchemaRef(op.getRequestBody().getContent(), referencedDefinitions);
            }
            if (op.getResponses() != null) {
                for (String keyResponses : op.getResponses().keySet()) {
                    ApiResponse response = op.getResponses().get(keyResponses);
                    if (response.getHeaders() != null) {
                        for (String keyHeaders : response.getHeaders().keySet()) {
                            Header header = response.getHeaders().get(keyHeaders);
                            addSchemaRef(header.getSchema(), referencedDefinitions);
                            addContentSchemaRef(header.getContent(), referencedDefinitions);
                        }
                    }
                    addContentSchemaRef(response.getContent(), referencedDefinitions);
                }
            }
            if (op.getParameters() != null) {
                for (Parameter parameter : op.getParameters()) {
                    addSchemaRef(parameter.getSchema(), referencedDefinitions);
                    addContentSchemaRef(parameter.getContent(), referencedDefinitions);
                }
            }
            if (op.getCallbacks() != null) {
                for (String keyCallback : op.getCallbacks().keySet()) {
                    Callback callback = op.getCallbacks().get(keyCallback);
                    for (String keyCallbackPathItem : callback.keySet()) {
                        PathItem callbackPathItem = callback.get(keyCallbackPathItem);
                        addPathItemSchemaRef(callbackPathItem, referencedDefinitions);
                    }
                }
            }
        }
    }

    protected OpenAPI removeBrokenReferenceDefinitions(OpenAPI openApi) {

        if (openApi == null || openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) {
            return openApi;
        }
        Set<String> referencedDefinitions = new TreeSet<>();

        if (openApi.getPaths() != null) {
            for (String resourcePath : openApi.getPaths().keySet()) {
                PathItem pathItem = openApi.getPaths().get(resourcePath);
                addPathItemSchemaRef(pathItem, referencedDefinitions);
            }
        }

        referencedDefinitions.addAll(resolveAllNestedRefs(referencedDefinitions, referencedDefinitions, openApi));
        openApi.getComponents()
                .getSchemas()
                .keySet()
                .retainAll(referencedDefinitions.stream()
                        .map(s -> (String) RefUtils.extractSimpleName(s).getLeft())
                        .collect(Collectors.toSet()));
        return openApi;
    }

    protected Set<String> resolveAllNestedRefs(Set<String> refs, Set<String> accumulatedRefs, OpenAPI openApi) {
        Set<String> justDiscoveredReferencedDefinitions = new TreeSet<>();
        for (String ref : refs) {
            locateReferencedDefinitions(ref, justDiscoveredReferencedDefinitions, openApi);
        }
        // Base case - no new references have been discovered. Halt discovery to avoid infinite loops
        if (accumulatedRefs.containsAll(justDiscoveredReferencedDefinitions)) {
            return Collections.emptySet();
        } else {
            // Remove all refs that have already been discovered.
            justDiscoveredReferencedDefinitions.removeAll(accumulatedRefs);
            accumulatedRefs.addAll(justDiscoveredReferencedDefinitions);
            return resolveAllNestedRefs(justDiscoveredReferencedDefinitions, accumulatedRefs, openApi);
        }
    }

    protected void locateReferencedDefinitions(String ref, Set<String> nestedReferencedDefinitions, OpenAPI openAPI) {
        nestedReferencedDefinitions.add(ref);
        String simpleName = (String) RefUtils.extractSimpleName(ref).getLeft();
        Schema model = openAPI.getComponents().getSchemas().get(simpleName);
        if (model != null) {
            addSchemaRef(model, nestedReferencedDefinitions);
        }
    }
}
