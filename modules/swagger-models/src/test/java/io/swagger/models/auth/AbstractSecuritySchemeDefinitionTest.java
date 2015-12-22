package io.swagger.models.auth;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class AbstractSecuritySchemeDefinitionTest {

  @Test
  public void testGettersAndSetters() {
    AbstractSecuritySchemeDefinition instance=new AbstractSecuritySchemeDefinitionTestImpl();
    String vendorName="x-vendor";
	String value="value";
	
	instance.setVendorExtension(vendorName, value);
	assertEquals(instance.getVendorExtensions().get(vendorName), value);
	
	String description="description";
	instance.setDescription(description);
	assertEquals(instance.getDescription(), description);
  }
}
