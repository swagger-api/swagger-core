package io.swagger.jaxrs2;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.jaxrs2.util.ReaderUtils;
import io.swagger.oas.models.*;
import io.swagger.oas.models.callbacks.Callback;
import io.swagger.oas.models.callbacks.Callbacks;
import io.swagger.oas.models.info.Contact;
import io.swagger.oas.models.info.Info;
import io.swagger.oas.models.info.License;
import io.swagger.oas.models.security.SecurityScheme;
import io.swagger.oas.models.servers.Server;
import io.swagger.oas.models.tags.Tag;
import io.swagger.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class Reader {
    private OpenAPI openAPI;
    private Paths paths;
    private Set<Tag> openApiTags;

    private static final String GET_METHOD = "get";
    private static final String POST_METHOD = "post";
    private static final String PUT_METHOD = "put";
    private static final String DELETE_METHOD = "delete";
    private static final String PATCH_METHOD = "patch";
    private static final String TRACE_METHOD = "trace";
    private static final String HEAD_METHOD = "head";
    private static final String OPTIONS_METHOD = "options";

    public Reader(OpenAPI openAPI) {
        this.openAPI = openAPI;
        paths = new Paths();
        openApiTags = new LinkedHashSet<>();
    }

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    private OpenAPI read(Class cls) {
        //TODO Class level Annotations
        io.swagger.oas.annotations.security.SecurityScheme apiSecurityScheme = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.security.SecurityScheme.class);
        io.swagger.oas.annotations.servers.Server server = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.servers.Server.class);
        io.swagger.oas.annotations.ExternalDocumentation apiExternalDocs = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.ExternalDocumentation.class);
        io.swagger.oas.annotations.info.Info apiInfo = ReflectionUtils.getAnnotation(cls, io.swagger.oas.annotations.info.Info.class);


        boolean hasPathAnnotation = (ReflectionUtils.getAnnotation(cls, javax.ws.rs.Path.class) != null);

        Optional<SecurityScheme> securityScheme = SecurityParser.getSecurityScheme(apiSecurityScheme);
        Components components = new Components();
        if (securityScheme.isPresent()) {
            Map<String, SecurityScheme> securitySchemeMap = new HashMap();
            securitySchemeMap.put(securityScheme.get().getName(), securityScheme.get());
            components.setSecuritySchemes(securitySchemeMap);
        }
        openAPI.setComponents(components);

        Optional<Server> serverOptional = OperationParser.getServer(server);

        Method methods[] = cls.getMethods();
        for (Method method : methods) {
            Operation operation = parseMethod(cls, method);
        }

        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.addAll(openApiTags);
        openAPI.setTags(tagList);

        openAPI.setExternalDocs(OperationParser.getExternalDocumentation(apiExternalDocs).get());
        openAPI.setInfo(getInfo(apiInfo).get());

        openAPI.setPaths(paths);

        return openAPI;
    }

    public Optional<Info> getInfo(io.swagger.oas.annotations.info.Info info){
        if (info == null){
            return Optional.empty();
        }
        Info infoObject = new Info();
        infoObject.setDescription(info.description());
        infoObject.setTermsOfService(info.termsOfService());
        infoObject.setTitle(info.title());
        infoObject.setVersion(info.version());
        infoObject.setContact(getContact(info.contact()).get());
        infoObject.setLicense(getLicense(info.license()).get());

        return Optional.of(infoObject);
    }

    public Optional<Contact> getContact(io.swagger.oas.annotations.info.Contact contact){
        if (contact == null){
            return Optional.empty();
        }
        Contact contactObject = new Contact();
        contactObject.setEmail(contact.email());
        contactObject.setName(contact.name());
        contactObject.setUrl(contact.url());
        return Optional.of(contactObject);
    }

    public Optional<License> getLicense(io.swagger.oas.annotations.info.License license){
        if (license == null){
            return Optional.empty();
        }
        License licenseObject = new License();
        licenseObject.setName(license.name());
        licenseObject.setUrl(license.url());

        return Optional.of(licenseObject);
    }

    public Operation parseMethod(Method method) {
        JavaType classType = TypeFactory.defaultInstance().constructType(method.getDeclaringClass());
        return parseMethod(classType.getClass(), method);
    }

    private Operation parseMethod(Class<?> cls, Method method) {
        Operation operation = new Operation();
        io.swagger.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.Operation.class);
        io.swagger.oas.annotations.callbacks.Callback apiCallback = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.callbacks.Callback.class);
        io.swagger.oas.annotations.security.SecurityRequirement apiSecurity = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.security.SecurityRequirement.class);
        io.swagger.oas.annotations.links.Link apiLinks = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.links.Link.class);

        if (apiOperation != null) {
            getCallbacks(apiCallback).ifPresent(x -> operation.setCallbacks(x));
            SecurityParser.getSecurityRequirement(apiSecurity).ifPresent(x -> operation.setSecurity(x));
            operation.setResponses(OperationParser.getApiResponses(apiOperation.responses(), apiLinks).get());

            setOperationObjectFromApiOperationAnnotation(operation, apiOperation);
        }
        return operation;
    }

    private Optional<Callbacks> getCallbacks(io.swagger.oas.annotations.callbacks.Callback apiCallback) {
        if (apiCallback == null) {
            return Optional.empty();
        }
        Callbacks callbacksObject = new Callbacks();
        Callback callbackObject = new Callback();
        PathItem pathItemObject = new PathItem();

        for (io.swagger.oas.annotations.Operation callbackOperation : apiCallback.operation()) {
            Operation callbackNewOperation = new Operation();

            setOperationObjectFromApiOperationAnnotation(callbackNewOperation, callbackOperation);

            switch (callbackOperation.method()) {
                case POST_METHOD:
                    pathItemObject.post(callbackNewOperation);
                    break;
                case GET_METHOD:
                    pathItemObject.get(callbackNewOperation);
                    break;
                case DELETE_METHOD:
                    pathItemObject.delete(callbackNewOperation);
                    break;
                case PUT_METHOD:
                    pathItemObject.put(callbackNewOperation);
                    break;
                case PATCH_METHOD:
                    pathItemObject.patch(callbackNewOperation);
                    break;
                case TRACE_METHOD:
                    pathItemObject.trace(callbackNewOperation);
                    break;
                case HEAD_METHOD:
                    pathItemObject.head(callbackNewOperation);
                    break;
                case OPTIONS_METHOD:
                    pathItemObject.options(callbackNewOperation);
                    break;
                default:
                    // Do nothing here
                    break;

            }
        }
        pathItemObject.setDescription(apiCallback.name());
        pathItemObject.setSummary(apiCallback.name());

        callbackObject.addPathItem(apiCallback.name(), pathItemObject);
        callbacksObject.addCallback(apiCallback.name(), callbackObject);

        return Optional.of(callbacksObject);
    }

    private void setOperationObjectFromApiOperationAnnotation(Operation operation, io.swagger.oas.annotations.Operation apiOperation) {
        operation.setTags(ReaderUtils.getStringListFromStringArray(apiOperation.tags()).get());
        openApiTags.addAll(OperationParser.getTags(apiOperation.tags()).get());
        operation.setSummary(apiOperation.summary());
        operation.setDescription(apiOperation.description());
        operation.setExternalDocs(OperationParser.getExternalDocumentation(apiOperation.externalDocs()).get());
        operation.setOperationId(apiOperation.operationId());
        operation.setParameters(OperationParser.getParametersList(apiOperation.parameters()).get());
        operation.setRequestBody(OperationParser.getRequestBody(apiOperation.requestBody()).get());
        operation.setDeprecated(apiOperation.deprecated());
        operation.setServers(OperationParser.getServers(apiOperation.servers()).get());
    }
}