package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.resources.ResourceWithBeanParams;
import io.swagger.resources.ResourceWithComplexBodyInputType;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ScannerTest {

    @Test(description = "scan a simple resource")
    public void scanSimpleResource() {
        final Swagger swagger = getSwagger(ResourceWithBeanParams.class);
        final List<Parameter> params = getParameters(swagger, "/{id}");

        final Parameter skip = params.get(0);
        assertEquals(skip.getName(), "skip");
        assertEquals(skip.getDescription(), "number of records to skip");

        final Parameter limit = params.get(1);
        assertEquals(limit.getName(), "limit");
        assertEquals(limit.getDescription(), "maximum number of records to return");
    }

    @Test(description = "scan another resource")
    public void scanAnotherResource() {
        final Swagger swagger = getSwagger(ResourceWithComplexBodyInputType.class);

        final Operation post = swagger.getPaths().get("/myapi/testPostWithBody").getPost();
        assertNotNull(post);

        assertNotNull(swagger.getDefinitions());
        assertNotNull(swagger.getDefinitions().get("ClassWithString"));
    }

    @Test(description = "scan a bean param resource")
    public void scanBeanParamResource() {
        final Swagger swagger = getSwagger(ResourceWithBeanParams.class);
        final List<Parameter> params = getParameters(swagger, "/bean/{id}");

        final HeaderParameter headerParam1 = (HeaderParameter) params.get(0);
        assertEquals(headerParam1.getDefaultValue(), "1");
        assertEquals(headerParam1.getName(), "test order annotation 1");

        final HeaderParameter headerParam2 = (HeaderParameter) params.get(1);
        assertEquals(headerParam2.getDefaultValue(), "2");
        assertEquals(headerParam2.getName(), "test order annotation 2");

        final QueryParameter priority1 = (QueryParameter) params.get(2);
        assertEquals(priority1.getDefaultValue(), "overridden");
        assertEquals(priority1.getName(), "test priority 1");

        final QueryParameter priority2 = (QueryParameter) params.get(3);
        assertEquals(priority2.getDefaultValue(), "overridden");
        assertEquals(priority2.getName(), "test priority 2");

        final ModelImpl bodyParam1 = (ModelImpl) ((BodyParameter) params.get(4)).getSchema();
        assertEquals(bodyParam1.getDefaultValue(), "bodyParam");
    }

    private List<Parameter> getParameters(Swagger swagger, String path) {
        return swagger.getPaths().get(path).getGet().getParameters();
    }

    private Swagger getSwagger(Class<?> clas) {
        return new Reader(new Swagger()).read(clas);
    }
}
