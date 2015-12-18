package io.swagger.models;

import static org.testng.Assert.assertEquals;

import org.powermock.reflect.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InfoTest {

	private Info instance;

	@BeforeMethod
	public void setUp() throws Exception {
		instance=new Info();

	}

	

	
	@Test
	public void testSetVendorExtension() {
		String name="x-vendor";
		String value="value";
		instance.setVendorExtension(name, value);
		assertEquals(instance.getVendorExtensions().get(name), value);
	}


	@Test
	public void testMergeWith(){
		Info info=new Info();
		info.setDescription("description");
		Whitebox.setInternalState(instance, "vendorExtensions", (Object)null);
		instance.mergeWith(info);
		assertEquals(info.getDescription(), instance.getDescription());
	}
}
