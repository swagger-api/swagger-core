package io.swagger.models.properties;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class FloatPropertyTest {
	@Test
	public void testGettersAndSetters() {
		FloatProperty floatProperty = new FloatProperty();
		float defaul=2.3F;

		floatProperty._default("2.3");
		assertEquals(floatProperty.getDefault(),defaul);
		floatProperty._default("hello");
		assertEquals(floatProperty.getDefault(),defaul);

		floatProperty.example(defaul);
		assertEquals(floatProperty.getExample(), "2.3");
		
		List<Float>_enum=Arrays.asList(1F);
		floatProperty.setEnum(_enum);
		assertEquals(floatProperty.getEnum(), _enum);

		String vendorName = "x-vendor";
		String value = "value";
		floatProperty.vendorExtension(vendorName, value);
		assertEquals(floatProperty.getVendorExtensions().get(vendorName), value);
	}
}
