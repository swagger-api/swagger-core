package io.swagger.models.refs;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RefFormatTest {
  @Test
  public void testValues() {
	  Assert.assertEquals(RefFormat.valueOf("INTERNAL"), RefFormat.INTERNAL);
	  Assert.assertTrue(Arrays.asList(RefFormat.values()).contains(RefFormat.INTERNAL));
  }
}
