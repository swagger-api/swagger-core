package io.swagger.models.parameters;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class QueryParameterTest {

  @Test
  public void getDefaultCollectionFormat() {
    //when
    String collectionFormat = new QueryParameter().getDefaultCollectionFormat();

    //then
    assertEquals(collectionFormat, "multi","The default format must be 'multi'");
  }
}
