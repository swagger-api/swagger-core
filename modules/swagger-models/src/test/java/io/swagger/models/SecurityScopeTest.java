package io.swagger.models;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SecurityScopeTest {

	@Test
	public void testSecurityScope() {
		String name = "name";
		String description = "description";
		SecurityScope securityScope=new SecurityScope(name, description);
		Assert.assertEquals(securityScope.getName(), name);
		Assert.assertEquals(securityScope.getDescription(), description);
	}
}
