package io.swagger.models.properties;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class AbstractNumericPropertyTest {
  @Test
  public void testGettersAndSetters() {
	  Double minimum=2.2, maximum=6.4;
	  Boolean exclusiveMinimum=true, exclusiveMaximum=true;
	  AbstractNumericProperty abstractNumericProperty=new AbstractNumericPropertyTestImpl();
	  abstractNumericProperty.setMinimum(minimum);
	  abstractNumericProperty.setMaximum(maximum);
	  abstractNumericProperty.setExclusiveMaximum(exclusiveMaximum);
	  abstractNumericProperty.setExclusiveMinimum(exclusiveMinimum);
	  
	  assertEquals(abstractNumericProperty.getMinimum(), minimum);
	  assertEquals(abstractNumericProperty.getMaximum(), maximum);
	  assertEquals(abstractNumericProperty.getExclusiveMaximum(), exclusiveMaximum);
	  assertEquals(abstractNumericProperty.getExclusiveMinimum(), exclusiveMinimum);
  }
}
