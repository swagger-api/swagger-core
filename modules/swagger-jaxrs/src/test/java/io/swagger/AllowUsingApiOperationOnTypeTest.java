package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.resources.ApiOperationResource;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AllowUsingApiOperationOnTypeTest {

    private static final String USERS_PATH = "/users";
    private static final String TEST_DESCRIPTION = "Test Uber Description";
    private static final String META_DATA__DESCRIPTION = "Meta DataUber Description";

    @Test
    public void testThatApiOperationMightBeInType() {
        final Swagger swagger = getSwagger(ApiOperationResource.class);
        final Path usersPath = swagger.getPaths().get(USERS_PATH);
        Assert.assertNotNull(usersPath);
        Assert.assertEquals(TEST_DESCRIPTION, usersPath.getGet().getDescription());
        Assert.assertEquals(META_DATA__DESCRIPTION, usersPath.getGet().getSummary());
    }

    private Swagger getSwagger(final Class<?> cls) {
        return new Reader(new Swagger()).read(cls);
    }

}
