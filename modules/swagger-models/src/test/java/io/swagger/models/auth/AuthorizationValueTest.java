package io.swagger.models.auth;

import static  org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class AuthorizationValueTest {

  @Test
  public void testConstructor() {
	  //given
	  final String keyName="keyName", value="value", type="type";
	  
	  //when
	  AuthorizationValue  authorizationValue = new AuthorizationValue(keyName, value, type);
	  
	  //then
	  assertEquals(authorizationValue.getKeyName(), keyName,"The getKeyName must be the same as the one passed to the constructor");
	  assertEquals(authorizationValue.getValue(), value,"The getValue must be the same as the one passed to the constructor");
	  assertEquals(authorizationValue.getType(), type,"The getType must be the same as the one passed to the constructor");
  }
}
