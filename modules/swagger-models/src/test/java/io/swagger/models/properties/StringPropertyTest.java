package io.swagger.models.properties;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class StringPropertyTest {
	@Test
	public void testGettersAndSetters() {
		StringProperty stringProperty = new StringProperty();
		String vendorName = "x-vendor";
		String value = "value";
		stringProperty.vendorExtension(vendorName, value);
		assertEquals(stringProperty.getVendorExtensions().get(vendorName), value);

		stringProperty._enum(value);
		assertTrue(stringProperty.getEnum().contains(value));
	}

	@Test
	public void testFromName() {
		assertEquals(StringProperty.Format.fromName("byte"), StringProperty.Format.BYTE);
		assertNull(StringProperty.Format.fromName("unknown"));
		assertEquals(StringProperty.Format.valueOf("BYTE"),  StringProperty.Format.BYTE);
	}
}
