package io.swagger.models.parameters;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class FormParameterTest {

  @Test
  public void getDefaultCollectionFormat() {
    //when
    String collectionFormat=  new FormParameter().getDefaultCollectionFormat();

    //then
    assertEquals(collectionFormat, "multi","The default format must be multi");
  }
}
