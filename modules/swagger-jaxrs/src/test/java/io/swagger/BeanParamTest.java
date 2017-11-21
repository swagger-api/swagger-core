package io.swagger;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.swagger.annotations.Api;
import io.swagger.jaxrs.DefaultParameterExtension;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.models.ListOfStringsBeanParam;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

public class BeanParamTest {

    @Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MyBeanParam {
        // Remove this Annotation and use BeanParam directly with Swagger Core 2.0
    }

    @Api
    @Path("/")
    private static class MyBeanParamResource {
        @GET
        public String getWithBeanParam(@MyBeanParam ListOfStringsBeanParam listOfStringsBean) {
            return "result";
        }
    }

    private final SwaggerExtension myDefaultParameterExtension = new DefaultParameterExtension() {
        @Override
        protected boolean isBeanParametersAggregatorAnnotation(Annotation annotation) {
            return annotation instanceof MyBeanParam;
        }
    };

    private List<SwaggerExtension> originalExtensions;

    @BeforeMethod
    public void beforeMethod() {
        originalExtensions = SwaggerExtensions.getExtensions();
        SwaggerExtensions.setExtensions(Collections.singletonList(myDefaultParameterExtension));
    }

    @AfterMethod
    public void afterMethod() {
        SwaggerExtensions.setExtensions(originalExtensions);
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
