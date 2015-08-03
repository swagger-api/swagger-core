package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.resources.RegexPathParamResource;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RegexPathParamTest {

    @Test(description = "scan a simple resource")
    public void scanSimpleResource() {
        Swagger swagger = new Reader(new Swagger()).read(RegexPathParamResource.class);
        Operation get = swagger.getPaths().get("/{report_type}").getGet();
        Parameter param = get.getParameters().get(0);
        assertEquals(param.getName(), "report_type");
        assertEquals(param.getPattern(), "[aA-zZ]+");
    }
}
