package io.swagger.models.properties;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class IntegerPropertyTest {
	@Test
	public void testGettersAndSetters() {
		IntegerProperty integerProperty = new IntegerProperty();
		int defaul=6;

		integerProperty._default("6");
		assertEquals(integerProperty.getDefault(),(Integer)defaul);
		integerProperty._default("hello");
		assertEquals(integerProperty.getDefault(),(Integer)defaul);

		integerProperty.example(defaul);
		assertEquals(integerProperty.getExample(), "6");
		
		List<Integer>_enum=Arrays.asList(1);
		integerProperty.setEnum(_enum);
		assertEquals(integerProperty.getEnum(), _enum);

		String vendorName = "x-vendor";
		String value = "value";
		integerProperty.vendorExtension(vendorName, value);
		assertEquals(integerProperty.getVendorExtensions().get(vendorName), value);
	}
}
