package io.swagger.models.properties;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class BooleanPropertyTest {
	@Test
	public void testGettersAndSetters() {
		BooleanProperty booleanProperty = new BooleanProperty();
		booleanProperty._default(false);
		assertFalse(booleanProperty.getDefault());

		booleanProperty._default("true");
		assertTrue(booleanProperty.getDefault());
		booleanProperty._default("unknown");
		assertFalse(booleanProperty.getDefault());
		
		booleanProperty.example(true);
		assertEquals(booleanProperty.getExample(), "true");

		String vendorName = "x-vendor";
		String value = "value";
		booleanProperty.vendorExtension(vendorName, value);
		assertEquals(booleanProperty.getVendorExtensions().get(vendorName), value);
	}
}
