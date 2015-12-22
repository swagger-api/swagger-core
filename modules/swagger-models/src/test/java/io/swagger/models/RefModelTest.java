package io.swagger.models;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class RefModelTest {

  @Test
  public void testClone() {
	  RefModel refModel=new RefModel();
	  refModel.asDefault("ref");
	  assertEquals(refModel.get$ref(), "#/definitions/ref");
	  assertEquals(refModel.getSimpleRef(), "ref");
	  refModel.setReference("reference");
	  RefModel cloned=(RefModel) refModel.clone();
	  assertEquals(cloned.getReference(), refModel.getReference());
	  
	  assertNull(refModel.getVendorExtensions());
	  
	  
	  
	  
  }
}
