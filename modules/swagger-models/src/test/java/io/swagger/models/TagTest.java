package io.swagger.models;

import static org.testng.Assert.*;

import java.util.HashMap;

import org.testng.annotations.Test;

public class TagTest {

  @Test
  public void testToString() {
    Tag tag=new Tag();
    
    String vendorName="x-vendor";
	String value="value";
	
	tag.setVendorExtension(vendorName, value);
	assertEquals(tag.getVendorExtensions().get(vendorName), value);
	
	assertTrue(tag.toString().contains("extensions:{x-vendor=value}}"));
  }
}
