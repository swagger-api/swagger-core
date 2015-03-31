/**
 *  Copyright 2015 Reverb Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.swagger.sample.util;

import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.Operation;
import com.wordnik.swagger.models.parameters.Parameter;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.model.ApiDescription;
import com.wordnik.swagger.core.filter.SwaggerSpecFilter;

import org.slf4j.*;

import java.util.Map;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
/**
 * 
 * The rules are maintained in simple map with key as path and a boolean value
 * indicating given path is secure or not. For method level security the key is
 * combination of http method and path .
 * 
 * If the resource or method is secure then it can only be viewed using a
 * secured api key
 * 
 * Note: Objective of this class is not to provide fully functional
 * implementation of authorization filter. This is only a sample demonstration
 * how API authorization filter works.
 * 
 */

public class ApiAuthorizationFilterImpl implements SwaggerSpecFilter {
  static Logger logger = LoggerFactory.getLogger(ApiAuthorizationFilterImpl.class);

  public boolean isOperationAllowed(
    Operation operation,
    ApiDescription api,
    Map<String, List<String>> params,
    Map<String, String> cookies,
    Map<String, List<String>> headers) {
    if(!api.getMethod().equals("get") || api.getPath().startsWith("/store"))
      return checkKey(params, headers);
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

  public boolean checkKey(Map<String, List<String>> params, Map<String, List<String>> headers) {
    String keyValue = null;
    if(params.containsKey("api_key"))
      keyValue = params.get("api_key").get(0);
    else {
      if(headers.containsKey("api_key"))
        keyValue = headers.get("api_key").get(0);
    }
    if("special-key".equals(keyValue))
      return true;
    else
      return false;
  }
}