package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.Authorization;
import com.wordnik.swagger.annotations.AuthorizationScope;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.HttpMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.annotation.Annotation;
import java.util.*;

public class Reader {
  Logger LOGGER = LoggerFactory.getLogger(Reader.class);

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
    return read(cls, "", null, false, new String[0], new String[0], new HashMap<String, Tag>(), new ArrayList<Parameter>());
  }

  protected Swagger read(Class<?> cls, String parentPath, String parentMethod, boolean readHidden, String[] parentConsumes, String[] parentProduces, Map<String, Tag> parentTags, List<Parameter> parentParameters) {
    if(swagger == null)
      swagger = new Swagger();
    Api api = (Api) cls.getAnnotation(Api.class);
    Map<String, SecurityScope> globalScopes = new HashMap<String, SecurityScope>();

    javax.ws.rs.Path apiPath = (javax.ws.rs.Path) cls.getAnnotation(javax.ws.rs.Path.class);
    String[] apiConsumes = new String[0];
    String[] apiProduces = new String[0];

    // only read if allowing hidden apis OR api is not marked as hidden
    if((api != null && readHidden) || (api != null && !api.hidden())) {
      // the value will be used as a tag for 2.0 UNLESS a Tags annotation is present
      Set<String> tagStrings = extractTags(api);
      Map<String, Tag> tags = new HashMap<String, Tag>();
      for(String tagString : tagStrings) {
        Tag tag = new Tag().name(tagString);
        tags.put(tagString, tag);
      }
      if(parentTags != null)
        tags.putAll(parentTags);
      for(String tagName: tags.keySet()) {
        swagger.tag(tags.get(tagName));
      }

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
      for(Method method : methods) {
        ApiOperation apiOperation = (ApiOperation) method.getAnnotation(ApiOperation.class);
        javax.ws.rs.Path methodPath = method.getAnnotation(javax.ws.rs.Path.class);

        String operationPath = getPath(apiPath, methodPath, parentPath);
        if(operationPath != null && apiOperation != null) {
          String [] pps = operationPath.split("/");
          String [] pathParts = new String[pps.length];
          Map<String, String> regexMap = new HashMap<String, String>();

          for(int i = 0; i < pps.length; i++) {
            String p = pps[i];
            if(p.startsWith("{")) {
              int pos = p.indexOf(":");
              if(pos > 0) {
                String left = p.substring(1, pos);
                String right = p.substring(pos + 1, p.length()-1);
                pathParts[i] = "{" + left + "}";
                regexMap.put(left, right);
              }
              else
                pathParts[i] = p;
            }
            else pathParts[i] = p;
          }
          StringBuilder pathBuilder = new StringBuilder();
          for(String p : pathParts) {
            if(!p.isEmpty())
              pathBuilder.append("/").append(p);
          }
          operationPath = pathBuilder.toString();

          String httpMethod = extractOperationMethod(apiOperation, method, SwaggerExtensions.chain());

          Operation operation = parseMethod(method);
          if(parentParameters != null) {
            for(Parameter param : parentParameters) {
              operation.parameter(param);
            }
          }
          for(Parameter param : operation.getParameters()) {
            if(regexMap.get(param.getName()) != null) {
              String pattern = regexMap.get(param.getName());
              param.setPattern(pattern);
            }
          }

          String protocols = apiOperation.protocols();
          if(!"".equals(protocols)) {
            String[] parts = protocols.split(",");
            for(String part : parts) {
              String trimmed = part.trim();
              if(!"".equals(trimmed))
                operation.scheme(Scheme.forValue(trimmed));
            }
          }

          Annotation annotation;
          annotation = cls.getAnnotation(Consumes.class);
          if(annotation != null)
            apiConsumes = ((Consumes)annotation).value();
          if(parentConsumes != null) {
            Set<String> both = new HashSet<String>(Arrays.asList(apiConsumes));
            both.addAll(new HashSet<String>(Arrays.asList(parentConsumes)));
            if(operation.getConsumes() != null)
              both.addAll(new HashSet<String>(operation.getConsumes()));
            apiConsumes = both.toArray(new String[both.size()]);
          }

          annotation = cls.getAnnotation(Produces.class);
          if(annotation != null)
            apiProduces = ((Produces)annotation).value();

          if(parentProduces != null) {
            Set<String> both = new HashSet<String>(Arrays.asList(apiProduces));
            both.addAll(new HashSet<String>(Arrays.asList(parentProduces)));
            if(operation.getProduces() != null)
              both.addAll(new HashSet<String>(operation.getProduces()));
            apiProduces = both.toArray(new String[both.size()]);
          }
          if(isSubResource(method)) {
            Type t = method.getGenericReturnType();
            Class<?> responseClass = method.getReturnType();
            Swagger subSwagger = read(responseClass, operationPath, httpMethod, true, apiConsumes, apiProduces, tags, operation.getParameters());
          }

          // can't continue without a valid http method
          httpMethod = httpMethod == null ? parentMethod : httpMethod;
          if(httpMethod != null) {
            ApiOperation op = (ApiOperation) method.getAnnotation(ApiOperation.class);
            if(op != null) {
              boolean hasExplicitTag = false;
              for(String tag : op.tags()) {
                if(!"".equals(tag)) {
                  operation.tag(tag);
                  swagger.tag(new Tag().name(tag));
                }
              }
            }
            if(operation != null) {
              if(operation.getConsumes() == null)
                for(String mediaType: apiConsumes)
                  operation.consumes(mediaType);
              if(operation.getProduces() == null)
                for(String mediaType: apiProduces)
                  operation.produces(mediaType);

              if(operation.getTags() == null) {
                for(String tagString : tags.keySet())
                  operation.tag(tagString);
              }
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
    }
    return swagger;
  }

  protected boolean isSubResource(Method method) {
    Type t = method.getGenericReturnType();
    Class<?> responseClass = method.getReturnType();
    if(responseClass != null && responseClass.getAnnotation(Api.class) != null) {
      return true;
    }
    return false;
  }

  protected Set<String> extractTags(Api api) {
    Set<String> output = new LinkedHashSet<String>();

    boolean hasExplicitTags = false;
    for(String tag : api.tags()) {
      if(!"".equals(tag)) {
        hasExplicitTags = true;
        output.add(tag);
      }
    }
    if(!hasExplicitTags) {
      // derive tag from api path + description
      String tagString = api.value().replace("/", "");
      if(!"".equals(tagString))
        output.add(tagString);
    }
    return output;
  }

  String getPath(javax.ws.rs.Path classLevelPath, javax.ws.rs.Path methodLevelPath, String parentPath) {
    if(classLevelPath == null && methodLevelPath == null)
      return null;
    StringBuilder b = new StringBuilder();
    if(parentPath != null && !"".equals(parentPath) && !"/".equals(parentPath)) {
      if(!parentPath.startsWith("/"))
        parentPath = "/" + parentPath;
      if(parentPath.endsWith("/"))
        parentPath = parentPath.substring(0, parentPath.length() - 1);

      b.append(parentPath);
    }
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
    if(!output.startsWith("/"))
      output = "/" + output;
    if(output.endsWith("/") && output.length() > 1)
      return output.substring(0, output.length() - 1);
    else
      return output;
  }

  public Map<String, Property> parseResponseHeaders(com.wordnik.swagger.annotations.ResponseHeader[] headers) {
    Map<String,Property> responseHeaders = null;
    if(headers != null && headers.length > 0) {
      for(com.wordnik.swagger.annotations.ResponseHeader header : headers) {
        String name = header.name();
        if(!"".equals(name)) {
          if(responseHeaders == null)
            responseHeaders = new HashMap<String, Property>();
          String description = header.description();
          Class<?> cls = header.response();
          String container = header.responseContainer();

          if(!cls.equals(java.lang.Void.class) && !"void".equals(cls.toString())) {
            Property responseProperty = null;
            Property property = ModelConverters.getInstance().readAsProperty(cls);
            if(property != null) {
              if("list".equalsIgnoreCase(container))
                responseProperty = new ArrayProperty(property);
              else if("map".equalsIgnoreCase(container))
                responseProperty = new MapProperty(property);
              else
                responseProperty = property;
              responseProperty.setDescription(description);
              responseHeaders.put(name, responseProperty);
            }
          }
        }
      }
    }
    return responseHeaders;
  }

  public Operation parseMethod(Method method) {
    Operation operation = new Operation();

    ApiOperation apiOperation = (ApiOperation) method.getAnnotation(ApiOperation.class);
    ApiResponses responseAnnotation = method.getAnnotation(ApiResponses.class);

    String operationId = method.getName();
    String responseContainer = null;

    Class<?> responseClass = null;
    Map<String,Property> defaultResponseHeaders = new HashMap<String, Property>();

    if(apiOperation != null) {
      if(apiOperation.hidden())
        return null;
      if(!"".equals(apiOperation.nickname()))
        operationId = method.getName();

      defaultResponseHeaders = parseResponseHeaders(apiOperation.responseHeaders());

      operation
        .summary(apiOperation.value())
        .description(apiOperation.notes());

      if(apiOperation.response() != null && !Void.class.equals(apiOperation.response()))
        responseClass = apiOperation.response();
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
      LOGGER.debug("picking up response class from method " + method);
      Type t = method.getGenericReturnType();
      responseClass = method.getReturnType();
      if(!responseClass.equals(java.lang.Void.class) && !"void".equals(responseClass.toString()) && responseClass.getAnnotation(Api.class) == null) {
        LOGGER.debug("reading model " + responseClass);
        Map<String, Model> models = ModelConverters.getInstance().readAll(t);
      }
    }
    if(responseClass != null
      && !responseClass.equals(java.lang.Void.class)
      && !responseClass.equals(javax.ws.rs.core.Response.class)
      && responseClass.getAnnotation(Api.class) == null) {
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
            .schema(responseProperty)
            .headers(defaultResponseHeaders));
        }
      }
      else if(!responseClass.equals(java.lang.Void.class) && !"void".equals(responseClass.toString())) {
        Map<String, Model> models = ModelConverters.getInstance().read(responseClass);
        if(models.size() == 0) {
          Property p = ModelConverters.getInstance().readAsProperty(responseClass);
          operation.response(200, new Response()
            .description("successful operation")
            .schema(p)
            .headers(defaultResponseHeaders));
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
            .schema(responseProperty)
            .headers(defaultResponseHeaders));
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
        Map<String,Property> responseHeaders = parseResponseHeaders(apiResponse.responseHeaders());

        Response response = new Response()
          .description(apiResponse.message())
          .headers(responseHeaders);

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

    LOGGER.debug("getParameters for " + cls);
    Set<Class<?>> classesToSkip = new HashSet<Class<?>>();
    if(chain.hasNext()) {
      SwaggerExtension extension = chain.next();
      LOGGER.debug("trying extension " + extension);
      parameters = extension.extractParameters(annotations, cls, isArray, classesToSkip, chain);
    }

    if(parameters.size() > 0) {
      for(Parameter parameter : parameters) {
        ParameterProcessor.applyAnnotations(swagger, parameter, cls, annotations, isArray);
      }
    }
    else {
      LOGGER.debug("no parameter found, looking at body params");
      if(classesToSkip.contains(cls) == false) {
        if(type instanceof ParameterizedType) {
          ParameterizedType ti = (ParameterizedType) type;
          Type innerType = ti.getActualTypeArguments()[0];
          if(innerType instanceof Class) {
            Parameter param = ParameterProcessor.applyAnnotations(swagger, null, (Class)innerType, annotations, isArray);
            if(param != null) {
              parameters.add(param);
            }            
          }
        }
        else {
          Parameter param = ParameterProcessor.applyAnnotations(swagger, null, cls, annotations, isArray);
          if(param != null) {
            parameters.add(param);
          }
        }
      }
    }
    return parameters;
  }

  public String extractOperationMethod(ApiOperation apiOperation, Method method, Iterator<SwaggerExtension> chain) {
    if(apiOperation.httpMethod() != null && !"".equals(apiOperation.httpMethod()))
      return apiOperation.httpMethod().toLowerCase();
    else if(method.getAnnotation(javax.ws.rs.GET.class) != null)
      return "get";
    else if(method.getAnnotation(javax.ws.rs.PUT.class) != null)
      return "put";
    else if(method.getAnnotation(javax.ws.rs.POST.class) != null)
      return "post";
    else if(method.getAnnotation(javax.ws.rs.DELETE.class) != null)
      return "delete";
    else if(method.getAnnotation(javax.ws.rs.OPTIONS.class) != null)
      return "options";
    else if(method.getAnnotation(javax.ws.rs.HEAD.class) != null)
      return "head";
    else if(method.getAnnotation(PATCH.class) != null)
      return "patch";
    else if(method.getAnnotation(HttpMethod.class) != null) {
      HttpMethod httpMethod = (HttpMethod) method.getAnnotation(HttpMethod.class);
      return httpMethod.value().toLowerCase();
    }
    else if(chain.hasNext())
      return chain.next().extractOperationMethod(apiOperation, method, chain);
    else
      return null;
  }

  boolean isPrimitive(Class<?> cls) {
    boolean out = false;

    Property property = ModelConverters.getInstance().readAsProperty(cls);
    if(property == null)
      out = false;
    else if("integer".equals(property.getType()))
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
}
