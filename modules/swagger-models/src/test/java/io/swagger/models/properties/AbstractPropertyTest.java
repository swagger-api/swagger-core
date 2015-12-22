package io.swagger.models.properties;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.HashMap;

import io.swagger.models.Xml;

public class AbstractPropertyTest {
	@Test
	public void testGettersAndSetters() {
		String name = "name";
		String type = "type";
		String format = "format";
		String example = "example";
		Xml xml = new Xml();
		boolean required = true;
		Integer position = 3;
		String description = "description";
		String title = "title";
		Boolean readOnly = true;
		String access = "String access";
		
		AbstractProperty instance=new AbstractPropertyTestImpl();
		instance.setName(name);
		instance.setType(type);
		instance.setFormat(format);
		instance.setExample(example);
		instance.setXml(xml);
		instance.setRequired(required);
		instance.setPosition(position);
		instance.setDescription(description);
		instance.setTitle(title);
		instance.readOnly();
		instance.setAccess(access);
		
		assertEquals(instance.getName(), name);
		assertEquals(instance.getType(), type);
		assertEquals(instance.getFormat(), format);
		assertEquals(instance.getExample(), example);
		assertEquals(instance.getXml(), xml);
		assertEquals(instance.getRequired(), required);
		assertEquals(instance.getPosition(), position);
		assertEquals(instance.getDescription(), description);
		assertEquals(instance.getTitle(), title);
		assertEquals(instance.getReadOnly(), readOnly);
		assertEquals(instance.getAccess(), access);
		
		instance.setReadOnly(false);
		assertNull(instance.getReadOnly());
		
		instance.setDefault("default");
		String vendorName = "x-vendor";
		String value = "value";
		instance.setVendorExtension(vendorName, value);
		assertEquals(instance.getVendorExtensions().get(vendorName), value);
		
		instance.setVendorExtensionMap(new HashMap<String, Object>());
		assertEquals(instance.getVendorExtensions().get(vendorName), value);

	}
}
