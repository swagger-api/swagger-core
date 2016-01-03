package io.swagger.models.properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

import io.swagger.models.properties.StringProperty.Format;

public class StringPropertyTest {

	@Test
	public void testFromName() {
		// when
		Format format = StringProperty.Format.fromName("byte");

		// then
		assertEquals(format, StringProperty.Format.BYTE, "The format from name 'byte' is BYTE");

		// when
		format = StringProperty.Format.fromName("unknown");

		// then
		assertNull(format, "The format for unknown names must be null");
	}

	@Test
	public void testValueOf() {
		// when
		Format value = StringProperty.Format.valueOf("BYTE");

		// then
		assertEquals(value, StringProperty.Format.BYTE, "The value for 'BYTE' is BYTE");
	}
}
