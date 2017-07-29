package io.swagger.core.filter;

import io.swagger.model.ApiDescription;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.oas.models.tags.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
        for (String resourcePath : openAPI.getPaths().keySet()) {
            PathItem path = openAPI.getPaths().get(resourcePath);
            PathItem clonedPath = new PathItem();
            clonedPath.setGet(path.getGet());
            clonedPath.setPost(path.getPost());
            clonedPath.setPut(path.getPut());
            clone.path(resourcePath, clonedPath);
        }

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
            if (((AbstractSpecFilter) filter).isRemovingUnreferencedDefinitions()) {
                clone = removeBrokenReferenceDefinitions(clone);
            }
        }

        return clone;
    }

    private OpenAPI removeBrokenReferenceDefinitions(OpenAPI openAPI) {

        if (openAPI.getComponents().getSchemas() == null || openAPI.getComponents().getSchemas().isEmpty()) {
            return openAPI;
        }

        Set<String> referencedDefinitions = new TreeSet<String>();

        if (openAPI.getComponents().getResponses() != null) {
            for (ApiResponse response : openAPI.getComponents().getResponses().values()) {
                // TODO
//                String propertyRef = getPropertyRef(response.getSchema());
//                if (propertyRef != null) {
//                    referencedDefinitions.add(propertyRef);
//                }
            }
        }
        if (openAPI.getComponents().getParameters() != null) {
            for (Parameter p : openAPI.getComponents().getParameters().values()) {
                // TODO
//                if (p instanceof BodyParameter) {
//                    BodyParameter bp = (BodyParameter) p;
//                    Set<String>  modelRef = getModelRef(bp.getSchema());
//                    if (modelRef != null) {
//                        referencedDefinitions.addAll(modelRef);
//                    }
//                }
            }
        }
        if (openAPI.getPaths() != null) {
            for (PathItem path : openAPI.getPaths().values()) {
                if (path.getParameters() != null) {
                    for (Parameter p : path.getParameters()) {
                        // TODO
//                        if (p instanceof BodyParameter) {
//                            BodyParameter bp = (BodyParameter) p;
//                            Set<String>  modelRef = getModelRef(bp.getSchema());
//                            if (modelRef != null) {
//                                referencedDefinitions.addAll(modelRef);
//                            }
//                        }
                    }
                }
                /*
                if (path.getOperations() != null) {
                    for (Operation op: path.getOperations()) {
                        if (op.getResponses() != null) {
                            for (ApiResponse response: op.getResponses().values()) {
                                String propertyRef = getPropertyRef(response.getSchema());
                                if (propertyRef != null) {
                                    referencedDefinitions.add(propertyRef);
                                }
                            }
                        }
                        if (op.getParameters() != null) {
                            for (Parameter p: op.getParameters()) {
                                if (p instanceof BodyParameter) {
                                    BodyParameter bp = (BodyParameter) p;
                                    Set<String> modelRef = getModelRef(bp.getSchema());
                                    if (modelRef != null) {
                                        referencedDefinitions.addAll(modelRef);
                                    }
                                }
                            }
                        }
                    }
                }
                */
            }
        }

        if (openAPI.getComponents().getSchemas() != null) {
            Set<String> nestedReferencedDefinitions = new TreeSet<>();
            for (String ref : referencedDefinitions) {
                locateReferencedDefinitions(ref, nestedReferencedDefinitions, openAPI);
            }
            referencedDefinitions.addAll(nestedReferencedDefinitions);
            openAPI.getComponents().getSchemas().keySet().retainAll(referencedDefinitions);
        }

        return openAPI;
    }

    private void locateReferencedDefinitions(Map<String, Schema> props, Set<String> nestedReferencedDefinitions, OpenAPI openAPI) {
        if (props == null) {
            return;
        }
        for (String keyProp : props.keySet()) {
            Schema p = props.get(keyProp);
            String ref = getPropertyRef(p);
            if (ref != null) {
                locateReferencedDefinitions(ref, nestedReferencedDefinitions, openAPI);
            }
        }
    }

    private void locateReferencedDefinitions(String ref, Set<String> nestedReferencedDefinitions, OpenAPI openAPI) {
        // if not already processed so as to avoid infinite loops
        if (!nestedReferencedDefinitions.contains(ref)) {
            nestedReferencedDefinitions.add(ref);

            Schema model = openAPI.getComponents().getSchemas().get(ref);
            if (model != null) {
                locateReferencedDefinitions(model.getProperties(), nestedReferencedDefinitions, openAPI);
            }
        }
    }

    public Map<String, Schema> filterDefinitions(OpenAPISpecFilter filter, Map<String, Schema> definitions, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (definitions == null) {
            return null;
        }
        Map<String, Schema> clonedDefinitions = new LinkedHashMap<>();

        for (String key : definitions.keySet()) {
            Schema definition = definitions.get(key);
            Map<String, Schema> clonedProperties = new LinkedHashMap<>();
            if (definition.getProperties() != null) {
                for (String propName : (Set<String>) definition.getProperties().keySet()) {
                    Schema property = (Schema) definition.getProperties().get(propName);
                    if (property != null) {
                        /*boolean shouldInclude = filter.isPropertyAllowed(definition, property, propName, params, cookies, headers);
                        if (shouldInclude) {
                            clonedProperties.put(propName, property);
                        }*/
                    }
                }
            }

//            Model clonedModel = (Model) definition.clone();
//            if (clonedModel.getProperties() != null) {
//                clonedModel.getProperties().clear();
//            }
//            if( definition.getVendorExtensions() != null && clonedModel.getVendorExtensions() != null ){
//                clonedModel.getVendorExtensions().putAll( definition.getVendorExtensions());
//            }
//
//            clonedModel.setProperties(clonedProperties);
//            clonedDefinitions.put(key, clonedModel);
            // TODO this is not a valid clone!
            clonedDefinitions.put(key, definition);
        }
        return clonedDefinitions;
    }

    public Operation filterOperation(OpenAPISpecFilter filter, Operation op, ApiDescription description, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        Operation clonedOperation = new Operation()
                .summary(op.getSummary())
                .description(op.getDescription())
                .operationId(op.getOperationId())
//                .schemes(op.getSchemes())
//                .consumes(op.getConsumes())
//                .produces(op.getProduces())
                .tags(op.getTags())
                .externalDocs(op.getExternalDocs())
//                .vendorExtensions(op.getVendorExtensions())
//                .deprecated(op.isDeprecated())
                ;

        List<Parameter> clonedParams = new ArrayList<Parameter>();
        if (op.getParameters() != null) {
            for (Parameter param : op.getParameters()) {
                /*if (filter.isParamAllowed(param, op, description, params, cookies, headers)) {
                    clonedParams.add(param);
                }*/
            }
        }
        clonedOperation.setParameters(clonedParams);
        clonedOperation.setSecurity(op.getSecurity());
        clonedOperation.setResponses(op.getResponses());

        return clonedOperation;
    }

    private String getPropertyRef(Schema property) {
        /*
        if (property instanceof ArrayProperty &&
                ((ArrayProperty) property).getItems() != null) {
            return getPropertyRef(((ArrayProperty) property).getItems());
        } else if (property instanceof MapProperty &&
                ((MapProperty) property).getAdditionalProperties() != null) {
            return getPropertyRef(((MapProperty) property).getAdditionalProperties());
        } else if (property instanceof RefProperty) {
            return ((RefProperty) property).getSimpleRef();
        }*/
        if (property.get$ref() != null) {
            return property.get$ref();
        }
        return null;
    }

    private Set<String> getModelRef(Schema model) {
        /*
        if (model instanceof ArrayModel &&
                ((ArrayModel) model).getItems() != null) {
            String propertyRef = getPropertyRef(((ArrayModel) model).getItems());
            if (propertyRef != null) {
                return new HashSet<String>(Arrays.asList(propertyRef));
            }
        } else if (model instanceof ComposedModel &&
                ((ComposedModel) model).getAllOf() != null) {
            Set<String> refs = new LinkedHashSet<String>();
            ComposedModel cModel = (ComposedModel) model;
            for (Model ref: cModel.getAllOf()) {
                if (ref instanceof RefModel) {
                    refs.add(((RefModel)ref).getSimpleRef());
                }
            }
            return refs;
        } else if (model instanceof RefModel) {
            return new HashSet<String>(Arrays.asList(((RefModel) model).getSimpleRef()));
        }
        */
        if (model.get$ref() != null) {
            return new HashSet<>(Arrays.asList((model.get$ref())));
        }
        return null;
    }
}
