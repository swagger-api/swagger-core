package com.wordnik.swagger.jackson;

import java.util.*;

import com.wordnik.swagger.jackson.*;
import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.xml.bind.annotation.*;

public class XMLInfoTest extends SwaggerTestBase {
  @XmlRootElement(name="xmlDecoratedBean")
  @ApiModel(description="DESC")
  static class XmlDecoratedBean {
    @XmlElement(name="elementB")
    public int b;

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    public List<String> items;

    @JsonProperty("elementC")
    public String c;
  }

  public void testSimple() throws Exception {
    final ModelResolver mr = modelResolver();
    Model model = mr.resolve(XmlDecoratedBean.class,new ModelConverterContextMock());
    assertTrue(model instanceof ModelImpl);

    ModelImpl impl = (ModelImpl) model;

    Xml xml = impl.getXml();
    assertNotNull(xml);
    assertEquals(xml.getName(), "xmlDecoratedBean");

    Property property = impl.getProperties().get("items");
    assertNotNull(property);
    xml = property.getXml();

    assertNotNull(xml);
    assertEquals(xml.getName(), "item");
    assertTrue(xml.getWrapped());

    property = impl.getProperties().get("elementC");
    assertNotNull(property);
  }
}
