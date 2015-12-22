package io.swagger.models.parameters;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RefParameterTest {

	private RefParameter parameter;

	@BeforeMethod
	public void setup() {
		parameter=new RefParameter("ref");
	}

	@Test
	public void testAsDefault() {
		parameter.asDefault("ref");
		assertEquals(parameter.get$ref(), "#/parameters/ref");
		assertEquals(parameter.getSimpleRef(), "ref");
		
	}

	@Test
	public void testGetRequired() {
		assertFalse(parameter.getRequired());
	}

	@Test
	public void testIsType() {
		assertTrue(RefParameter.isType("$ref", "format"));
		assertFalse(RefParameter.isType("$ref0", "format"));
	}
}
