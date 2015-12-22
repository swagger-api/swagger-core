package io.swagger.models;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import io.swagger.models.refs.RefFormat;

public class RefPathTest {

  @Test
  public void testGettersAndSetters() {
    RefPath refPath=new RefPath("ref");
    assertEquals(refPath.get$ref(), "#/paths/ref");
    assertEquals(refPath.getRefFormat(), RefFormat.INTERNAL);
  }
}
