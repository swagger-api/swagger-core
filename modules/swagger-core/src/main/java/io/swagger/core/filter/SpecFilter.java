package io.swagger.core.filter;

import io.swagger.model.ApiDescription;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                .externalDocs(swagger.getExternalDocs());

        final Set<String> filteredTags = new HashSet<String>();
        final Set<String> allowedTags = new HashSet<String>();
        for (String resourcePath : swagger.getPaths().keySet()) {
            Path path = swagger.getPaths().get(resourcePath);
            Map<String, Operation> ops = new HashMap<String, Operation>();
            ops.put("get", path.getGet());
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
        clone.setDefinitions(definitions);
        return clone;
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
                .externalDocs(op.getExternalDocs());

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
}