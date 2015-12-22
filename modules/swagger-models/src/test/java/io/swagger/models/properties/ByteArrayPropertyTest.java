package io.swagger.models.properties;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ByteArrayPropertyTest {

  @Test
  public void testVendorExtension() {
    ByteArrayProperty byteArrayProperty=new ByteArrayProperty();
    String vendorName = "x-vendor";
	String value = "value";
	byteArrayProperty.vendorExtension(vendorName, value);
	assertEquals(byteArrayProperty.getVendorExtensions().get(vendorName), value);
  }
}
