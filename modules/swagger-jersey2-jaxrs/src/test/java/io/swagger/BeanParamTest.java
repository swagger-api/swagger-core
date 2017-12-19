package io.swagger;

import io.swagger.annotations.Api;
import io.swagger.jaxrs.Reader;
import io.swagger.models.ListOfStringsBeanParam;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

public class BeanParamTest {

    @Api
    @Path("/")
    private static class MyBeanParamResource {
        @GET
        public String getWithBeanParam(@BeanParam ListOfStringsBeanParam listOfStringsBean) {
            return "result";
        }
    }

    @Test(description = "check array type of serialized BeanParam containing QueryParams") // tests issue #2466
    public void shouldSerializeTypeParameter() {
        Swagger swagger = new Reader(new Swagger()).read(MyBeanParamResource.class);
        List<Parameter> getOperationParams = swagger.getPath("/").getGet().getParameters();
        Assert.assertEquals(getOperationParams.size(), 1);
        QueryParameter param = (QueryParameter) getOperationParams.get(0);
        Assert.assertEquals(param.getName(), "listOfStrings");
        Assert.assertEquals(param.getType(), "array");
        // These are the important checks:
        Property itemsProperty = param.getItems();
        Assert.assertEquals(itemsProperty.getClass(), StringProperty.class);
        Assert.assertEquals(itemsProperty.getType(), "string");
    }

}
