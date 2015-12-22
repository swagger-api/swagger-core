package io.swagger.models.properties;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class LongPropertyTest {
	@Test
	public void testGettersAndSetters() {
		LongProperty longProperty = new LongProperty();
		long defaul=6L;

		longProperty._default("6");
		assertEquals(longProperty.getDefault(),(Long)defaul);
		longProperty._default("hello");
		assertEquals(longProperty.getDefault(),(Long)defaul);

		longProperty.example(defaul);
		assertEquals(longProperty.getExample(), "6");
		
		List<Long>_enum=Arrays.asList(1L);
		longProperty.setEnum(_enum);
		assertEquals(longProperty.getEnum(), _enum);

		String vendorName = "x-vendor";
		String value = "value";
		longProperty.vendorExtension(vendorName, value);
		assertEquals(longProperty.getVendorExtensions().get(vendorName), value);
	}
}
