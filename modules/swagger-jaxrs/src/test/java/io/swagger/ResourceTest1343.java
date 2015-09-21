package io.swagger;

import org.testng.annotations.Test;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Swagger;
import io.swagger.resources.Resource1343;
import io.swagger.util.Json;

public class ResourceTest1343 {
    private final Reader reader = new Reader(new Swagger());

    private Swagger getSwagger(Class<?> resource) {
        return reader.read(resource);
    }

    @Test(description = "scan resource 1343")
    public void scanResource1343() {
        Swagger swagger = getSwagger(Resource1343.class);
        Json.prettyPrint(swagger);
    }
}
