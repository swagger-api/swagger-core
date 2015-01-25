package com.wordnik.swagger.jackson;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.converter.*;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.ModelImpl;
import com.wordnik.swagger.models.Xml;
import com.wordnik.swagger.models.properties.Property;

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
    final ModelConverter mr = modelResolver();
    Model model = mr.resolve(XmlDecoratedBean.class, new ModelConverterContextImpl(mr), null);
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
