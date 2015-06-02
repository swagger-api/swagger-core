/**
 *  Copyright 2015 SmartBear Software
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

package com.wordnik.swagger.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Produces;

import com.wordnik.swagger.annotations.ExtensionProperty;
import com.wordnik.swagger.annotations.Extension;
import com.wordnik.swagger.annotations.SwaggerDefinition;
import com.wordnik.swagger.jaxrs.config.DefaultReaderConfig;
import com.wordnik.swagger.jaxrs.config.ReaderConfig;
import com.wordnik.swagger.jaxrs.config.ReaderListener;
import com.wordnik.swagger.models.Contact;
import com.wordnik.swagger.models.ExternalDocs;
import com.wordnik.swagger.models.Info;
import com.wordnik.swagger.models.License;
import com.wordnik.swagger.jaxrs.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.Authorization;
import com.wordnik.swagger.annotations.AuthorizationScope;
import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.jaxrs.ext.SwaggerExtension;
import com.wordnik.swagger.jaxrs.ext.SwaggerExtensions;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.Operation;
import com.wordnik.swagger.models.Path;
import com.wordnik.swagger.models.Response;
import com.wordnik.swagger.models.Scheme;
import com.wordnik.swagger.models.SecurityRequirement;
import com.wordnik.swagger.models.SecurityScope;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.models.Tag;
import com.wordnik.swagger.models.parameters.Parameter;
import com.wordnik.swagger.models.properties.ArrayProperty;
import com.wordnik.swagger.models.properties.MapProperty;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.models.properties.RefProperty;
import com.wordnik.swagger.util.Json;

public class Reader {
  private static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);
  private static final String SUCCESSFUL_OPERATION = "successful operation";
  private static final String PATH_DELIMITER = "/";

  private final ReaderConfig config;
  private Swagger swagger;

  public Reader(Swagger swagger) {
    this(swagger, null);
  }

  public Reader(Swagger swagger, ReaderConfig config) {
    this.swagger = swagger == null ? new Swagger() : swagger;
    this.config = new DefaultReaderConfig(config);
  }

  /**
   * Scans a set of classes for both ReaderListeners and Swagger annotations. All found listeners will
   * be instantiated before any of the classes are scanned for Swagger annotations - so they can be invoked
   * accordingly.
   *
   * @param classes a set of classes to scan
   * @return the generated Swagger definition
   */

  public Swagger read(Set<Class<?>> classes) {

    Map<Class<?>,ReaderListener> listeners = new HashMap<Class<?>, ReaderListener>();

    for(Class<?> cls: classes) {
      if( ReaderListener.class.isAssignableFrom( cls ) && !listeners.containsKey( cls )){
        try {
          listeners.put( cls, (ReaderListener) cls.newInstance());
        } catch (Exception e) {
          LOGGER.error("Failed to create ReaderListener", e);
        }
      }
    }

    for( ReaderListener listener : listeners.values()){
      try {
        listener.beforeScan(this, swagger);
      }
      catch( Exception e ){
        LOGGER.error( "Unexpected error invoking beforeScan listener [" + listener.getClass().getName() + "]", e );
      }
    }

    for(Class<?> cls: classes)
      read(cls);

    for( ReaderListener listener : listeners.values()){
      try {
        listener.afterScan( this, swagger);
      }
      catch( Exception e ){
        LOGGER.error( "Unexpected error invoking afterScan listener [" + listener.getClass().getName() + "]", e );
      }
    }

    return swagger;
  }

  public Swagger getSwagger() {
    return this.swagger;
  }

  /**
   * Scans a single class for Swagger annotations - does not invoke ReaderListeners
   */

  public Swagger read(Class<?> cls) {
     return read(cls, "", null, false, new String[0], new String[0], new HashMap<String, Tag>(), new ArrayList<Parameter>());
  }

  protected Swagger read(Class<?> cls, String parentPath, String parentMethod, boolean readHidden, String[] parentConsumes, String[] parentProduces, Map<String, Tag> parentTags, List<Parameter> parentParameters) {
    SwaggerDefinition swaggerDefinition = cls.getAnnotation(SwaggerDefinition.class);
    if( swaggerDefinition != null ){
      readSwaggerConfig( cls, swaggerDefinition);
    }

    Api api = (Api) cls.getAnnotation(Api.class);
    Map<String, SecurityScope> globalScopes = new HashMap<String, SecurityScope>();

    Map<String, Tag> tags = new HashMap<String, Tag>();
    List<SecurityRequirement> securities = new ArrayList<SecurityRequirement>();
    
    String[] consumes = new String[0];
    String[] produces = new String[0];
    final Set<Scheme> globalSchemes = EnumSet.noneOf(Scheme.class);

    // only read if allowing hidden apis OR api is not marked as hidden
    final boolean readable = (api != null && readHidden) || (api != null && !api.hidden());
    if(readable) {
      // the value will be used as a tag for 2.0 UNLESS a Tags annotation is present
      Set<String> tagStrings = extractTags(api);      
      for(String tagString : tagStrings) {
        Tag tag = new Tag().name(tagString);
        tags.put(tagString, tag);
      }
      if(parentTags != null)
        tags.putAll(parentTags);
      for(String tagName: tags.keySet()) {
        swagger.tag(tags.get(tagName));
      }

      if (!api.produces().isEmpty()) {
        produces = new String[]{api.produces()};
      } else if (cls.getAnnotation(Produces.class) != null) {
        produces = ((Produces) cls.getAnnotation(Produces.class)).value();
      }
      if (!api.consumes().isEmpty()){
        consumes = new String[]{api.consumes()};
      } else if (cls.getAnnotation(Consumes.class) != null){
        consumes = ((Consumes)cls.getAnnotation(Consumes.class)).value();
      }
      globalSchemes.addAll(parseSchemes(api.protocols()));
      Authorization[] authorizations = api.authorizations();
      
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
    }
    
    // allow reading the JAX-RS APIs without @Api annotation
    if (readable || (api == null && config.isScanAllResources())) {
      // merge consumes, produces

      // look for method-level annotated properties

      // handle sub-resources by looking at return type

      // parse the method
      final javax.ws.rs.Path apiPath = cls.getAnnotation(javax.ws.rs.Path.class);
      Method methods[] = cls.getMethods();
      for(Method method : methods) {
        if (ReflectionUtils.isOverriddenMethod(method, cls)) {
          continue;
        }
        javax.ws.rs.Path methodPath = getAnnotation(method, javax.ws.rs.Path.class);

        String operationPath = getPath(apiPath, methodPath, parentPath);
        if(operationPath != null) {
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

          if (isIgnored(operationPath)) {
            continue;
          }

          final ApiOperation apiOperation = getAnnotation(method, ApiOperation.class);
          String httpMethod = extractOperationMethod(apiOperation, method, SwaggerExtensions.chain());

          Operation operation = parseMethod(method);
          if(operation == null)
            continue;
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

          if (apiOperation != null) {
            for (Scheme scheme: parseSchemes(apiOperation.protocols())) {
              operation.scheme(scheme);
            }
          }

          if (operation.getSchemes() == null || operation.getSchemes().isEmpty()) {
            for (Scheme scheme: globalSchemes) {
              operation.scheme(scheme);
            }
          }

          String[] apiConsumes = consumes;
          if(parentConsumes != null) {
            Set<String> both = new HashSet<String>(Arrays.asList(apiConsumes));
            both.addAll(new HashSet<String>(Arrays.asList(parentConsumes)));
            if(operation.getConsumes() != null)
              both.addAll(new HashSet<String>(operation.getConsumes()));
            apiConsumes = both.toArray(new String[both.size()]);
          }

          String[] apiProduces = produces;
          if(parentProduces != null) {
            Set<String> both = new HashSet<String>(Arrays.asList(apiProduces));
            both.addAll(new HashSet<String>(Arrays.asList(parentProduces)));
            if(operation.getProduces() != null)
              both.addAll(new HashSet<String>(operation.getProduces()));
            apiProduces = both.toArray(new String[both.size()]);
          }
          final Class<?> subResource = getSubResource(method);
          if (subResource != null) {
            read(subResource, operationPath, httpMethod, true, apiConsumes, apiProduces, tags, operation.getParameters());
          }

          // can't continue without a valid http method
          httpMethod = httpMethod == null ? parentMethod : httpMethod;
          if(httpMethod != null) {
            if(apiOperation != null) {
              boolean hasExplicitTag = false;
              for(String tag : apiOperation.tags()) {
                if(!"".equals(tag)) {
                  operation.tag(tag);
                  swagger.tag(new Tag().name(tag));
                }
              }

              if( operation != null ){
                addExtensionProperties( apiOperation.extensions(), operation.getVendorExtensions());
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
              // Only add global @Api securities if operation doesn't already have more specific securities
              if (operation.getSecurity() == null) {
                for(SecurityRequirement security : securities) {
                  operation.security(security);
                }
              }

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

  protected void readSwaggerConfig(Class<?> cls, SwaggerDefinition config) {

    if( !config.basePath().isEmpty()){
      swagger.setBasePath( config.basePath());
    }

    if( !config.host().isEmpty()){
      swagger.setHost( config.host());
    }

    readInfoConfig(config);

    for( String consume: config.consumes()){
      swagger.addConsumes(consume);
    }

    for( String produce: config.produces()){
      swagger.addProduces( produce );
    }

    if( !config.externalDocs().value().isEmpty() ){
      ExternalDocs externalDocs = swagger.getExternalDocs();
      if( externalDocs == null ){
        externalDocs = new ExternalDocs();
        swagger.setExternalDocs(externalDocs);
      }

      externalDocs.setDescription(config.externalDocs().value());

      if( !config.externalDocs().url().isEmpty()){
        externalDocs.setUrl( config.externalDocs().url() );
      }
    }

    for( com.wordnik.swagger.annotations.Tag tagConfig : config.tags()){
      if( !tagConfig.name().isEmpty()){
        Tag tag = new Tag();
        tag.setName( tagConfig.name() );
        tag.setDescription( tagConfig.description());

        if( !tagConfig.externalDocs().value().isEmpty() ){
           tag.setExternalDocs( new ExternalDocs( tagConfig.externalDocs().value(),
                   tagConfig.externalDocs().url()));
        }

        addExtensionProperties( tagConfig.extensions(), tag.getVendorExtensions());

        swagger.addTag( tag );
      }
    }

    for( SwaggerDefinition.Scheme scheme : config.schemes()){
      if( scheme != SwaggerDefinition.Scheme.DEFAULT ){
        swagger.addScheme( Scheme.forValue( scheme.name()));
      }
    }
  }

  protected void readInfoConfig(SwaggerDefinition config) {
    com.wordnik.swagger.annotations.Info infoConfig = config.info();
    Info info = swagger.getInfo();
    if( info == null ){
      info = new Info();
      swagger.setInfo(info);
    }

    if( !infoConfig.description().isEmpty() ){
      info.setDescription( infoConfig.description());
    }

    if( !infoConfig.termsOfService().isEmpty() ){
      info.setTermsOfService( infoConfig.termsOfService());
    }

    if( !infoConfig.title().isEmpty() ){
      info.setTitle(infoConfig.title());
    }

    if( !infoConfig.version().isEmpty() ){
      info.setVersion(infoConfig.version());
    }

    if( !infoConfig.contact().name().isEmpty() ){
      Contact contact = info.getContact();
      if( contact == null ){
        contact = new Contact();
        info.setContact( contact );
      }

      contact.setName( infoConfig.contact().name() );
      if( !infoConfig.contact().email().isEmpty() ){
        contact.setEmail( infoConfig.contact().email());
      }

      if( !infoConfig.contact().url().isEmpty() ){
        contact.setUrl(infoConfig.contact().url());
      }
    }

    if( !infoConfig.license().name().isEmpty() ){
      License license = info.getLicense();
      if( license == null ){
        license = new License();
        info.setLicense( license );
      }

      license.setName( infoConfig.license().name());
      if( !infoConfig.license().url().isEmpty() ){
        license.setUrl( infoConfig.license().url());
      }
    }

    addExtensionProperties(infoConfig.extensions(), info.getVendorExtensions());
  }

  private void addExtensionProperties(Extension [] extensions, Map<String, Object> map) {
    for( Extension extension : extensions ) {
      String name = extension.name();
      if (name.length() > 0) {

        if( !name.startsWith("x-")){
          name = "x-" + name;
        }

        if( !map.containsKey( name )) {
          map.put(name, new HashMap<String, Object>());
        }

        map = (Map<String, Object>) map.get(name);
      }

      for (ExtensionProperty property : extension.properties()) {
        if (!property.name().isEmpty() && !property.value().isEmpty()) {

          String propertyName = property.name();
          if( name.isEmpty() && !propertyName.startsWith( "x-")){
            propertyName = "x-" + propertyName;
          }

          map.put(propertyName, property.value());
        }
      }
    }
  }

  protected Class<?> getSubResource(Method method) {
    final Class<?> rawType = method.getReturnType();
    final Class<?> type;
    if (Class.class.equals(rawType)) {
      type = getClassArgument(method.getGenericReturnType());
      if (type == null) {
        return null;
      }
    } else {
      type = rawType;
    }
    return type.getAnnotation(Api.class) != null ? type : null;
  }

  private static Class<?> getClassArgument(Type cls) {
    if (cls instanceof ParameterizedType) {
      final ParameterizedType parameterized = (ParameterizedType) cls;
      final Type[] args = parameterized.getActualTypeArguments();
      if (args.length != 1) {
        LOGGER.error(String.format("Unexpected class definition: %s", cls));
        return null;
      }
      final Type first = args[0];
      if (first instanceof Class) {
        return (Class<?>) first;
      } else {
        return null;
      }
    } else {
      LOGGER.error(String.format("Unknown class definition: %s", cls));
      return null;
    }
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
    if (classLevelPath == null && methodLevelPath == null && StringUtils.isEmpty(parentPath)) {
      return null;
    }
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

          if(!cls.equals(java.lang.Void.class) && !"void".equals(cls.toString())) {
            Property property = ModelConverters.getInstance().readAsProperty(cls);
            if(property != null) {
              Property responseProperty = ContainerWrapper.wrapContainer(header.responseContainer(), property,
                      ContainerWrapper.ARRAY, ContainerWrapper.LIST, ContainerWrapper.SET);
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

    ApiOperation apiOperation = getAnnotation(method, ApiOperation.class);
    ApiResponses responseAnnotation = getAnnotation(method, ApiResponses.class);

    String operationId = method.getName();
    String responseContainer = null;

    Type responseType = null;
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

      if(apiOperation.response() != null && !isVoid(apiOperation.response()))
        responseType = apiOperation.response();
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
              if(scope.scope() != null && !"".equals(scope.scope())) {
                security.addScope(scope.scope());
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
      if (apiOperation.consumes() != null && !apiOperation.consumes().isEmpty()) {
        operation.consumes(apiOperation.consumes());
      }
      if (apiOperation.produces() != null && !apiOperation.produces().isEmpty()) {
        operation.produces(apiOperation.produces());
      }
    }

    if(responseType == null) {
      // pick out response from method declaration
      LOGGER.debug("picking up response class from method " + method);
      responseType = method.getGenericReturnType();
    }
    if(isValidResponse(responseType)) {
      int responseCode = 200;
      if (apiOperation != null) {
        responseCode = apiOperation.code();
      }
      if(isPrimitive(responseType)) {
        Property property = ModelConverters.getInstance().readAsProperty(responseType);
        if(property != null) {
          Property responseProperty = ContainerWrapper.wrapContainer(responseContainer, property);
          operation.response(responseCode, new Response()
            .description(SUCCESSFUL_OPERATION)
            .schema(responseProperty)
            .headers(defaultResponseHeaders));
        }
      } else {
        Map<String, Model> models = ModelConverters.getInstance().read(responseType);
        if(models.size() == 0) {
          Property p = ModelConverters.getInstance().readAsProperty(responseType);
          operation.response(responseCode, new Response()
            .description(SUCCESSFUL_OPERATION)
            .schema(p)
            .headers(defaultResponseHeaders));
        }
        for(String key: models.keySet()) {
          Property property = new RefProperty().asDefault(key);
          Property responseProperty = ContainerWrapper.wrapContainer(responseContainer, property);
          operation.response(responseCode, new Response()
            .description(SUCCESSFUL_OPERATION)
            .schema(responseProperty)
            .headers(defaultResponseHeaders));
          swagger.model(key, models.get(key));
        }
        models = ModelConverters.getInstance().readAll(responseType);
        for(String key: models.keySet()) {
          swagger.model(key, models.get(key));
        }
      }
    }

    operation.operationId(operationId);

    Annotation annotation;
    if (apiOperation != null && apiOperation.consumes() != null && apiOperation.consumes().isEmpty()) {
      annotation = getAnnotation(method, Consumes.class);
      if(annotation != null) {
        String[] apiConsumes = ((Consumes)annotation).value();
        for(String mediaType: apiConsumes)
          operation.consumes(mediaType);
      }
    }

    if (apiOperation != null && apiOperation.produces() != null && apiOperation.produces().isEmpty()) {
      annotation = getAnnotation(method, Produces.class);
      if(annotation != null) {
        String[] apiProduces = ((Produces)annotation).value();
        for(String mediaType: apiProduces)
          operation.produces(mediaType);
      }
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

        responseType = apiResponse.response();
        if(responseType != null && !isVoid(responseType)) {
          Map<String, Model> models = ModelConverters.getInstance().read(responseType);
          for(String key: models.keySet()) {
            Property property =  new RefProperty().asDefault(key);
            Property responseProperty = ContainerWrapper.wrapContainer(apiResponse.responseContainer(), property);
            response.schema(responseProperty);
            swagger.model(key, models.get(key));
          }
          models = ModelConverters.getInstance().readAll(responseType);
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
    Type[] genericParameterTypes = method.getGenericParameterTypes();
    Annotation[][] paramAnnotations = method.getParameterAnnotations();
    for(int i = 0; i < genericParameterTypes.length; i++) {
      Type type = genericParameterTypes[i];
      List<Parameter> parameters = getParameters(type,Arrays.asList(paramAnnotations[i]));

      for(Parameter parameter : parameters) {
        operation.parameter(parameter);
      }
    }
    if(operation.getResponses() == null) {
      operation.defaultResponse(new Response().description(SUCCESSFUL_OPERATION));
    }
    return operation;
  }

  private static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationClass) {
    A annotation = method.getAnnotation(annotationClass);
    if (annotation == null) {
      Method superclassMethod = ReflectionUtils.getOverriddenMethod(method);
      if (superclassMethod != null) {
        annotation = getAnnotation(superclassMethod, annotationClass);
      }
    }
    return annotation;
  }

  private List<Parameter> getParameters(Type type, List <Annotation> annotations) {
    final Iterator<SwaggerExtension> chain = SwaggerExtensions.chain();
    if (!chain.hasNext()) {
      return Collections.emptyList();
    }
    LOGGER.debug("getParameters for " + type);
    Set<Type> typesToSkip = new HashSet<Type>();
    final SwaggerExtension extension = chain.next();
    LOGGER.debug("trying extension " + extension);

    final List<Parameter> parameters = extension.extractParameters(annotations, type, typesToSkip, chain);

    if(parameters.size() > 0) {
      final List<Parameter> processed = new ArrayList<Parameter>(parameters.size());
      for(Parameter parameter : parameters) {
        if (ParameterProcessor.applyAnnotations(swagger, parameter, type, annotations) != null) {
          processed.add(parameter);
        }
      }
      return processed;
    }
    else {
      LOGGER.debug("no parameter found, looking at body params");
      final List<Parameter> body = new ArrayList<Parameter>();
      if (!typesToSkip.contains(type)) {
        Parameter param = ParameterProcessor.applyAnnotations(swagger, null, type, annotations);
        if (param != null) {
          body.add(param);
        }
      }
      return body;
    }
  }

  public String extractOperationMethod(ApiOperation apiOperation, Method method, Iterator<SwaggerExtension> chain) {
    if(apiOperation != null && apiOperation.httpMethod() != null && !"".equals(apiOperation.httpMethod()))
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
    } else if ((ReflectionUtils.getOverriddenMethod(method)) != null){
      return extractOperationMethod(apiOperation, ReflectionUtils.getOverriddenMethod(method), chain);
    } else if(chain.hasNext())
      return chain.next().extractOperationMethod(apiOperation, method, chain);
    else
      return null;
  }

  boolean isPrimitive(Type type) {
    boolean out = false;

    Property property = ModelConverters.getInstance().readAsProperty(type);
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

  private static Set<Scheme> parseSchemes(String schemes) {
    final Set<Scheme> result = EnumSet.noneOf(Scheme.class);
    for (String item : StringUtils.trimToEmpty(schemes).split(",")) {
      final Scheme scheme = Scheme.forValue(StringUtils.trimToNull(item));
      if (scheme != null) {
        result.add(scheme);
      }
    }
    return result;
  }

  private boolean isIgnored(String path) {
    for (String item : config.getIgnoredRoutes()) {
      final int length = item.length();
      if (path.startsWith(item) && (path.length() == length || path.startsWith(PATH_DELIMITER, length))) {
        return true;
      }
    }
    return false;
  }

  private static boolean isVoid(Type type) {
    final Class<?> cls = TypeFactory.defaultInstance().constructType(type).getRawClass();
    return Void.class.isAssignableFrom(cls) || Void.TYPE.isAssignableFrom(cls);
  }

  private static boolean isValidResponse(Type type) {
    final JavaType javaType = TypeFactory.defaultInstance().constructType(type);
    if (isVoid(javaType)) {
      return false;
    }
    final Class<?> cls = javaType.getRawClass();
    return !javax.ws.rs.core.Response.class.isAssignableFrom(cls) && !isResourceClass(cls);
  }

  private static boolean isResourceClass(Class<?> cls) {
    return cls.getAnnotation(Api.class) != null;
  }

  enum ContainerWrapper {
    LIST("list") {
      @Override
      protected Property doWrap(Property property) {
        return new ArrayProperty(property);
      }
    },
    ARRAY("array") {
      @Override
      protected Property doWrap(Property property) {
        return new ArrayProperty(property);
      }
    },
    MAP("map") {
      @Override
      protected Property doWrap(Property property) {
        return new MapProperty(property);
      }
    },
    SET("set") {
      @Override
      protected Property doWrap(Property property) {
        ArrayProperty arrayProperty = new ArrayProperty(property);
        arrayProperty.setUniqueItems(true);
        return arrayProperty;
      }
    };

    private final String container;

    ContainerWrapper(String container) {
      this.container = container;
    }

    public Property wrap(String container, Property property) {
      if (this.container.equalsIgnoreCase(container)) {
        return doWrap(property);
      }
      return null;
    }

    public static Property wrapContainer(String container, Property property, ContainerWrapper... allowed) {
      final Set<ContainerWrapper> tmp = allowed.length > 0 ? EnumSet.copyOf(Arrays.asList(allowed)) : EnumSet.allOf(ContainerWrapper.class);
      for (ContainerWrapper wrapper : tmp) {
        final Property prop = wrapper.wrap(container, property);
        if (prop != null) {
          return prop;
        }
      }
      return property;
    }

    protected abstract Property doWrap(Property property);
  }

  public ReaderConfig getConfig() {
    return config;
  }
}
