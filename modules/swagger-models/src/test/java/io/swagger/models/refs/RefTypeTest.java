package io.swagger.models.refs;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RefTypeTest {
  @Test
  public void testValues() {
	  Assert.assertEquals(RefType.valueOf("DEFINITION"), RefType.DEFINITION);
	  Assert.assertTrue(Arrays.asList(RefType.values()).contains(RefType.DEFINITION));
  }
}
