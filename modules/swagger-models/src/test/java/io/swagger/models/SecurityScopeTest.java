package io.swagger.models;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class SecurityScopeTest {

	@Test
	public void testSecurityScope() {
		//given
		String name = "name";
		String description = "description";
		
		//when
		SecurityScope securityScope=new SecurityScope(name, description);
		
		//then
		assertEquals(securityScope.getName(), name,"The get name must equal the set one");
		assertEquals(securityScope.getDescription(), description,"The get description must equal the set one");
	}
}
