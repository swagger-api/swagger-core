package io.swagger.jaxrs2;

import io.swagger.oas.models.OpenAPI;
import org.testng.annotations.Test;



public class ReaderTest {
    @Test(description = "scan methods")
    public void scanMethods() {

        Reader reader = new Reader(new OpenAPI());

    }
}
