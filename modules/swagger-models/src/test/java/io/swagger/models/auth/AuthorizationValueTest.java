package io.swagger.models.auth;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class AuthorizationValueTest {

  @Test
  public void testAuthorizationValue() {
	  String keyName="keyName",  value="value",  type="type";
	  AuthorizationValue  authorizationValue=new AuthorizationValue(keyName, value, type);
	  assertEquals(authorizationValue.getKeyName(), keyName);
	  assertEquals(authorizationValue.getValue(), value);
	  assertEquals(authorizationValue.getType(), type);
  }
}
