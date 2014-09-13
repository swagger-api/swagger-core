package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.jaxrs.PATCH;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.parameters.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.util.Json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import java.lang.reflect.*;
import java.lang.annotation.Annotation;
import java.util.*;

public class Reader {
  Swagger swagger;
  static ObjectMapper m = Json.mapper();

  public Reader(Swagger swagger) {
    this.swagger = swagger;
  }

  public Swagger read(Set<Class<?>> classes) {
    for(Class cls: classes)
      read(cls);
    return swagger;
  }

  public Swagger getSwagger() {
    return this.swagger;
  }

  public Swagger read(Class cls) {
    if(swagger == null)
      swagger = new Swagger();
    Api api = (Api) cls.getAnnotation(Api.class);
    Map<String, SecurityScope> globalScopes = new HashMap<String, SecurityScope>();
    
    javax.ws.rs.Path apiPath = (javax.ws.rs.Path) cls.getAnnotation(javax.ws.rs.Path.class);
    String[] apiConsumes = new String[0];
    String[] apiProduces = new String[0];

    Annotation annotation;
    annotation = cls.getAnnotation(Consumes.class);
    if(annotation != null) {
      apiConsumes = ((Consumes)annotation).value();
    }

    annotation = cls.getAnnotation(Produces.class);
    if(annotation != null) {
      apiProduces = ((Produces)annotation).value();
    }

    if(api != null) {
      // the value will be used as a tag for 2.0
      String tag = api.value().replace("/", "");
      String description = api.description();
      String basePath = api.basePath();
      int position = api.position();
      String produces = api.produces();
      String consumes = api.consumes();
      String schems = api.protocols();
      Authorization[] authorizations = api.authorizations();

      List<SecurityRequirement> securities = new ArrayList<SecurityRequirement>();
      for(Authorization auth : authorizations) {
        SecurityRequirement security = new SecurityRequirement();
        security.setName(auth.value());
        AuthorizationScope[] scopes = auth.scopes();
        for(AuthorizationScope scope : scopes) {
          if(scope.scope() != null && !"".equals(scope.scope())) {
            security.addScope(scope.scope());
            swagger.addSecurityDefinition(auth.value(),
              new SecurityDefinition(auth.value(), auth.type())
                .scope(new SecurityScope(scope.scope(), scope.description())));
          }
        }
        securities.add(security);
      }

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
            if(operation.getConsumes() == null)
              for(String mediaType: apiConsumes)
                operation.consumes(mediaType);
            if(operation.getProduces() == null)
              for(String mediaType: apiProduces)
                operation.produces(mediaType);

            operation.tag(tag);
            if(securities.size() > 0) 
              operation.security(securities);
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
      if(methodPath.endsWith("/")) {
        methodPath = methodPath.substring(0, methodPath.length() -1);
      }
      b.append(methodPath);
    }
    return b.toString();
  }

  public Operation parseMethod(Method method) {
    Operation operation = new Operation();

    ApiOperation apiOperation = (ApiOperation) method.getAnnotation(ApiOperation.class);
    ApiResponses responseAnnotation = method.getAnnotation(ApiResponses.class);

    String operationId = method.getName();
    String responseContainer = null;

    Class<?> responseClass = null;
    if(apiOperation != null) {
      if(!"".equals(apiOperation.nickname()))
        operationId = method.getName();
      operation
        .summary(apiOperation.value())
        .description(apiOperation.notes());
      if(apiOperation.response() != null && !Void.class.equals(apiOperation.response())) {
        responseClass = apiOperation.response();
      }
      if(!"".equals(apiOperation.responseContainer()))
        responseContainer = apiOperation.responseContainer();
    }
    else {
      // pick out response from method declaration
      responseClass = method.getReturnType();
    }
    if(responseClass != null && !responseClass.equals(java.lang.Void.class)) {
      if(isPrimitive(responseClass)) {
        Property responseProperty = null;
        Property property = ModelConverters.readAsProperty(responseClass);
        if(property != null) {
          if("list".equalsIgnoreCase(responseContainer))
            responseProperty = new ArrayProperty(property);
          else if("map".equalsIgnoreCase(responseContainer))
            responseProperty = new MapProperty(property);
          else
            responseProperty = property;
          operation.response(200, new Response()
            .description("successful operation")
            .schema(responseProperty));
        }
      }
      else {
        Map<String, Model> models = ModelConverters.read(responseClass);
        for(String key: models.keySet()) {
          Property responseProperty = null;

          if("list".equalsIgnoreCase(responseContainer))
            responseProperty = new ArrayProperty(new RefProperty().asDefault(key));
          else
            responseProperty = new RefProperty().asDefault(key);
          operation.response(200, new Response()
            .description("successful operation")
            .schema(responseProperty));
          swagger.model(key, models.get(key));
        }
        models = ModelConverters.readAll(responseClass);
        for(String key: models.keySet()) {
          swagger.model(key, models.get(key));
        }
      }
    }

    operation.operationId(operationId);

    Annotation annotation;
    annotation = method.getAnnotation(Consumes.class);
    if(annotation != null) {
      String[] apiConsumes = ((Consumes)annotation).value();
      for(String mediaType: apiConsumes)
        operation.consumes(mediaType);
    }

    annotation = method.getAnnotation(Produces.class);
    if(annotation != null) {
      String[] apiProduces = ((Produces)annotation).value();
      for(String mediaType: apiProduces)
        operation.produces(mediaType);
    }


    List<ApiResponse> apiResponses = new ArrayList<ApiResponse>();
    if(responseAnnotation != null) {
      for(ApiResponse apiResponse: responseAnnotation.value()) {
        Response response = new Response()
          .description(apiResponse.message());

        if(apiResponse.code() == 0)
          operation.defaultResponse(response);
        else
          operation.response(apiResponse.code(), response);

        responseClass = apiResponse.response();
        if(responseClass != null && !responseClass.equals(java.lang.Void.class)) {
          Map<String, Model> models = ModelConverters.read(responseClass);
          for(String key: models.keySet()) {
            response.schema(new RefProperty().asDefault(key));
            swagger.model(key, models.get(key));
          }
          models = ModelConverters.readAll(responseClass);
          for(String key: models.keySet()) {
            swagger.model(key, models.get(key));
          }
        }
      }
    }
    boolean isDeprecated = false;
    annotation = method.getAnnotation(Deprecated.class);
    if(annotation != null)
      isDeprecated = true;

    boolean hidden = false;
    if(apiOperation != null)
      hidden = apiOperation.hidden();

    // process parameters
    Class[] parameterTypes = method.getParameterTypes();
    Type[] genericParameterTypes = method.getGenericParameterTypes();
    Annotation[][] paramAnnotations = method.getParameterAnnotations();
    // paramTypes = method.getParameterTypes
    // genericParamTypes = method.getGenericParameterTypes
    for(int i = 0; i < parameterTypes.length; i++) {
    	Class<?> cls = parameterTypes[i];
    	Type type = genericParameterTypes[i];
    	Parameter parameter = getParameter(cls, type, paramAnnotations[i]);
      if(parameter != null) {
        // add it
        operation.parameter(parameter);
      }
    }
    if(operation.getResponses() == null) {
      operation.defaultResponse(new Response().description("successful operation"));
    }
    return operation;
  }

