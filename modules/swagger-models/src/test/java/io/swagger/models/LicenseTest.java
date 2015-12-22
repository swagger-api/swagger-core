package io.swagger.models;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LicenseTest {

	private License instance;

	@BeforeMethod
	public void setUp() throws Exception {
		instance=new License();
		
		
	}

	@Test
	public void testSetVendorExtension() {
		instance.setVendorExtension("x-name", "value");
		assertEquals(instance.getVendorExtensions().get("x-name"), "value");
	}
	


}
