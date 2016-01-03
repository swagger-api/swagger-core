package io.swagger.models;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class HttpMethodTest {

	@Test
	public void testValues() {
		// when
		List<HttpMethod> values = Arrays.asList(HttpMethod.values());

		// then
		assertTrue(values.contains(HttpMethod.DELETE), "The DELETE value must appear in the values");
	}

	@Test
	public void testValueOf() {
		// when
		HttpMethod value = HttpMethod.valueOf("DELETE");

		// then
		assertEquals(value, HttpMethod.DELETE, "ValueOf must retrieve the DELETE value");
	}
}
