package com.wordnik.swagger.core.filter;

import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.model.ApiDescription;

import java.util.Map;
import java.util.List;

public abstract class AbstractSpecFilter implements SwaggerSpecFilter {
  public boolean isOperationAllowed(
    Operation operation,
    ApiDescription api,
    Map<String, List<String>> params,
    Map<String, String> cookies,
    Map<String, List<String>> headers) {
    return true;
  }

  public boolean isParamAllowed(
    Parameter parameter,
    Operation operation,
    ApiDescription api,
    Map<String, List<String>> params,
    Map<String, String> cookies,
    Map<String, List<String>> headers) {
    return true;
  }

  public boolean isPropertyAllowed(
    Model model,
    Property property,
    String propertyName,
    Map<String, List<String>> params,
    Map<String, String> cookies,
    Map<String, List<String>> headers) {
    return true;
  }
}