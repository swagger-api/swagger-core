package io.swagger.jaxrs2;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.oas.models.responses.ApiResponses;
import io.swagger.util.ReflectionUtils;

import java.lang.reflect.Method;
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
        }

        return operation;
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
