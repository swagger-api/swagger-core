package io.swagger.models.parameters;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class AbstractParameterTest {
	@Test
	public void testGettersAndSetters() {
		AbstractParameter abstractParameter = new AbstractParameterTestImpl();
		String in = "in";
		String name = "name";
		abstractParameter.setIn(in);
		abstractParameter.setName(name);
		assertEquals(abstractParameter.getIn(), in);
		assertEquals(abstractParameter.getName(), name);

		String description = "description";
		abstractParameter.setDescription(description);
		assertEquals(abstractParameter.getDescription(), description);

		Boolean required = true;
		abstractParameter.setRequired(required);
		assertTrue(abstractParameter.getRequired());

		String access = "access";
		abstractParameter.setAccess(access);
		assertEquals(abstractParameter.getAccess(), access);

		String pattern = "pattern";
		abstractParameter.setPattern("pattern");
		assertEquals(abstractParameter.getPattern(), pattern);

		String vendorName = "x-vendor";
		String value = "value";
		abstractParameter.setVendorExtension(vendorName, value);
		assertEquals(abstractParameter.getVendorExtensions().get(vendorName), value);
	}
}
