package io.swagger.v3.jaxrs2.annotations.requestbody;

import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.resources.RequestBodyResource;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

/**
 * Testing RequestBody Resource
 * Created by rafaellopez on 12/6/17.
 */
public class RequestBodyTest {
    @Test(description = "scan class with requesbody annotation")
    public void scanClassAndFieldLevelAnnotations() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(RequestBodyResource.class);
        Yaml.prettyPrint(openAPI);
    }
}
