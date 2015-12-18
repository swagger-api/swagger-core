package io.swagger.models;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.powermock.reflect.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.swagger.models.properties.Property;

public class AbstractModelTest extends AbstractModel {

	private AbstractModelTest instance;

	// @ObjectFactory
	// public IObjectFactory getObjectFactory() {
	// return new org.powermock.modules.testng.PowerMockObjectFactory();
	// }

	@BeforeMethod
	public void setUp() throws Exception {
		instance=new AbstractModelTest();
	}

	@Test
	public void testHashCode() {
		assertEquals(961, instance.hashCode());
	}

	@Test
	public void testGetExternalDocs() {
		ExternalDocs value = new ExternalDocs();
		instance.setExternalDocs(value);
		assertEquals(value, instance.getExternalDocs());
	}

	@Test
	public void testGetTitle() {
		String title = "title";
		instance.setTitle(title);
		assertEquals(title, instance.getTitle());
	}

	@Test
	public void testGetVendorExtensions() {
		String name = "x-name";
		Object value = "value";
		instance.setVendorExtension(name, value);
		assertEquals(value, instance.getVendorExtensions().get(name));
	}

	@Test
	public void testCloneTo() {
		AbstractModelTest clone = new AbstractModelTest();
		ExternalDocs externalDocs = new ExternalDocs();
		instance.setExternalDocs(externalDocs);
		instance.cloneTo(clone);
		assertEquals(externalDocs, clone.getExternalDocs());
	}

	@Test
	public void testClone() {
		assertNull(clone());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(instance.equals(instance));
		assertFalse(instance.equals(null));
		assertFalse(instance.equals(new Object()));
		AbstractModel other = new AbstractModelTest();
		ExternalDocs externalDocs = new ExternalDocs();
		other.setExternalDocs(externalDocs);
		assertFalse(instance.equals(other));
		instance.setExternalDocs(externalDocs);
		other.setExternalDocs(null);
		assertFalse(instance.equals(other));
		other.setExternalDocs(externalDocs);
		Map<String, Object> vendorExtensions = new HashMap<String, Object>();
		Whitebox.setInternalState(instance, "vendorExtensions", (Map<String, Object>) null);
		assertFalse(instance.equals(other));
		Whitebox.setInternalState(instance, "vendorExtensions", vendorExtensions);
		Whitebox.setInternalState(other, "vendorExtensions",(Map<String, Object>) null);
		assertFalse(instance.equals(other));
		Whitebox.setInternalState(other, "vendorExtensions", vendorExtensions);
		assertTrue(instance.equals(other));

	}

	@Test
	public void testGetReference() {
		String reference = "reference";
		setReference(reference);
		assertEquals(reference, getReference());
	}

	@Override
	public String getDescription() {

		return null;
	}

	@Override
	public void setDescription(String description) {

	}

	@Override
	public Map<String, Property> getProperties() {

		return null;
	}

	@Override
	public void setProperties(Map<String, Property> properties) {

	}

	@Override
	public Object getExample() {

		return null;
	}

	@Override
	public void setExample(Object example) {

	}

}
