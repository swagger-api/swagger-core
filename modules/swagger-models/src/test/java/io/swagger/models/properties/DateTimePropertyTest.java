package io.swagger.models.properties;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class DateTimePropertyTest {
	@Test
	public void testGettersAndSetters() {
		DateTimeProperty dateProperty = new DateTimeProperty();
		String vendorName = "x-vendor";
		String value = "value";
		dateProperty.vendorExtension(vendorName, value);
		assertEquals(dateProperty.getVendorExtensions().get(vendorName), value);
		
		dateProperty._enum(value);
		assertTrue(dateProperty.getEnum().contains(value));
		
		dateProperty.setEnum(null);
		assertNull(dateProperty.getEnum());
	}
}
