package io.swagger.models;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import io.swagger.models.refs.RefFormat;

public class RefPathTest {

  @Test
  public void testConstructor() {
	//given
    RefPath refPath=new RefPath("ref");
    
    //then
    assertEquals(refPath.get$ref(), "#/paths/ref","The ref value must respect the format");
    assertEquals(refPath.getRefFormat(), RefFormat.INTERNAL,"The format must be INTERNAL");
  }
}
