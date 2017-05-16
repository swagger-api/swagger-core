package io.swagger.jaxrs2;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.oas.annotations.responses.ApiResponse;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.parameters.Parameter;

import java.lang.reflect.Method;
import java.util.Collections;


public class Reader {
    private OpenAPI openAPI;

    public Reader(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }


    public Operation parseMethod(Method method) {
        //JavaType classType = TypeFactory.defaultInstance().constructType(method.getDeclaringClass());
        //BeanDescription bd = new ObjectMapper().getSerializationConfig().introspect(classType);
        //return parseMethod(classType.getClass(), method, bd.findMethod(method.getName(), method.getParameterTypes()),
        //Collections.<Parameter> emptyList(), Collections.<ApiResponse> emptyList());
        return new Operation();
    }
}
