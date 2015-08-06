package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.resources.CollectionFormatResource;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class CollectionFormatTest {
    private static final String MULTI = "multi";
    private static final String CSV = "csv";
    private final Swagger swagger = new Reader(new Swagger()).read(CollectionFormatResource.class);

    @Test(testName = "check collection format for QueryParam")
    public void readQueryParamTest() {
        Operation operation = getOperation("testQueryParam");
        assertEquals(getCollectionFormat(operation, 0), MULTI);
        assertNull(getCollectionFormat(operation, 1));
        assertEquals(getCollectionFormat(operation, 2), MULTI);
    }

    @Test(testName = "check collection format for FormParam")
    public void readFormParamTest() {
        Operation operation = getOperation("testFormParam");
        assertEquals(getCollectionFormat(operation, 0), MULTI);
        assertNull(getCollectionFormat(operation, 1));
        assertEquals(getCollectionFormat(operation, 2), MULTI);
    }

    @Test(testName = "check collection format for PathParam")
    public void readPathParamTest() {
        Operation operation = getOperation("testPathParam");
        assertEquals(getCollectionFormat(operation, 0), CSV);
        assertNull(getCollectionFormat(operation, 1));
        assertEquals(getCollectionFormat(operation, 2), CSV);
    }

    @Test(testName = "check collection format for HeaderParam")
    public void readHeaderParamTest() {
        Operation operation = getOperation("testHeaderParam");
        assertEquals(getCollectionFormat(operation, 0), CSV);
        assertNull(getCollectionFormat(operation, 1));
        assertEquals(getCollectionFormat(operation, 2), CSV);
    }

    @Test(testName = "check collection format for CookieParam")
    public void readCookieParamTest() {
        Operation operation = getOperation("testCookieParam");
        assertEquals(getCollectionFormat(operation, 0), CSV);
        assertNull(getCollectionFormat(operation, 1));
        assertEquals(getCollectionFormat(operation, 2), CSV);
    }

    @Test(testName = "check collection format for Mixed Param")
    public void readMixedParamTest() {
        Operation operation = getOperation("testMixedParam");
        assertEquals(getCollectionFormat(operation, 0), MULTI);
        assertEquals(getCollectionFormat(operation, 1), CSV);
        assertNull(getCollectionFormat(operation, 2));
        assertEquals(getCollectionFormat(operation, 3), CSV);
    }

    private Operation getOperation(String name) {
        return swagger.getPath("/collectionFormat/" + name).getPost();
    }

    private String getCollectionFormat(Operation op, int index) {
        return ((AbstractSerializableParameter) op.getParameters().get(index)).getCollectionFormat();
    }
}
