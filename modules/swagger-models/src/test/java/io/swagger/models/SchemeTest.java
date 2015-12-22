package io.swagger.models;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SchemeTest {

  @Test
  public void testForValue() {
    Assert.assertEquals(Scheme.forValue("http"), Scheme.HTTP);
    Assert.assertNull(Scheme.forValue("unknown"));
  }

  @Test
  public void testToValue() {
	  Assert.assertEquals(Scheme.HTTP.toValue(),"http");
  }
  
  @Test
  public void testValueOf(){
	  Assert.assertEquals(Scheme.valueOf("HTTP"), Scheme.HTTP);
  }
}
