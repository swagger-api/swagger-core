package io.swagger.models;

import static org.testng.Assert.*;

import org.powermock.reflect.Whitebox;
import org.testng.annotations.Test;

public class SecurityRequirementTest {
  @Test
  public void testGettersAndSetters() {
	  String name="name";
	  SecurityRequirement securityRequirement=new SecurityRequirement(name);
	  String scope="scope";
	  securityRequirement.scope(scope);
	  assertTrue(securityRequirement.getScopes().contains(scope));
	  
	  securityRequirement=new SecurityRequirement();
	  Whitebox.setInternalState(securityRequirement, "requirements",(Object) null);
	  securityRequirement.requirement(name);
	  assertTrue(securityRequirement.getRequirements().get(name).isEmpty());
	  
	  securityRequirement.setRequirements("other", null);
	  assertNull(securityRequirement.getRequirements().get("other"));
  }
}
