package io.swagger.models.properties;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class ArrayPropertyTest {
	@Test
	public void testGettersAndSetters() {
		Property items = new BooleanProperty();
		ArrayProperty arrayProperty = new ArrayProperty(items);
		arrayProperty.uniqueItems();
		assertTrue(arrayProperty.getUniqueItems());

		String vendorName = "x-vendor";
		String value = "value";
		arrayProperty.vendorExtension(vendorName, value);
		assertEquals(arrayProperty.getVendorExtensions().get(vendorName), value);
	}
}
