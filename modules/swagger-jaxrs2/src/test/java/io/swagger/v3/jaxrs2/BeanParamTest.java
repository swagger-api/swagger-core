package io.swagger.v3.jaxrs2;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.swagger.v3.jaxrs2.resources.model.NestedBeanParam;
import io.swagger.v3.oas.models.media.IntegerSchema;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.swagger.v3.jaxrs2.resources.model.ListOfStringsBeanParam;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;

import static org.testng.Assert.assertEquals;

public class BeanParamTest {

    @Path("/")
    private static class MyBeanParamResource {
        @GET
        public String getWithBeanParam(@BeanParam ListOfStringsBeanParam listOfStringsBean) {
            return "result";
        }

        @GET
        @Path("/nested-param")
        public String getWithNestedBeanParam(@BeanParam NestedBeanParam listOfStringsBean) {
            return "result";
        }

    }

    @Test(description = "check array type of serialized BeanParam containing QueryParams") // tests issue #2466
    public void shouldSerializeTypeParameter() {
        OpenAPI openApi = new Reader(new OpenAPI()).read(MyBeanParamResource.class);
        List<Parameter> getOperationParams = openApi.getPaths().get("/").getGet().getParameters();
        assertEquals(getOperationParams.size(), 1);
        Parameter param = getOperationParams.get(0);
        assertEquals(param.getName(), "listOfStrings");
        Schema<?> schema = param.getSchema();
        // These are the important checks:
        assertEquals(schema.getClass(), ArraySchema.class);
        assertEquals(schema.getItems().getType(), "string");
    }

    @Test(description = "check integer type of nested BeanParam containing a QueryParam") // tests issue #2466
    public void shouldSerializeNestedTypeParameter() {
        OpenAPI openApi = new Reader(new OpenAPI()).read(MyBeanParamResource.class);
        List<Parameter> getOperationParams = openApi.getPaths().get("/nested-param").getGet().getParameters();
        Assert.assertEquals(getOperationParams.size(), 1);

        Parameter queryParam = getOperationParams.get(0);
        Assert.assertEquals(queryParam.getName(), "queryParam");
        Schema<?> schema = queryParam.getSchema();
        Assert.assertEquals(schema.getClass(), IntegerSchema.class);
        Assert.assertEquals(schema.getType(), "integer");
        Assert.assertEquals(((IntegerSchema) schema).getDefault(), 10);
    }

}