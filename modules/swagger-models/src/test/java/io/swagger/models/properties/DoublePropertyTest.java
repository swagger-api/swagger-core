package io.swagger.models.properties;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class DoublePropertyTest {
	@Test
	public void testGettersAndSetters() {
		DoubleProperty doubleProperty = new DoubleProperty();
		double defaul=2.3;
		doubleProperty._default(defaul);
		assertEquals(doubleProperty.getDefault(),defaul);

		doubleProperty._default("2.3");
		assertEquals(doubleProperty.getDefault(),defaul);
		doubleProperty._default("hello");
		assertEquals(doubleProperty.getDefault(),defaul);

		doubleProperty.example(defaul);
		assertEquals(doubleProperty.getExample(), "2.3");
		
		List<Double>_enum=Arrays.asList(1.0);
		doubleProperty.setEnum(_enum);
		assertEquals(doubleProperty.getEnum(), _enum);

		String vendorName = "x-vendor";
		String value = "value";
		doubleProperty.vendorExtension(vendorName, value);
		assertEquals(doubleProperty.getVendorExtensions().get(vendorName), value);
	}
}
