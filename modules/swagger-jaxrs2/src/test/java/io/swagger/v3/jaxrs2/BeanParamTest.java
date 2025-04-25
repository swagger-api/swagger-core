package io.swagger.v3.jaxrs2;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

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

}