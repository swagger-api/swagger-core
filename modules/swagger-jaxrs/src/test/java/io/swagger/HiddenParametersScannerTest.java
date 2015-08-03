package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.resources.HiddenParametersResource;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class HiddenParametersScannerTest {
    private final Swagger swagger = new Reader(new Swagger()).read(HiddenParametersResource.class);

    @Test
    public void shouldScanMethodWithAllParamsHidden() throws Exception {
        Operation get = getGet("/all-hidden/{id}");

        assertNotNull(get);
        assertEquals(0, get.getParameters().size());
    }

    @Test
    public void shouldScanMethodWithSomeParamsHidden() {
        Operation get = getGet("/some-hidden/{id}");

        assertNotNull(get);
        List<Parameter> parameters = get.getParameters();
        assertEquals(parameters.size(), 3);
        assertEquals(parameters.get(0).getIn(), "cookie");
        assertEquals(parameters.get(1).getIn(), "header");
        assertEquals(parameters.get(2).getIn(), "query");
    }

    @Test
    public void shouldScanMethodWithOtherParamsHidden() {
        Operation get = getGet("/others-hidden/{id}");

        assertNotNull(get);
        List<Parameter> parameters = get.getParameters();
        assertEquals(parameters.size(), 3);
        assertEquals(parameters.get(0).getIn(), "body");
        assertEquals(parameters.get(1).getIn(), "formData");
        assertEquals(parameters.get(2).getIn(), "path");
    }

    private Operation getGet(String path) {
        return swagger.getPaths().get(path).getGet();
    }
}
