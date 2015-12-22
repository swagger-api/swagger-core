package io.swagger.models.properties;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class UUIDPropertyTest {
  @Test
  public void testGettersAndSetters() {
	  UUIDProperty property=new UUIDProperty();
	  String vendorName = "x-vendor";
		String value = "value";
		property.vendorExtension(vendorName, value);
		assertEquals(property.getVendorExtensions().get(vendorName), value);
		List<String>_enum=Arrays.asList("_enum");
		property.setEnum(_enum);
		assertEquals(property.getEnum(), _enum);
  }
}
