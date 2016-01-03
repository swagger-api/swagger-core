package io.swagger.models.properties;

import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

public class FilePropertyTest {

  @Test
  public void testIsType() {
    //when
    boolean isType = FileProperty.isType("file", "format");

    //then
    assertTrue(isType, "isType must return true for file and format");
  }
}
