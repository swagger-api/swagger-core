package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.jaxrs.ext.SwaggerExtension;
import com.wordnik.swagger.jaxrs.ext.SwaggerExtensions;
import com.wordnik.swagger.jaxrs.PATCH;
import com.wordnik.swagger.jaxrs.utils.ParameterUtils;
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
import javax.ws.rs.FormParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.annotation.Annotation;
import java.util.*;

public class Reader {
  List<SwaggerExtension> EXTENSIONS = SwaggerExtensions.getExtensions();
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
        if(auth.value() != null && !"".equals(auth.value())) {
          SecurityRequirement security = new SecurityRequirement();
          security.setName(auth.value());
          AuthorizationScope[] scopes = auth.scopes();
          for(AuthorizationScope scope : scopes) {
            if(scope.scope() != null && !"".equals(scope.scope())) {
              security.addScope(scope.scope());
            }
          }
          securities.add(security);
        }
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
            for(SecurityRequirement security : securities)
              operation.security(security);
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
    if(methodLevelPath != null && !"/".equals(methodLevelPath.value())) {
      String methodPath = methodLevelPath.value();
      if(!methodPath.startsWith("/") && !b.toString().endsWith("/")) {
        b.append("/");
      }
      if(methodPath.endsWith("/")) {
        methodPath = methodPath.substring(0, methodPath.length() -1);
      }
      b.append(methodPath);
    }
    String output = b.toString();
    if(output.endsWith("/") && output.length() > 1)
      return output.substring(0, output.length() - 1);
    else
      return output;
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
      if(apiOperation.authorizations()!= null) {
        List<SecurityRequirement> securities = new ArrayList<SecurityRequirement>();
        for(Authorization auth : apiOperation.authorizations()) {
          if(auth.value() != null && !"".equals(auth.value())) {
            SecurityRequirement security = new SecurityRequirement();
            security.setName(auth.value());
            AuthorizationScope[] scopes = auth.scopes();
            for(AuthorizationScope scope : scopes) {
              SecurityDefinition definition = new SecurityDefinition(auth.type());
              if(scope.scope() != null && !"".equals(scope.scope())) {
                security.addScope(scope.scope());
                definition.scope(scope.scope(), scope.description());
              }
            }
            securities.add(security);
          }
        }
        if(securities.size() > 0) {
          for(SecurityRequirement sec : securities)
            operation.security(sec);
        }
      }
    }

    if(responseClass == null) {
      // pick out response from method declaration
      Type t = method.getGenericReturnType();
      responseClass = method.getReturnType();
      if(!responseClass.equals(java.lang.Void.class) && !"void".equals(responseClass.toString())) {
        Map<String, Model> models = ModelConverters.getInstance().readAll(t);
      }
    }
    if(responseClass != null
      && !responseClass.equals(java.lang.Void.class)
      && !responseClass.equals(javax.ws.rs.core.Response.class)) {
      if(isPrimitive(responseClass)) {
        Property responseProperty = null;
        Property property = ModelConverters.getInstance().readAsProperty(responseClass);
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
      else if(!responseClass.equals(java.lang.Void.class) && !"void".equals(responseClass.toString())) {
        Map<String, Model> models = ModelConverters.getInstance().read(responseClass);
        if(models.size() == 0) {
          Property p = ModelConverters.getInstance().readAsProperty(responseClass);
          operation.response(200, new Response()
            .description("successful operation")
            .schema(p));
        }
        for(String key: models.keySet()) {
          Property responseProperty = null;

          if("list".equalsIgnoreCase(responseContainer))
            responseProperty = new ArrayProperty(new RefProperty().asDefault(key));
          else if("map".equalsIgnoreCase(responseContainer))
            responseProperty = new MapProperty(new RefProperty().asDefault(key));
          else
            responseProperty = new RefProperty().asDefault(key);
          operation.response(200, new Response()
            .description("successful operation")
            .schema(responseProperty));
          swagger.model(key, models.get(key));
        }
        models = ModelConverters.getInstance().readAll(responseClass);
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
          Map<String, Model> models = ModelConverters.getInstance().read(responseClass);
          for(String key: models.keySet()) {
            response.schema(new RefProperty().asDefault(key));
            swagger.model(key, models.get(key));
          }
          models = ModelConverters.getInstance().readAll(responseClass);
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
    	List<Parameter> parameters = getParameters(cls, type, paramAnnotations[i]);

      for(Parameter parameter : parameters) {
        operation.parameter(parameter);
      }
    }
    if(operation.getResponses() == null) {
      operation.defaultResponse(new Response().description("successful operation"));
    }
    return operation;
  }

  List<Parameter> getParameters(Class<?> cls, Type type, Annotation[] annotations) {
    // look for path, query
    boolean isArray = ParameterUtils.isMethodArgumentAnArray(cls, type);
    Iterator<SwaggerExtension> chain = SwaggerExtensions.chain();
    List<Parameter> parameters = null;

    if(chain.hasNext()) {
      SwaggerExtension extension = chain.next();
      parameters = extension.extractParameters(annotations, cls, isArray, chain);
    }

    for(Parameter parameter : parameters) {
      ParameterProcessor.applyAnnotations(swagger, parameter, cls, annotations, isArray);
    }

    return parameters;
  }

  boolean isPrimitive(Class<?> cls) {
    boolean out = false;

    Property property = ModelConverters.getInstance().readAsProperty(cls);
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
    else if("file".equals(property.getType()))
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
