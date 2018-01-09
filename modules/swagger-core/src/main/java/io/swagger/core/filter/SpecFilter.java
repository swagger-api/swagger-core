package io.swagger.core.filter;

import io.swagger.model.ApiDescription;
import io.swagger.models.ArrayModel;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.RefModel;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SpecFilter {
    public Swagger filter(Swagger swagger, SwaggerSpecFilter filter, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        Swagger clone = new Swagger();
        clone.info(swagger.getInfo())
                .tags(swagger.getTags() == null ? null : new ArrayList<Tag>(swagger.getTags()))
                .host(swagger.getHost())
                .basePath(swagger.getBasePath())
                .schemes(swagger.getSchemes())
                .consumes(swagger.getConsumes())
                .produces(swagger.getProduces())
                .externalDocs(swagger.getExternalDocs())
                .vendorExtensions(swagger.getVendorExtensions());

        final Set<String> filteredTags = new HashSet<String>();
        final Set<String> allowedTags = new HashSet<String>();
        for (String resourcePath : swagger.getPaths().keySet()) {
            Path path = swagger.getPaths().get(resourcePath);
            Map<String, Operation> ops = new HashMap<String, Operation>();
            ops.put("get", path.getGet());
            ops.put("head", path.getHead());
            ops.put("put", path.getPut());
            ops.put("post", path.getPost());
            ops.put("delete", path.getDelete());
            ops.put("patch", path.getPatch());
            ops.put("options", path.getOptions());

            Path clonedPath = new Path();
            for (String key : ops.keySet()) {
                Operation op = ops.get(key);
                if (op != null) {
                    ApiDescription desc = new ApiDescription(resourcePath, key);
                    final Set<String> tags;
                    if (filter.isOperationAllowed(op, desc, params, cookies, headers)) {
                        clonedPath.set(key, filterOperation(filter, op, desc, params, cookies, headers));
                        tags = allowedTags;
                    } else {
                        tags = filteredTags;
                    }
                    if (op.getTags() != null) {
                        tags.addAll(op.getTags());
                    }
                }
            }
            if (!clonedPath.isEmpty()) {
                clone.path(resourcePath, clonedPath);
            }
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

        Map<String, Model> definitions = filterDefinitions(filter, swagger.getDefinitions(), params, cookies, headers);
        clone.setSecurityDefinitions(swagger.getSecurityDefinitions());
        clone.setSecurity(swagger.getSecurity());
        clone.setDefinitions(definitions);

        // isRemovingUnreferencedDefinitions is not defined in SwaggerSpecFilter to avoid breaking compatibility with
        // existing client filters directly implementing SwaggerSpecFilter.
        if (filter instanceof AbstractSpecFilter) {
            if (((AbstractSpecFilter)filter).isRemovingUnreferencedDefinitions()) {
                clone = removeBrokenReferenceDefinitions (clone);
            }
        }

        return clone;
    }

    private Swagger removeBrokenReferenceDefinitions (Swagger swagger) {

        if (swagger.getDefinitions() == null || swagger.getDefinitions().isEmpty()) return swagger;

        Set<String> referencedDefinitions =  new TreeSet<String>();

        if (swagger.getResponses() != null) {
            for (Response response: swagger.getResponses().values()) {
                String propertyRef = getPropertyRef(response.getSchema());
                if (propertyRef != null) {
                    referencedDefinitions.add(propertyRef);
                }
            }
        }
        if (swagger.getParameters() != null) {
            for (Parameter p: swagger.getParameters().values()) {
                if (p instanceof BodyParameter) {
                    BodyParameter bp = (BodyParameter) p;
                    Set<String>  modelRef = getModelRef(bp.getSchema());
                    if (modelRef != null) {
                        referencedDefinitions.addAll(modelRef);
                    }
                }
            }
        }
        if (swagger.getPaths() != null) {
            for (Path path : swagger.getPaths().values()) {
                if (path.getParameters() != null) {
                    for (Parameter p: path.getParameters()) {
                        if (p instanceof BodyParameter) {
                            BodyParameter bp = (BodyParameter) p;
                            Set<String>  modelRef = getModelRef(bp.getSchema());
                            if (modelRef != null) {
                                referencedDefinitions.addAll(modelRef);
                            }
                        }
                    }
                }
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
            }
        }

        if (swagger.getDefinitions() != null) {
            Set<String> nestedReferencedDefinitions =  new TreeSet<String>();
            for (String ref : referencedDefinitions){
                locateReferencedDefinitions(ref, nestedReferencedDefinitions, swagger);
            }
            referencedDefinitions.addAll(nestedReferencedDefinitions);
            swagger.getDefinitions().keySet().retainAll(referencedDefinitions);
        }
        
        return swagger;
    }

    private void locateReferencedDefinitions (Map<String, Property> props, Set<String> nestedReferencedDefinitions, Swagger swagger) {
        if (props == null) return;
        for (String keyProp: props.keySet()) {
            Property p = props.get(keyProp);
            String ref = getPropertyRef(p);
            if (ref != null) {
                locateReferencedDefinitions(ref, nestedReferencedDefinitions, swagger);
            }
        }
    }

    private void locateReferencedDefinitions(String ref, Set<String> nestedReferencedDefinitions, Swagger swagger) {
        // if not already processed so as to avoid infinite loops
        if (!nestedReferencedDefinitions.contains(ref)) {
            nestedReferencedDefinitions.add(ref);
            Model model = swagger.getDefinitions().get(ref);
            if (model != null) {
                locateReferencedDefinitions(model.getProperties(), nestedReferencedDefinitions, swagger);
            }
        }
    }

    public Map<String, Model> filterDefinitions(SwaggerSpecFilter filter, Map<String, Model> definitions, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        if (definitions == null) {
            return null;
        }
        Map<String, Model> clonedDefinitions = new LinkedHashMap<String, Model>();

        for (String key : definitions.keySet()) {
            Model definition = definitions.get(key);
            Map<String, Property> clonedProperties = new LinkedHashMap<String, Property>();
            if (definition.getProperties() != null) {
                for (String propName : definition.getProperties().keySet()) {
                    Property property = definition.getProperties().get(propName);
                    if (property != null) {
                        boolean shouldInclude = filter.isPropertyAllowed(definition, property, propName, params, cookies, headers);
                        if (shouldInclude) {
                            clonedProperties.put(propName, property);
                        }
                    }
                }
            }
            Model clonedModel = (Model) definition.clone();
            if (clonedModel.getProperties() != null) {
                clonedModel.getProperties().clear();
            }
            if( definition.getVendorExtensions() != null && clonedModel.getVendorExtensions() != null ){
                clonedModel.getVendorExtensions().putAll( definition.getVendorExtensions());
            }

            clonedModel.setProperties(clonedProperties);
            clonedDefinitions.put(key, clonedModel);
        }
        return clonedDefinitions;
    }

    public Operation filterOperation(SwaggerSpecFilter filter, Operation op, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        Operation clonedOperation = new Operation()
                .summary(op.getSummary())
                .description(op.getDescription())
                .operationId(op.getOperationId())
                .schemes(op.getSchemes())
                .consumes(op.getConsumes())
                .produces(op.getProduces())
                .tags(op.getTags())
                .externalDocs(op.getExternalDocs())
                .vendorExtensions(op.getVendorExtensions())
                .deprecated(op.isDeprecated());

        List<Parameter> clonedParams = new ArrayList<Parameter>();
        if (op.getParameters() != null) {
            for (Parameter param : op.getParameters()) {
                if (filter.isParamAllowed(param, op, api, params, cookies, headers)) {
                    clonedParams.add(param);
                }
            }
        }
        clonedOperation.setParameters(clonedParams);
        clonedOperation.setSecurity(op.getSecurity());
        clonedOperation.setResponses(op.getResponses());

        return clonedOperation;
    }

    private String getPropertyRef(Property property) {
        if (property instanceof ArrayProperty &&
                ((ArrayProperty) property).getItems() != null) {
            return getPropertyRef(((ArrayProperty) property).getItems());
        } else if (property instanceof MapProperty &&
                ((MapProperty) property).getAdditionalProperties() != null) {
            return getPropertyRef(((MapProperty) property).getAdditionalProperties());
        } else if (property instanceof RefProperty) {
            return ((RefProperty) property).getSimpleRef();
        }
        return null;
    }

    private Set<String> getModelRef(Model model) {
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
        return null;
    }
}
