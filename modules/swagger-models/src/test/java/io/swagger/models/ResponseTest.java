package io.swagger.models;

import static org.testng.Assert.*;

import java.util.HashMap;

import org.testng.annotations.Test;

import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.Property;

public class ResponseTest {
	@Test
	public void testGettersAndSetters() {
		Response response = new Response();
		String type = "type";
		Object example = "example";
		response.example(type, example);
		assertEquals(example, response.getExamples().get(type));

		String name = "name";
		Property property = new BooleanProperty();
		response.header(name, property);
		assertEquals(response.getHeaders().get("name"), property);

		String vendorName = "x-vendor";
		String value = "value";
		response.vendorExtension(vendorName, value);
		assertEquals(response.getVendorExtensions().get(vendorName), value);

	}
}
