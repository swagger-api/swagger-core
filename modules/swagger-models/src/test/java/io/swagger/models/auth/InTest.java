package io.swagger.models.auth;

import java.util.Arrays;
import java.util.HashMap;

import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InTest {

  @Test
  public void testForValue() {
    Assert.assertEquals(In.forValue("header"), In.HEADER);
    Assert.assertNull(In.forValue("unknown"));
  }

  @Test
  public void testToValue() {
	  Assert.assertEquals(In.HEADER.toValue(),"header");
	  Whitebox.setInternalState(In.class, "names", new HashMap<String, In>());
	  Assert.assertNull(In.HEADER.toValue());
  }
  
  @Test
  public void testValues(){
	  Assert.assertEquals(In.valueOf("HEADER"), In.HEADER);
	  Assert.assertTrue(Arrays.asList(In.values()).contains(In.HEADER));
  }
}
