package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Swagger;
import io.swagger.resources.JsonIdentityResource;
import io.swagger.util.ResourceUtils;

import org.testng.annotations.Test;

import java.io.IOException;

public class JsonIdentityTest {

    @Test(description = "Scan API with operation and response references")
    public void scan() throws IOException {
        final Swagger swagger = new Reader(new Swagger()).read(JsonIdentityResource.class);
        final String json = ResourceUtils.loadClassResource(getClass(), "ResourceWithJsonIdentity.json");
        SerializationMatchers.assertEqualsToJson(swagger, json);
    }
}
