package io.swagger.models.properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class MapPropertyTest {
	@Test
	public void testGettersAndSetters() {
		MapProperty mapProperty = new MapProperty(new IntegerProperty());

		assertTrue(mapProperty.isType("object", null));
		
		DoubleProperty property=new DoubleProperty();
		mapProperty.additionalProperties(property);
		assertEquals(mapProperty.getAdditionalProperties(), property);

		String vendorName = "x-vendor";
		String value = "value";
		mapProperty.vendorExtension(vendorName, value);
		assertEquals(mapProperty.getVendorExtensions().get(vendorName), value);
	}
}
