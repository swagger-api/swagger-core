package com.wordnik.swagger.jackson;

import com.wordnik.swagger.jackson.*;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.annotations.ApiModelProperty;

import org.joda.time.DateTime;

import java.util.Map;


public class JodaTest extends SwaggerTestBase {
  static class ModelWithJodaDateTime {
    @ApiModelProperty(value="Name!", position = 2)
    public String name;

    @ApiModelProperty(value = "creation timestamp", required = true, position = 1)
    public DateTime createdAt;
  }

  public void testSimple() throws Exception {
    final ModelResolver mr = modelResolver();
    Model model = mr.resolve(ModelWithJodaDateTime.class,new ModelConverterContextMock());
    assertNotNull(model);

    Map<String,Property> props = model.getProperties();
    assertEquals(2, props.size());

    for (Map.Entry<String,Property> entry : props.entrySet()) {
      String name = entry.getKey();
      Property prop = entry.getValue();

      if ("name".equals(name)) {
        assertEquals("string", prop.getType());
      } else if ("createdAt".equals(name)) {
        assertEquals("string", prop.getType());
        assertEquals("date-time", prop.getFormat());
      } else {
        fail("Unknown property '"+name+"'");
      }
    }
  }
}
