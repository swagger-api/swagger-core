package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.converter.ModelConverters;

import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.jaxrs.PATCH;

import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


import java.lang.reflect.*;
import java.lang.annotation.Annotation;
import java.util.*;

public class Reader {
  Swagger swagger;
  static ObjectMapper m = Json.mapper();

  public Swagger read(Set<Class<?>> classes) {
    for(Class cls: classes) {
      read(cls);
    }
    return swagger;
  }

  public Swagger getSwagger() {
    return this.swagger;
  }

  public Swagger read(Class cls) {
    if(swagger == null)
      swagger = new Swagger();
    Api api = (Api) cls.getAnnotation(Api.class);
    
    javax.ws.rs.Path apiPath = (javax.ws.rs.Path) cls.getAnnotation(javax.ws.rs.Path.class);
    Consumes apiConsumes;
    Produces apiProduces;

    Annotation annotation;
    annotation = cls.getAnnotation(Consumes.class);
    if(annotation != null) {
      apiConsumes = (Consumes)annotation;
    }

    annotation = cls.getAnnotation(Produces.class);
    if(annotation != null) {
      apiProduces = (Produces)annotation;
    }

    if(api != null) {
      // the value will be used as a tag for 2.0
      String tag = api.value();
      String description = api.description();
      String basePath = api.basePath();
      int position = api.position();
      String produces = api.produces();
      String consumes = api.consumes();
      String schems = api.protocols();

      // merge consumes, produces

      // look for method-level annotated properties

      // handle subresources by looking at return type

      // parse the method


      Method methods[] = cls.getMethods();
      for(Method method: methods) {
        ApiOperation apiOperation = (ApiOperation) method.getAnnotation(ApiOperation.class);
        javax.ws.rs.Path methodPath = method.getAnnotation(javax.ws.rs.Path.class);

        String operationPath = getPath(apiPath, methodPath);
        if(operationPath != null && apiOperation != null) {
          String httpMethod = getHttpMethod(apiOperation, method);

          // can't continue without the http method
          if(httpMethod == null)
            break;

          Operation operation = parseMethod(method);
          if(operation != null) {
            Path path = swagger.getPath(operationPath);
            if(path == null) {
              path = new Path();
              swagger.path(operationPath, path);
            }
            path.set(httpMethod, operation);
          }
        }
      }
    }
    return swagger;
  }

  String getPath(javax.ws.rs.Path classLevelPath, javax.ws.rs.Path methodLevelPath) {
    if(classLevelPath == null && methodLevelPath == null)
      return null;
    StringBuilder b = new StringBuilder();
    if(classLevelPath != null) {
      b.append(classLevelPath.value());
    }
    if(methodLevelPath != null) {
      String methodPath = methodLevelPath.value();
      if(!methodPath.startsWith("/") && !b.toString().endsWith("/")) {
        b.append("/");
      }
      b.append(methodPath);
    }
    return b.toString();
  }

  protected Operation parseMethod(Method method) {
    Operation operation = new Operation();

    ApiOperation apiOperation = (ApiOperation) method.getAnnotation(ApiOperation.class);
    ApiResponses responseAnnotation = method.getAnnotation(ApiResponses.class);

    List<ApiResponse> apiResponses = new ArrayList<ApiResponse>();
    if(responseAnnotation != null) {
      for(ApiResponse apiResponse: responseAnnotation.value()) {
        Response response = new Response()
          .description(apiResponse.message());

        if(apiResponse.code() == 0)
          operation.defaultResponse(response);
        else
          operation.response(apiResponse.code(), response);

        Class responseClass = apiResponse.response();
        if(responseClass != null && !responseClass.equals(java.lang.Void.class)) {
          Map<String, Model> models = ModelConverters.read(responseClass);
          for(String key: models.keySet()) {
            response.schema(new RefProperty().asDefault(key));
          }
        }
      }
    }
    boolean isDeprecated = false;
    Annotation annotation = method.getAnnotation(Deprecated.class);
    if(annotation != null)
      isDeprecated = true;

    boolean hidden = false;
    if(apiOperation != null)
      hidden = apiOperation.hidden();

    // process parameters
    Class[] parameterTypes = method.getParameterTypes();
    Annotation[][] paramAnnotations = method.getParameterAnnotations();
    // paramTypes = method.getParameterTypes
    // genericParamTypes = method.getGenericParameterTypes
    for(int i = 0; i < parameterTypes.length; i++) {
      Parameter parameter = getParameter(parameterTypes[i], paramAnnotations[i]);
      if(parameter != null) {
        // add it
        operation.parameter(parameter);
      }
    }
    return operation;
  }

