package io.swagger.models;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;


public class ArrayModelTest extends PowerMockTestCase {

	private ArrayModel instance;

	@BeforeMethod
	public void setUp() throws Exception {
		instance = new ArrayModel();
	}

	@Test
	public void testHashCode() {
		assertEquals(1835900728, instance.hashCode());
	}

	@Test
	public void testClone() {
		instance.setProperties(new HashMap<String, Property>());
		instance.setType("type");
		instance.setDescription("description");
		instance.setExample(new Object());
		ArrayModel cloned = (ArrayModel) instance.clone();
		assertEquals(instance.getProperties(), cloned.getProperties());
		assertEquals(instance.getType(), cloned.getType());
		assertEquals(instance.getDescription(), cloned.getDescription());
		assertEquals(instance.getExample(), cloned.getExample());
	}

	@Test
	public void testEqualsObject() throws Exception {
		assertTrue(instance.equals(instance));
		assertFalse(instance.equals(null));
		assertFalse(instance.equals(new Object()));
//		Method methods[] = PowerMockito.methods(ArrayModel.class, new String[] { "equals" });
//		for (Method method : methods) {
//			if (method.getDeclaringClass() == ArrayModel.class)
//				PowerMockito.stub(method).toReturn(true);
//		}
//
//		setUp();
//		assertFalse(instance.equals(new Object()));
		ArrayModel other = new ArrayModel();
		String description = "description";
		other.setDescription(description);
		assertFalse(instance.equals(other));
		assertFalse(other.equals(instance));
		instance.setDescription(description);
		Object example = new Object();
		other.setExample(example);
		assertFalse(instance.equals(other));
		assertFalse(other.equals(instance));
		instance.setExample(example);
		Property items = new ArrayProperty();
		other.setItems(items);
		assertFalse(instance.equals(other));
		assertFalse(other.equals(instance));
		instance.setItems(items);
		Map<String, Property> properties = new HashMap<String, Property>();
		other.setProperties(properties);
		assertFalse(instance.equals(other));
		assertFalse(other.equals(instance));
		instance.setProperties(properties);
		String type = "type";
		other.setType(type);
		instance.setType(null);
		assertFalse(instance.equals(other));
		assertFalse(other.equals(instance));
		instance.setType(type);
		assertTrue(instance.equals(other));
	}

	@Test
	public void testDescription() {
		String description = "description";
		assertEquals(description, instance.description(description).getDescription());
	}

	@Test
	public void testItems() {
		Property items = new ArrayProperty();
		assertEquals(items, instance.items(items).getItems());
	}

}
