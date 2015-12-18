package io.swagger.models;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ExternalDocsTest {

	private ExternalDocs instance;

	@BeforeMethod
	public void setUp() throws Exception {
		instance=new ExternalDocs("description", "url");
	}

	

	
	@Test
	public void testSetVendorExtension() {
		String name="x-vendor";
		String value="value";
		instance.setVendorExtension(name, value);
		assertEquals(instance.getVendorExtensions().get(name), value);
	}

	


}
