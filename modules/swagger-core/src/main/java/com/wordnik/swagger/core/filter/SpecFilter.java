package com.wordnik.swagger.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.wordnik.swagger.model.ApiDescription;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.Operation;
import com.wordnik.swagger.models.Path;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.models.parameters.Parameter;
import com.wordnik.swagger.models.properties.Property;

public class SpecFilter {
  public Swagger filter(Swagger swagger, SwaggerSpecFilter filter, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
    Swagger clone = new Swagger();
    clone.info(swagger.getInfo())
      .tags(swagger.getTags())
      .host(swagger.getHost())
      .basePath(swagger.getBasePath())
      .schemes(swagger.getSchemes())
      .consumes(swagger.getConsumes())
      .produces(swagger.getProduces())
      .externalDocs(swagger.getExternalDocs());

    for(String resourcePath : swagger.getPaths().keySet()) {
      Path path = swagger.getPaths().get(resourcePath);
      Map<String, Operation> ops = new HashMap<String, Operation>();
      ops.put("get", path.getGet());
      ops.put("put", path.getPut());
      ops.put("post", path.getPost());
      ops.put("delete", path.getDelete());
      ops.put("patch", path.getPatch());
      ops.put("options", path.getOptions());

      Path clonedPath = new Path();
      for(Map.Entry<String, Operation> entry: ops.entrySet()) {
        final Operation op = entry.getValue();
        if(op != null) {
          final String key = entry.getKey();
          ApiDescription desc = new ApiDescription(resourcePath, key);
          if(filter.isOperationAllowed(op, desc, params, cookies, headers)) {
            clonedPath.set(key, filterOperation(filter, op, desc, params, cookies, headers));
          }
        }
      }
      if(!clonedPath.isEmpty())
        clone.path(resourcePath, clonedPath);
    }

    clone.setSecurityDefinitions(swagger.getSecurityDefinitions());
    clone.setDefinitions(filterDefinitions(filter, swagger.getDefinitions(), params, cookies, headers));
    return clone;
  }

  public Map<String, Model> filterDefinitions(SwaggerSpecFilter filter, Map<String, Model> definitions, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
    if (definitions == null) {
      return null;
    }
    final Map<String, Model> clonedDefinitions = new LinkedHashMap<String, Model>();
    for(Map.Entry<String, Model> entry: definitions.entrySet()) {
      final Model definition = entry.getValue();
      Map<String, Property> clonedProperties = new LinkedHashMap<String, Property>();
      for(String propName: definition.getProperties().keySet()) {
        Property property = definition.getProperties().get(propName);
        if(property != null) {
          boolean shouldInclude = filter.isPropertyAllowed(definition, property, propName, params, cookies, headers);
          if(shouldInclude) {
            clonedProperties.put(propName, property);
          }
        }
      }
      Model clonedModel = (Model)definition.clone();
      clonedModel.getProperties().clear();
      clonedModel.setProperties(clonedProperties);
      clonedDefinitions.put(entry.getKey(), clonedModel);
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
    if(op.getParameters() != null) {
      for(Parameter param : op.getParameters()) {
        if(filter.isParamAllowed(param, op, api, params, cookies, headers)) {
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