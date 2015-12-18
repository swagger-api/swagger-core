package io.swagger.models;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import io.swagger.models.refs.RefFormat;

public class RefResponseTest {

  @Test
  public void testGettersAndSetters() {
    RefResponse RefResponse=new RefResponse("ref");
    assertEquals(RefResponse.get$ref(), "#/responses/ref");
    assertEquals(RefResponse.getRefFormat(), RefFormat.INTERNAL);
  }
}
