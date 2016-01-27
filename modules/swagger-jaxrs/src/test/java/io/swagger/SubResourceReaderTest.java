package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.resources.Resource942;
import io.swagger.resources.ResourceWithSubResources;
import io.swagger.resources.SubResourceHead;
import io.swagger.resources.TestResource;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SubResourceReaderTest {

    @Test(description = "scan a resource with subresources")
    public void readResourceWithSubresources() {
        final Swagger swagger = getSwagger(ResourceWithSubResources.class);
        assertEquals(getOperationId(swagger, "/employees/{id}"), "getAllEmployees");
        assertEquals(getOperationId(swagger, "/employees/{id}/{id}"), "getSubresourceOperation");
        assertEquals(getOperationId(swagger, "/employees/noPath"), "getGreeting");
    }

    @Test(description = "scan another resource with subresources")
    public void readAnotherResourceWithSubresources() {
        final Swagger swagger = getSwagger(TestResource.class);
        final Operation get = getGet(swagger, "/test/more/otherStatus");
        assertEquals(get.getOperationId(), "otherStatus");

        final Parameter qp = get.getParameters().get(0);
        assertEquals(qp.getIn(), "query");
        assertEquals(qp.getName(), "qp");

        assertTrue(get.getProduces().containsAll(Arrays.asList("application/json", "application/xml")));

        assertEquals(swagger.getPaths().keySet().size(), 2);
    }

    @Test(description = "scan resource with class-based sub-resources")
    public void readResourceWithClassBasedSubresources() {
        final Swagger swagger = getSwagger(SubResourceHead.class);
        assertEquals(swagger.getPaths().size(), 3);
        assertEquals(getOperationId(swagger, "/head/noPath"), "getGreeting");
        assertEquals(getOperationId(swagger, "/head/tail/hello"), "getGreeting");

        final Operation echo = getGet(swagger, "/head/tail/{string}");
        assertEquals(echo.getOperationId(), "getEcho");
        assertEquals(echo.getParameters().size(), 1);
    }

    @Test(description = "find a body param")
    public void findBodyParam() {
        Swagger swagger = getSwagger(Resource942.class);
        assertTrue(swagger.getPath("/test").getPost().getParameters().get(0) instanceof BodyParameter);
    }

    private Swagger getSwagger(Class<?> cls) {
        return new Reader(new Swagger()).read(cls);
    }

    private Operation getGet(Swagger swagger, String path) {
        return swagger.getPaths().get(path).getGet();
    }

    private String getOperationId(Swagger swagger, String path) {
        return getGet(swagger, path).getOperationId();
    }
}