  Parameter getParameter(Class cls, Annotation[] annotations) {
    // look for path, query
    Parameter parameter = null;
    String defaultValue;
    boolean allowMultiple;
    String allowableValues;

    for(Annotation annotation : annotations) {
      if(annotation instanceof QueryParam) {
        QueryParam param = (QueryParam) annotation;
        QueryParameter qp = new QueryParameter()
          .name(param.value());
        Property schema = ModelConverters.readAsProperty(cls);
        if(schema != null)
          qp.setProperty(schema);
        parameter = qp;
      }
      else if(annotation instanceof PathParam) {
        PathParam param = (PathParam) annotation;
        PathParameter pp = new PathParameter()
          .name(param.value());
        Property schema = ModelConverters.readAsProperty(cls);
        if(schema != null)
          pp.setProperty(schema);
        parameter = pp;
      }
      else if(annotation instanceof HeaderParam) {
        HeaderParam param = (HeaderParam) annotation;
        HeaderParameter hp = new HeaderParameter()
          .name(param.value());
        Property schema = ModelConverters.readAsProperty(cls);
        if(schema != null)
          hp.setProperty(schema);
        parameter = hp;
      }
      else if(annotation instanceof CookieParam) {
        CookieParam param = (CookieParam) annotation;
        CookieParameter hp = new CookieParameter()
          .name(param.value());
        Property schema = ModelConverters.readAsProperty(cls);
        if(schema != null)
          hp.setProperty(schema);
        parameter = hp;
      }
      else if(annotation instanceof DefaultValue) {
        DefaultValue defaultValueAnnotation = (DefaultValue) annotation;
        // TODO: not supported yet
        defaultValue = defaultValueAnnotation.value();
      }
      else {
        // System.out.println("unprocessed " + annotation);
      }
    }

    // lastly apply ApiParam
    for(Annotation annotation: annotations) {
      if(annotation instanceof ApiParam) {
        ApiParam param = (ApiParam) annotation;
        if(parameter != null) {
          // parameter.required(param.required());
          if(param.name() != null && !"".equals(param.name()))
            parameter.setName(param.name());
          parameter.setDescription(param.value());
          // parameter.setAccess(param.access());
          allowMultiple = param.allowMultiple();
          allowableValues = param.allowableValues();
          if(!"".equals(param.defaultValue()))
            defaultValue = param.defaultValue();
        }
        else {
          // must be a body param
          BodyParameter bp = new BodyParameter();
          if(param.name() != null && !"".equals(param.name()))
            bp.setName(param.name());
          else
            bp.setName("body");
          bp.setDescription(param.value());

          if(cls.isArray()) {
            Class innerType = cls.getComponentType();
            Property innerProperty = ModelConverters.readAsProperty(innerType);
            if(innerProperty == null) {
              Map<String, Model> models = ModelConverters.readAll(innerType);
              if(models.size() > 0) {
                for(String name: models.keySet()) {
                  if(name.indexOf("java.util") == -1) {
                    bp.setSchema(new RefModel().asDefault(name));
                    swagger.addDefinition(name, models.get(name));
                  }
                }
              }
              Json.printPretty(models);
            }
          }
          else {
            Map<String, Model> models = ModelConverters.readAll(cls);
            if(models.size() > 0) {
              for(String name: models.keySet()) {
                if(name.indexOf("java.util") == -1) {
                  bp.setSchema(new RefModel().asDefault(name));
                  swagger.addDefinition(name, models.get(name));
                }
              }
            }
          }
          parameter = bp;
        }
      }
    }
    return parameter;
  }

  String getHttpMethod(ApiOperation apiOperation, Method method) {
    if(apiOperation.httpMethod() != null && !"".equals(apiOperation.httpMethod()))
      return apiOperation.httpMethod();
    if(method.getAnnotation(javax.ws.rs.GET.class) != null) {
      return "get";
    }
    if(method.getAnnotation(javax.ws.rs.PUT.class) != null) {
      return "put";
    }
    if(method.getAnnotation(javax.ws.rs.POST.class) != null) {
      return "post";
    }
    if(method.getAnnotation(javax.ws.rs.DELETE.class) != null) {
      return "delete";
    }
    if(method.getAnnotation(javax.ws.rs.OPTIONS.class) != null) {
      return "options";
    }
    if(method.getAnnotation(javax.ws.rs.HEAD.class) != null) {
      return "head";
    }
    if(method.getAnnotation(PATCH.class) != null) {
      return "patch";
    }

    return null;
  }
}