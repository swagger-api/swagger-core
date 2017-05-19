package io.swagger.jaxrs2;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.jaxrs2.util.ReflectionUtils;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.models.ExternalDocumentation;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.oas.models.responses.ApiResponses;
import io.swagger.oas.models.servers.Server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Reader {
    private OpenAPI openAPI;

    public Reader(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    public Operation parseMethod(Method method) {
        JavaType classType = TypeFactory.defaultInstance().constructType(method.getDeclaringClass());
        BeanDescription bd = new ObjectMapper().getSerializationConfig().introspect(classType);
        return parseMethod(classType.getClass(), method, bd.findMethod(method.getName(), method.getParameterTypes()),
                Collections.<Parameter>emptyList(), Collections.<ApiResponse>emptyList());
    }

    private Operation parseMethod(Class<?> cls, Method method, AnnotatedMethod annotatedMethod,
                                  List<Parameter> globalParameters, List<ApiResponse> classApiResponses) {
        Operation operation = new Operation();
        io.swagger.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.oas.annotations.Operation.class);

        if (apiOperation != null) {
            //Add a Summary and the Description
            operation.summary(apiOperation.summary()).description(apiOperation.description());

            // Set Operation Id
            operation.setOperationId(getOperationId(cls, method));

            //Set Deprecated
            operation.setDeprecated(apiOperation.deprecated());

            operation.setResponses(getApiResponses(apiOperation.responses()));

            operation.requestBody(getRequestBody(apiOperation));

            operation.setExternalDocs(getExternalDocumentation(apiOperation));

            operation.setServers(getServers(apiOperation));

        }

        return operation;
    }

    private List<Server> getServers(io.swagger.oas.annotations.Operation apiOperation){
        io.swagger.oas.annotations.servers.Server[] servers = apiOperation.servers();
        List<Server> serverObjects = new ArrayList<>();
        for(io.swagger.oas.annotations.servers.Server server : servers){
            Server serverObject = new Server();
            serverObject.setUrl(server.url());
            serverObject.setDescription(server.description());
            serverObjects.add(serverObject);
        }
        return serverObjects;
    }

    private ExternalDocumentation getExternalDocumentation(io.swagger.oas.annotations.Operation apiOperation) {
        ExternalDocumentation external = new ExternalDocumentation();
        io.swagger.oas.annotations.ExternalDocumentation externalDocumentation = apiOperation.externalDocs();
        external.setDescription(externalDocumentation.description());
        external.setUrl(externalDocumentation.url());
        return external;
    }

    private RequestBody getRequestBody(io.swagger.oas.annotations.Operation apiOperation) {
        io.swagger.oas.annotations.parameters.RequestBody requestBody = apiOperation.requestBody();
        RequestBody requestBodyObject = new RequestBody();
        requestBodyObject.setDescription(requestBody.description());
        requestBodyObject.setRequired(requestBody.required());
        return requestBodyObject;
    }


    public ApiResponses getApiResponses(final io.swagger.oas.annotations.responses.ApiResponse[] responses) {
        ApiResponses apiResponses = new ApiResponses();
        for (io.swagger.oas.annotations.responses.ApiResponse response : responses) {
            ApiResponse apiResponse = new ApiResponse();

            Content content = new Content();
            io.swagger.oas.annotations.media.Content annotationContent = response.content();
            if (annotationContent != null) {
                content.addMediaType(annotationContent.mediaType(), null);

                ExampleObject examples[] = annotationContent.examples();
                for (ExampleObject example : examples) {
                    // What happens with the ExampleObject?
                }
                apiResponse.content(content);
            }
            apiResponse.setDescription(response.description());
            apiResponses.addApiResponse(response.responseCode(), apiResponse);
        }
        return apiResponses;
    }

    public String getOperationId(final Class<?> cls, final Method method) {
        String operationId = null;
        // check if it's an inherited or implemented method.
        boolean methodInSuperType = false;
        if (!cls.isInterface()) {
            methodInSuperType = ReflectionUtils.findMethod(method, cls.getSuperclass()) != null;
        }
        if (!methodInSuperType) {
            for (Class<?> implementedInterface : cls.getInterfaces()) {
                methodInSuperType = ReflectionUtils.findMethod(method, implementedInterface) != null;
                if (methodInSuperType) {
                    break;
                }
            }
        }
        if (!methodInSuperType) {
            operationId = method.getName();
        } else {
            // operationId = this.getOperationId(method.getName());
        }

        return operationId;
    }
}
