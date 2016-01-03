package io.swagger.models;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class LicenseTest {

	@Test
	public void testSetVendorExtension() {
		//given
		License instance = new License();

		// when
		instance.setVendorExtension("x-name", "value");

		// then
		assertEquals(instance.getVendorExtensions().get("x-name"), "value",
				"Must be able to retrieve the same value from the map");
	}
}
