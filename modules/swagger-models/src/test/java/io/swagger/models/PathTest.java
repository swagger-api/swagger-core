package io.swagger.models;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.HashMap;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import io.swagger.models.parameters.Parameter;

public class PathTest {
	@Test
	public void testOperations() {
		Operation get = new Operation();
		Operation put = new Operation();
		Operation post = new Operation();
		Operation head = new Operation();
		Operation delete = new Operation();
		Operation patch = new Operation();
		Operation options = new Operation();
		
		Path path = new Path();
		assertTrue(path.isEmpty());
		
		path.set("get", get);
		path.set("put", put);
		path.set("post", post);
		path.set("head", head);
		path.set("delete", delete);
		path.set("patch", patch);
		path.set("options", options);
		path.set("",null);
		assertFalse(path.isEmpty());
		assertTrue(path.getOperations().containsAll(Arrays.asList(get, put, post, head, delete, patch, options)));
		assertEquals(path.getOperationMap().get(HttpMethod.GET), get);

		Parameter parameter = Mockito.mock(Parameter.class);
		path.setParameters(null);
		path.addParameter(parameter);
		assertTrue(path.getParameters().contains(parameter));

		String vendorName = "x-vendor";
		String value = "value";
		path.setVendorExtension(vendorName, value);
		assertEquals(path.getVendorExtensions().get(vendorName), value);
	}
}
