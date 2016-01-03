package io.swagger.models;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import io.swagger.models.refs.RefFormat;

public class RefResponseTest {

  @Test
  public void testConstructor() {
	//given
    RefResponse RefResponse=new RefResponse("ref");
    
    //then
    assertEquals(RefResponse.get$ref(), "#/responses/ref","The ref value must respect the format");
    assertEquals(RefResponse.getRefFormat(), RefFormat.INTERNAL,"The format must be INTERNAL");
  }
}
