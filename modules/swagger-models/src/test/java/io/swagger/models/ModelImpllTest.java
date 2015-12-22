package io.swagger.models;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.swagger.TestUtils;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;


@PrepareForTest({})
public class ModelImpllTest extends PowerMockTestCase{

	private ModelImpl instance;
	Object[] propertiesAndValues;
	@BeforeMethod
	public void setUp() throws Exception {
		instance=new ModelImpl();
		propertiesAndValues=new Object[] {
				"additionalProperties",new ArrayProperty(),
				"description","description",
				"discriminator","discriminator",
				"example",new Object(),
				"format","format",
				"isSimple",true,
				"name","name",
				"properties",new HashMap<String, Property>(),
				"required",new ArrayList<String>(),
				"type","type",
				"xml",new Xml(),
				"defaultValue","defaultValue",
		};
	}



	@Test
	public void testClone() {
		//eliminate format
		propertiesAndValues=new Object[] {
				"additionalProperties",new ArrayProperty(),
				"description","description",
				"discriminator","discriminator",
				"example",new Object(),
				"isSimple",true,
				"name","name",
				"properties",new HashMap<String, Property>(),
				"required",new ArrayList<String>(),
				"type","type",
				"xml",new Xml(),
				"defaultValue","defaultValue",
		};
		TestUtils.testClone(instance, propertiesAndValues);
	}


	


	
	@Test
	public void testGetProperties() {
		assertNull(instance.getProperties());
	}
	@Test
	public void testEnum(){
		List<String> _enum=new ArrayList<String>();
		assertEquals(instance._enum(_enum).getEnum(), _enum);
		instance.setEnum(null);
		String value="value";
		assertTrue(instance._enum(value).getEnum().contains(value));
		
	}
	@Test
	public void testGettersAndSetters(){
		String key="key";
		Property property=new ArrayProperty();
		assertEquals(instance.property(key, property).getProperties().get(key), property);
		
		assertTrue(instance.required(key).getRequired().contains(key));
		
		assertNull(instance.getDiscriminator());
		assertNull(instance.getDescription());
		assertFalse(instance.isSimple());
		assertNull(instance.getAdditionalProperties());
		
		instance.setRequired(Arrays.asList(key));
		assertTrue(instance.getRequired().contains(key));
		
		assertNull(instance.getExample());
		assertNull(instance.getDefaultValue());
		assertNull(instance.getXml());
		
		String badKey="badKey";
		instance.addProperty(badKey, null);
		assertNull(instance.getProperties().get(badKey));
		
		instance.setRequired(Arrays.asList(key));
		instance.addProperty(key, property);
		assertEquals(instance.getProperties().get(key), property);
		
	}
	

}
