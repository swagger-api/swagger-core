package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.DefaultReaderConfig;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.resources.RegexPathParamResource;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RegexPathParamTest {

    @Test(description = "scan a simple resource")
    public void scanSimpleResource() {
        DefaultReaderConfig config = new DefaultReaderConfig();
        config.setScanAllResources(true);
        Swagger swagger = new Reader(new Swagger(), config).read(RegexPathParamResource.class);
        Operation get = swagger.getPaths().get("/{report_type}").getGet();
        Parameter param = get.getParameters().get(0);
        assertEquals(param.getName(), "report_type");
        assertEquals(param.getPattern(), "[aA-zZ]+");
    }
}
