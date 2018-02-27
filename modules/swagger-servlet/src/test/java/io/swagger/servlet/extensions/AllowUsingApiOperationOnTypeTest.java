package io.swagger.servlet.extensions;

import io.swagger.models.Swagger;
import io.swagger.servlet.Reader;
import io.swagger.servlet.resources.ApiOperationResource;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;

public class AllowUsingApiOperationOnTypeTest {

    private static final String USERS_PATH = "/users";

    @Test
    public void testThatApiOperationMightBeInType() {
        final Swagger swagger = new Swagger();
        Reader.read(swagger, Collections.<Class<?>>singleton(ApiOperationResource.class));
        Assert.assertNotNull(swagger.getPaths().get(USERS_PATH));
    }
}
