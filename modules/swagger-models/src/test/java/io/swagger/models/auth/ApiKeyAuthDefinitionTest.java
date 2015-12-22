package io.swagger.models.auth;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiKeyAuthDefinitionTest {

  @Test
  public void testApiKeyAuthDefinition() {
    String name="name";
    ApiKeyAuthDefinition apiKeyAuthDefinition=new ApiKeyAuthDefinition(name,In.HEADER);
    
    Assert.assertEquals(apiKeyAuthDefinition.getName(), name);
    Assert.assertEquals(apiKeyAuthDefinition.getIn(), In.HEADER);
  }
}
