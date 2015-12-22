package io.swagger.models.parameters;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BodyParameterTest {

  @Test
  public void testExample() {
    BodyParameter bodyParameter=new BodyParameter();
    String mediaType="mediaType",  value="value";
    bodyParameter.example(mediaType, value);
    Assert.assertEquals(bodyParameter.getExamples().get(mediaType), value);
  }
}
