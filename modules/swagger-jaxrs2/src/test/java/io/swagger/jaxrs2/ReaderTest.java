package io.swagger.jaxrs2;

import io.swagger.jaxrs2.resources.SimpleMethods;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import org.testng.annotations.Test;
import org.testng.Assert;


import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;


public class ReaderTest {
    @Test(description = "scan methods")
    public void scanMethods() {
        Method[] methods = SimpleMethods.class.getMethods();
        Reader reader = new Reader(new OpenAPI());
        for (final Method method : methods) {
            if (isValidRestPath(method)) {
                Operation operation = reader.parseMethod(method);
                Assert.assertNotNull(operation);
            }
        }

    }

    private Boolean isValidRestPath(Method method) {
        for (Class<? extends Annotation> item : Arrays.asList(GET.class, PUT.class, POST.class, DELETE.class,
                OPTIONS.class, HEAD.class)) {
            if (method.getAnnotation(item) != null) {
                return true;
            }
        }
        return false;
    }
}
