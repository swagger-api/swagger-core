package io.swagger.models.auth;

import static  org.testng.Assert.*;
import org.testng.annotations.Test;

public class OAuth2DefinitionTest {
  @Test
  public void testGettersAndSetters() {
	  OAuth2Definition auth2Definition=new OAuth2Definition();
	  String authorizationUrl= "authorizationUrl";
	  auth2Definition.implicit(authorizationUrl);
	  assertEquals(auth2Definition.getAuthorizationUrl(), authorizationUrl);
	  assertEquals(auth2Definition.getFlow(),"implicit");
	  
	  String tokenUrl="tokenUrl";
	  auth2Definition.password(tokenUrl);
	  assertEquals(auth2Definition.getTokenUrl(), tokenUrl);
	  assertEquals(auth2Definition.getFlow(), "password");
	  
	  auth2Definition.application(tokenUrl);
	  assertEquals(auth2Definition.getTokenUrl(), tokenUrl);
	  assertEquals(auth2Definition.getFlow(), "application");
	  
	  auth2Definition.accessCode(authorizationUrl, tokenUrl);
	  assertEquals(auth2Definition.getTokenUrl(), tokenUrl);
	  assertEquals(auth2Definition.getAuthorizationUrl(), authorizationUrl);
	  assertEquals(auth2Definition.getFlow(), "accessCode");
	  
	  String name="name",  description="description";
	  auth2Definition.scope(name, description);
	  assertEquals(auth2Definition.getScopes().get(name), description);
  }
}
