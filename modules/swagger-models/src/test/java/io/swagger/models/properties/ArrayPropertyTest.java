package io.swagger.models.properties;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class ArrayPropertyTest {

	@Test
	public void testUniqueItems() {
		//given
		Property items = new BooleanProperty();
		ArrayProperty arrayProperty = new ArrayProperty(items);

		//when
		arrayProperty.uniqueItems();

		//then
		assertTrue(arrayProperty.getUniqueItems(),"UniqueItems must be true after calling uniqueItems()");
	}
}
