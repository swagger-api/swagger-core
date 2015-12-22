package io.swagger.models.properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class ObjectPropertyTest {
	@Test
	public void testGettersAndSetters() {
		ObjectProperty objectProperty = new ObjectProperty(null);
		
		DoubleProperty property=new DoubleProperty();
		String name="name";
		objectProperty.property(name, property);
		assertEquals(objectProperty.getProperties().get(name), property);
		
		String defaul="default";
		objectProperty._default(defaul);
		
		objectProperty.readOnly(true);
		assertTrue(objectProperty.getReadOnly());
		
		objectProperty.required(true);
		assertTrue(objectProperty.getRequired());

		String vendorName = "x-vendor";
		String value = "value";
		objectProperty.vendorExtension(vendorName, value);
		assertEquals(objectProperty.getVendorExtensions().get(vendorName), value);
	}
}
