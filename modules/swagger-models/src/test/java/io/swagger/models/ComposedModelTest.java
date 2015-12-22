package io.swagger.models;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.swagger.TestUtils;



public class ComposedModelTest {

	private ComposedModel instance;
	@BeforeMethod
	public void setUp() throws Exception {
		instance=new ComposedModel();		
	}




	
	@Test
	public void testGetProperties() {
		instance.setProperties(null);
		assertNull(instance.getProperties());
	}

	@Test
	public void testClone(){
		instance.setDescription("description");
		ComposedModel cloned=(ComposedModel) instance.clone();
		assertEquals(cloned.getDescription(), instance.getDescription());
	}
}
