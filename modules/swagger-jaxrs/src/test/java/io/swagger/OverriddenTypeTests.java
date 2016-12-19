package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.resources.Resource469;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class OverriddenTypeTests {
    @Test(description = "add a user-specified format")
    public void passwordParameterTest() {
        Swagger swagger = new Reader(new Swagger()).read(Resource469.class);

        QueryParameter qp = (QueryParameter)swagger.getPath("/test").getGet().getParameters().get(0);
        assertEquals(qp.getFormat(), "password");
    }

    @Test(description = "overrides a system-detected format")
    public void integerFormatOverrideTest() {
        Swagger swagger = new Reader(new Swagger()).read(Resource469.class);

        QueryParameter qp = (QueryParameter)swagger.getPath("/test").getPost().getParameters().get(0);
        assertEquals(qp.getFormat(), "int64");
    }

    @Test(description = "overrides a system-detected type")
    public void integerTypeOverrideTest() {
        Swagger swagger = new Reader(new Swagger()).read(Resource469.class);

        QueryParameter qp = (QueryParameter)swagger.getPath("/test").getPut().getParameters().get(0);
        assertEquals(qp.getType(), "string");
        assertEquals(qp.getFormat(), "blah");
    }

    @Test(description = "add a user-specified type with implicit annotation")
    public void implicitParamTest() {
        Swagger swagger = new Reader(new Swagger()).read(Resource469.class);

        QueryParameter qp = (QueryParameter)swagger.getPath("/test2").getGet().getParameters().get(0);
        assertEquals(qp.getType(), "string");
        assertEquals(qp.getFormat(), "password");
    }
}
