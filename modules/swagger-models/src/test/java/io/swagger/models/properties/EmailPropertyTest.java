package io.swagger.models.properties;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class EmailPropertyTest {
	@Test
	public void testGettersAndSetters() {
		EmailProperty emailProperty = new EmailProperty(new StringProperty());
		String vendorName = "x-vendor";
		String value = "value";
		emailProperty.vendorExtension(vendorName, value);
		assertEquals(emailProperty.getVendorExtensions().get(vendorName), value);
		
		emailProperty._enum(value);
		assertTrue(emailProperty.getEnum().contains(value));
		
		emailProperty.setEnum(null);
		assertNull(emailProperty.getEnum());
		
		
	}
}
