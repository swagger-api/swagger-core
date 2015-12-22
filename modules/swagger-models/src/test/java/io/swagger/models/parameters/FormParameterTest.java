package io.swagger.models.parameters;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FormParameterTest {

  @Test
  public void getDefaultCollectionFormat() {
    Assert.assertEquals(new FormParameter().getDefaultCollectionFormat(), "multi");
  }
}
