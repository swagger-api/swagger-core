package io.swagger.models.properties;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FilePropertyTest {

  @Test
  public void testIsType() {
    Assert.assertTrue(FileProperty.isType("file", "format"));
  }
}
