package com.wordnik.swagger.core.filter;

import com.wordnik.swagger.core.filter.SwaggerSpecFilter;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.model.ApiDescription;

import com.wordnik.swagger.util.Json;

import java.util.*;

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
      for(String key: ops.keySet()) {
        Operation op = ops.get(key);
        if(op != null) {
          ApiDescription desc = new ApiDescription(resourcePath, key);
          if(filter.isOperationAllowed(op, desc, params, cookies, headers)) {
            clonedPath.set(key, filterOperation(filter, op, desc, params, cookies, headers));
          }
        }
      }
      if(!clonedPath.isEmpty())
        clone.path(resourcePath, clonedPath);
    }

    Map<String, Model> definitions = filterDefinitions(filter, swagger.getDefinitions(), params, cookies, headers);
    clone.setSecurityDefinitions(swagger.getSecurityDefinitions());
    clone.setDefinitions(definitions);
    return clone;
  }

  public Map<String, Model> filterDefinitions(SwaggerSpecFilter filter, Map<String, Model> definitions, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
    Map<String, Model> clonedDefinitions = new LinkedHashMap<String, Model>();

    for(String key: definitions.keySet()) {
      Model definition = definitions.get(key);
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