package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.resources.model.FormParamBean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.*;

import static org.testng.Assert.assertNotNull;

public class FormParamBeanTest {

    @Path("/")
    private static class MyFormBeanParamResource {
        @GET
        @Consumes("application/x-www-form-urlencoded")
        public String getWithFormBeanParam(@BeanParam FormParamBean formParamBean) {
            return "result";
        }
    }

    @Test(description = "check schema of serialized BeanParam containing FormParams")
    public void shouldSerializeTypeParameter() {
        OpenAPI openApi = new Reader(new OpenAPI()).read(MyFormBeanParamResource.class);
        RequestBody requestBody = openApi.getPaths().get("/").getGet().getRequestBody();
        assertNotNull(requestBody);
        MediaType mediaType = requestBody.getContent().get("application/x-www-form-urlencoded");
        assertNotNull(mediaType);
        Schema schema = mediaType.getSchema();
        Assert.assertEquals(schema.getProperties().size(), 3);
        Assert.assertEquals(schema.getProperties().get("param1"), new StringSchema());
        Assert.assertEquals(schema.getProperties().get("param2"), new StringSchema());
        Assert.assertEquals(schema.getProperties().get("param3"), new StringSchema());
    }

}