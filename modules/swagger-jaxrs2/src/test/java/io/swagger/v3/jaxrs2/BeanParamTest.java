package io.swagger.v3.jaxrs2;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.swagger.v3.jaxrs2.resources.model.ListOfStringsBeanParam;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;

public class BeanParamTest {

    @Path("/")
    private static class MyBeanParamResource {
        @GET
        public String getWithBeanParam(@BeanParam ListOfStringsBeanParam listOfStringsBean) {
            return "result";
        }
    }

    @Test(description = "check array type of serialized BeanParam containing QueryParams") // tests issue #2466
    public void shouldSerializeTypeParameter() {
        OpenAPI openApi = new Reader(new OpenAPI()).read(MyBeanParamResource.class);
        List<Parameter> getOperationParams = openApi.getPaths().get("/").getGet().getParameters();
        Assert.assertEquals(getOperationParams.size(), 1);
        Parameter param = getOperationParams.get(0);
        Assert.assertEquals(param.getName(), "listOfStrings");
        Schema<?> schema = param.getSchema();
        // These are the important checks:
        Assert.assertEquals(schema.getClass(), ArraySchema.class);
        Assert.assertEquals(((ArraySchema) schema).getItems().getType(), "string");   
    }

}