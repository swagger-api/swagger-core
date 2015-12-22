package io.swagger.models.parameters;

import org.testng.Assert;
import org.testng.annotations.Test;

public class QueryParameterTest {

  @Test
  public void getDefaultCollectionFormat() {
    Assert.assertEquals(new QueryParameter().getDefaultCollectionFormat(), "multi");
  }
}
