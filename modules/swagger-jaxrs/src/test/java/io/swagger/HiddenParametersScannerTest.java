package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import io.swagger.jaxrs.Reader;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;

import org.testng.annotations.Test;

import resources.HiddenParametersResource;

public class HiddenParametersScannerTest {
    @Test
    public void shouldScanMethodWithAllParamsHidden() throws Exception {
        Swagger swagger = new Reader(new Swagger()) .read(HiddenParametersResource.class);
        Path path = swagger.getPaths().get("/all-hidden/{id}");
        Operation get = path.getGet();

        assertNotNull(get);
        assertEquals(0, get.getParameters().size());
    }

    @Test
    public void shouldScanMethodWithSomeParamsHidden() {
        Swagger swagger = new Reader(new Swagger()) .read(HiddenParametersResource.class);
        Path path = swagger.getPaths().get("/some-hidden/{id}");
        Operation get = path.getGet();

        assertNotNull(get);
        assertEquals(get.getParameters().size(), 3);
        assertEquals(get.getParameters().get(0).getIn(), "cookie");
        assertEquals(get.getParameters().get(1).getIn(), "header");
        assertEquals(get.getParameters().get(2).getIn(), "query");
    }

     @Test public void shouldScanMethodWithOtherParamsHidden() {
     Swagger swagger = new Reader(new Swagger()).read(HiddenParametersResource.class);
     Path path = swagger.getPaths().get("/others-hidden/{id}");
     Operation get = path.getGet();

     assertNotNull(get);
     assertEquals(get.getParameters().size(), 3);
     assertEquals(get.getParameters().get(0).getIn(), "body");
     assertEquals(get.getParameters().get(1).getIn(), "formData");
     assertEquals(get.getParameters().get(2).getIn(), "path");
     }
}
