package io.swagger.core.filter;

import io.swagger.models.OpenAPI;
import io.swagger.models.Operation;
import io.swagger.models.PathItem;
import io.swagger.models.media.Schema;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.responses.Response;
import io.swagger.models.tags.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SpecFilter {
    public OpenAPI filter(OpenAPI swagger, SwaggerSpecFilter filter, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        OpenAPI clone = new OpenAPI();
        clone.info(swagger.getInfo())
                .tags(swagger.getTags() == null ? null : new ArrayList<Tag>(swagger.getTags())
//                .host(swagger.getHost())
//                .basePath(swagger.getBasePath())
//                .schemes(swagger.getSchemes())
//                .consumes(swagger.getConsumes())
//                .produces(swagger.getProduces())
//                .externalDocs(swagger.getExternalDocs())
//                .vendorExtensions(swagger.getVendorExtensions()
                );

        final Set<String> filteredTags = new HashSet<String>();
        final Set<String> allowedTags = new HashSet<String>();
        for (String resourcePath : swagger.getPaths().keySet()) {
            PathItem path = swagger.getPaths().get(resourcePath);
            Map<String, Operation> ops = new HashMap<String, Operation>();
            ops.put("get", path.getGet());
            ops.put("head", path.getHead());
            ops.put("put", path.getPut());
            ops.put("post", path.getPost());
            ops.put("delete", path.getDelete());
            ops.put("patch", path.getPatch());
            ops.put("options", path.getOptions());

            PathItem clonedPath = new PathItem();
            for (String key : ops.keySet()) {
                Operation op = ops.get(key);
                if (op != null) {
                    final Set<String> tags;
                    if (filter.isOperationAllowed(op, params, cookies, headers)) {
                        // TODO
//                        clonedPath.set(key, filterOperation(filter, op, params, cookies, headers));
                        tags = allowedTags;
                    } else {
                        tags = filteredTags;
                    }
                    if (op.getTags() != null) {
                        tags.addAll(op.getTags());
                    }
                }
            }
            // TODO
//            if (!clonedPath.isEmpty()) {
//                clone.path(resourcePath, clonedPath);
//            }
        }
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

        Map<String, Schema> definitions = filterDefinitions(filter, swagger.getComponents().getSchemas(), params, cookies, headers);
        // TODO
//        clone.setSecurityDefinitions(swagger.getSecurityDefinitions());
        clone.setSecurity(swagger.getSecurity());
        // TODO
//        clone.setDefinitions(definitions);

        // isRemovingUnreferencedDefinitions is not defined in SwaggerSpecFilter to avoid breaking compatibility with
        // existing client filters directly implementing SwaggerSpecFilter.
        if (filter instanceof AbstractSpecFilter) {
            if (((AbstractSpecFilter)filter).isRemovingUnreferencedDefinitions()) {
                clone = removeBrokenReferenceDefinitions (clone);
            }
        }

        return clone;
    }

    private OpenAPI removeBrokenReferenceDefinitions (OpenAPI swagger) {

        if (swagger.getComponents().getSchemas() == null || swagger.getComponents().getSchemas().isEmpty()) return swagger;

        Set<String> referencedDefinitions =  new TreeSet<String>();

        if (swagger.getComponents().getResponses() != null) {
            for (Response response: swagger.getComponents().getResponses().values()) {
                // TODO
//                String propertyRef = getPropertyRef(response.getSchema());
//                if (propertyRef != null) {
//                    referencedDefinitions.add(propertyRef);
//                }
            }
        }
        if (swagger.getComponents().getParameters() != null) {
            for (Parameter p: swagger.getComponents().getParameters().values()) {
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
        if (swagger.getPaths() != null) {
            for (PathItem path : swagger.getPaths().values()) {
                if (path.getParameters() != null) {
                    for (Parameter p: path.getParameters()) {
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
                            for (Response response: op.getResponses().values()) {
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

        if (swagger.getComponents().getSchemas() != null) {
            Set<String> nestedReferencedDefinitions =  new TreeSet<String>();
            for (String ref : referencedDefinitions){
                locateReferencedDefinitions(ref, nestedReferencedDefinitions, swagger);
            }
            referencedDefinitions.addAll(nestedReferencedDefinitions);
            swagger.getComponents().getSchemas().keySet().retainAll(referencedDefinitions);
        }
        
        return swagger;
    }

    private void locateReferencedDefinitions (Map<String, Schema> props, Set<String> nestedReferencedDefinitions, OpenAPI swagger) {
        if (props == null) return;
        for (String keyProp: props.keySet()) {
            Schema p = props.get(keyProp);
            String ref = getPropertyRef(p);
            if (ref != null) {
                locateReferencedDefinitions(ref, nestedReferencedDefinitions, swagger);
            }
        }
    }

    private void locateReferencedDefinitions(String ref, Set<String> nestedReferencedDefinitions, OpenAPI swagger) {
        // if not already processed so as to avoid infinite loops
        if (!nestedReferencedDefinitions.contains(ref)) {
            nestedReferencedDefinitions.add(ref);

            Schema model = swagger.getComponents().getSchemas().get(ref);
            if (model != null) {
                locateReferencedDefinitions(model.getProperties(), nestedReferencedDefinitions, swagger);
            }
        }
    }

    public Map<String, Schema> filterDefinitions(SwaggerSpecFilter filter, Map<String, Schema> definitions, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (definitions == null) {
            return null;
        }
        Map<String, Schema> clonedDefinitions = new LinkedHashMap<String, Schema>();

        for (String key : definitions.keySet()) {
            Schema definition = definitions.get(key);
            Map<String, Schema> clonedProperties = new LinkedHashMap<String, Schema>();
            if (definition.getProperties() != null) {
                for (String propName : definition.getProperties().keySet()) {
                    Schema property = definition.getProperties().get(propName);
                    if (property != null) {
                        boolean shouldInclude = filter.isPropertyAllowed(definition, property, propName, params, cookies, headers);
                        if (shouldInclude) {
                            clonedProperties.put(propName, property);
                        }
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

    public Operation filterOperation(SwaggerSpecFilter filter, Operation op, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
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
                if (filter.isParamAllowed(param, op, params, cookies, headers)) {
                    clonedParams.add(param);
                }
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
        if(property.getRef() != null) {
            return property.getRef();
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
        if(model.getRef() != null) {
            return new HashSet<String>(Arrays.asList((model.getRef())));
        }
        return null;
    }
}
