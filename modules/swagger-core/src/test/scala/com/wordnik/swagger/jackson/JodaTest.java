package com.wordnik.swagger.jackson;

import java.util.Map;

import org.joda.time.DateTime;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.converter.*;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;


public class JodaTest extends SwaggerTestBase {
  static class ModelWithJodaDateTime {
    @ApiModelProperty(value="Name!", position = 2)
    public String name;

    @ApiModelProperty(value = "creation timestamp", required = true, position = 1)
    public DateTime createdAt;
  }

  public void testSimple() throws Exception {
    final ModelConverter mr = modelResolver();
    Model model = mr.resolve(ModelWithJodaDateTime.class, new ModelConverterContextImpl(mr), null);
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
