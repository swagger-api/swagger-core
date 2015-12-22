package io.swagger.models.properties;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class RefPropertyTest {
  @Test
  public void testGettersAndSetters() {
	  RefProperty refProperty=new RefProperty();
	  assertNull(refProperty.getRefFormat());
	  assertNull(refProperty.getSimpleRef());
	  
	  refProperty.asDefault("ref");
	  assertEquals(refProperty.getSimpleRef(), "ref");
  }
}
