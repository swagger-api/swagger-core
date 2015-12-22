package io.swagger.models.properties;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class DecimalPropertyTest {
	@Test
	public void testGettersAndSetters() {
		DecimalProperty dateProperty = new DecimalProperty();
		String vendorName = "x-vendor";
		String value = "value";
		dateProperty.vendorExtension(vendorName, value);
		assertEquals(dateProperty.getVendorExtensions().get(vendorName), value);
	}
}
