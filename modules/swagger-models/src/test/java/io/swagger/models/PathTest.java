package io.swagger.models;

import io.swagger.models.parameters.Parameter;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class PathTest {
    @Test
    public void testOperations() {
        // when
        Path path = new Path();

        // then
        assertTrue(path.isEmpty(), "A new path must be empty");

        // given
        Operation get = new Operation();

        // when
        path.set("get", get);

        // then
        assertFalse(path.isEmpty(), "A path with operations must not be empty");
        assertTrue(path.getOperations().contains(get), "The path must contain the operations that have been put");
        assertEquals(path.getOperationMap().get(HttpMethod.GET), get,
                "The operation that has been put must be the same that we get");

        // given
        Operation put = new Operation();

        // when
        path.set("put", put);

        // then
        assertTrue(path.getOperations().contains(put), "The path must contain the operations that have been put");
        assertEquals(path.getOperationMap().get(HttpMethod.PUT), get,
                "The operation that has been put must be the same that we get");

        // given
        Operation post = new Operation();

        // when
        path.set("post", post);

        // then
        assertTrue(path.getOperations().contains(post), "The path must contain the operations that have been put");
        assertEquals(path.getOperationMap().get(HttpMethod.POST), get,
                "The operation that has been put must be the same that we get");

        // given
        Operation head = new Operation();

        // when
        path.set("head", head);

        // then
        assertTrue(path.getOperations().contains(head), "The path must contain the operations that have been put");
        assertEquals(path.getOperationMap().get(HttpMethod.HEAD), get,
                "The operation that has been put must be the same that we get");

        // given
        Operation delete = new Operation();

        // when
        path.set("delete", delete);

        // then
        assertTrue(path.getOperations().contains(delete), "The path must contain the operations that have been put");
        assertEquals(path.getOperationMap().get(HttpMethod.DELETE), get,
                "The operation that has been put must be the same that we get");

        // given
        Operation patch = new Operation();

        // when
        path.set("patch", patch);

        // then
        assertTrue(path.getOperations().contains(patch), "The path must contain the operations that have been put");
        assertEquals(path.getOperationMap().get(HttpMethod.PATCH), get,
                "The operation that has been put must be the same that we get");

        // given
        Operation options = new Operation();

        // when
        path.set("options", options);

        // then
        assertTrue(path.getOperations().contains(options), "The path must contain the operations that have been put");
        assertEquals(path.getOperationMap().get(HttpMethod.OPTIONS), get,
                "The operation that has been put must be the same that we get");

        // when
        Path operation = path.set("", null);

        // then
        assertNull(operation, "The returned operation must be null as we set an unknown method");
    }

    @Test
    public void testParameters() {
        // given
        Path path = new Path();
        Parameter parameter = Mockito.mock(Parameter.class);
        path.setParameters(null);

        // when
        path.addParameter(parameter);

        // then
        assertTrue(path.getParameters().contains(parameter), "The parameters list must contain the newly added one");
    }

    @Test
    public void testSetVendorExtension() {
        // given
        Path path = new Path();
        String vendorName = "x-vendor";
        String value = "value";

        // when
        path.setVendorExtension(vendorName, value);

        // then
        assertEquals(path.getVendorExtensions().get(vendorName), value,
                "Must be able to retrieve the same value from the map");
    }
}