  Parameter getParameter(Class<?> cls, Type type, Annotation[] annotations) {
    // look for path, query
    Parameter parameter = null;
    String defaultValue;
    boolean allowMultiple;
    String allowableValues;
    boolean isArray = false;
    
    // see if it's a collection type
    if(type instanceof ParameterizedType){
      ParameterizedType aType = (ParameterizedType) type;
      Type[] parameterArgTypes = aType.getActualTypeArguments();
      for(Type parameterArgType : parameterArgTypes){
      	if(cls.isAssignableFrom(List.class)){
      		isArray = true;
      	}
        Class<?> parameterArgClass = (Class<?>) parameterArgType;
        cls = parameterArgClass;
      }
    }

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
          allowMultiple = param.allowMultiple() || isArray;
          if(allowMultiple == true) {
            if(parameter instanceof PathParameter) {
              PathParameter p = (PathParameter) parameter;
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("default");
            }
            else if(parameter instanceof QueryParameter) {
              QueryParameter p = (QueryParameter) parameter;
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("default");
            }
            else if(parameter instanceof HeaderParameter) {
              HeaderParameter p = (HeaderParameter) parameter;
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("default");
            }
            else if(parameter instanceof CookieParameter) {
              CookieParameter p = (CookieParameter) parameter;
              Property items = PropertyBuilder.build(p.getType(), p.getFormat(), null);
              p.items(items)
                .array(true)
                .collectionFormat("default");
            }
          }

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
              Map<String, Model> models = ModelConverters.read(innerType);
              if(models.size() > 0) {
                for(String name: models.keySet()) {
                  if(name.indexOf("java.util") == -1) {
                    bp.setSchema(
                      new ArrayModel().items(new RefProperty().asDefault(name))
                    );
                    swagger.addDefinition(name, models.get(name));
                  }
                }
              }
              models = ModelConverters.readAll(innerType);
              for(String key : models.keySet()) {
                swagger.model(key, models.get(key));
              }
            }
            else {
              bp.setSchema(new ArrayModel().items(innerProperty));
            }
          }
          else {
            Map<String, Model> models = ModelConverters.read(cls);
            if(models.size() > 0) {
              for(String name: models.keySet()) {
                if(name.indexOf("java.util") == -1) {
                  if(isArray)
                    bp.setSchema(new ArrayModel().items(new RefProperty().asDefault(name)));
                  else
                    bp.setSchema(new RefModel().asDefault(name));
                  swagger.addDefinition(name, models.get(name));
                }
              }
            }
            models = ModelConverters.readAll(cls);
            for(String key : models.keySet()) {
              swagger.model(key, models.get(key));
            }
          }
          parameter = bp;
        }
      }
    }
    return parameter;
  }

  boolean isPrimitive(Class<?> cls) {
    boolean out = false;

    Property property = ModelConverters.readAsProperty(cls);
    if(property == null)
      out = false;
    if("integer".equals(property.getType()))
      out = true;
    else if("string".equals(property.getType()))
      out = true;
    else if("number".equals(property.getType()))
      out = true;
    else if("boolean".equals(property.getType()))
      out = true;
    else if("array".equals(property.getType()))
      out = true;
    else if("File".equals(property.getType()))
      out = true;
    return out;
  }

  String getHttpMethod(ApiOperation apiOperation, Method method) {
    if(apiOperation.httpMethod() != null && !"".equals(apiOperation.httpMethod()))
      return apiOperation.httpMethod();
    if(method.getAnnotation(javax.ws.rs.GET.class) != null)
      return "get";
    if(method.getAnnotation(javax.ws.rs.PUT.class) != null)
      return "put";
    if(method.getAnnotation(javax.ws.rs.POST.class) != null)
      return "post";
    if(method.getAnnotation(javax.ws.rs.DELETE.class) != null)
      return "delete";
    if(method.getAnnotation(javax.ws.rs.OPTIONS.class) != null)
      return "options";
    if(method.getAnnotation(javax.ws.rs.HEAD.class) != null)
      return "head";
    if(method.getAnnotation(PATCH.class) != null)
      return "patch";

    return null;
  }
}