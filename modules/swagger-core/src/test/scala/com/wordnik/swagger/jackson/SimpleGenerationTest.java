package com.wordnik.swagger.jackson;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.converter.ModelConverterContextImpl;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;

public class SimpleGenerationTest extends SwaggerTestBase {
//  ObjectMapper mapper = new ObjectMapper();

  @JsonPropertyOrder({ "a", "b" })
  @ApiModel(description="DESC")
  static class SimpleBean {
   public int b;
   public long c;
   public float d;
   public double e;
   public java.util.Date f;
   public String getA() { return null; }
  }

  @JsonPropertyOrder({ "a", "b", "c", "d" })
  static class JsonOrderBean {
    public int d;
    public int a;
    public int c;
    public int b;
  }

  static class PositionBean {
    @ApiModelProperty(position=4)
    public int d;

    @ApiModelProperty(position=1)
    public int a;

    @ApiModelProperty(position=3)
    public int c;

    @ApiModelProperty(position=2)
    public int b;
  }

  static class TheCount {
    @JsonProperty("theCount")
    private Integer count;

    public void setCount(Integer count) {
     this.count = count;
    }

    public Integer getCount() {
     return count;
    }
  }

  static class StringDateMapBean {
    public Map<String,Date> stuff;
  }

  @JsonPropertyOrder({ "a", "b" })
  static class IntArrayBean {
    public int[] b;
  }
  
  /*
  /**********************************************************
  /* Test methods
  /**********************************************************
   */

  public void testSimple() throws Exception  {
    ModelResolver modelResolver = modelResolver();
    ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
	
	Model model = context
      .resolve(SimpleBean.class);
    assertNotNull(model);

    assertEquals("DESC", model.getDescription());

    Map<String, Property> props = model.getProperties();
    assertEquals(6, props.size());

    for (Map.Entry<String, Property> entry : props.entrySet()) {
      String name = entry.getKey();
      Property prop = entry.getValue();

      if ("a".equals(name)) {
        assertEquals("string", prop.getType());
      } else if ("b".equals(name)) {
        assertEquals("integer", prop.getType());
        assertEquals("int32", prop.getFormat());
      } else if ("c".equals(name)) {
        assertEquals("integer", prop.getType());
        assertEquals("int64", prop.getFormat());
      } else if ("d".equals(name)) {
        assertEquals("number", prop.getType());
        assertEquals("float", prop.getFormat());
      } else if ("e".equals(name)) {
        assertEquals("number", prop.getType());
        assertEquals("double", prop.getFormat());
      } else if ("f".equals(name)) {
        assertEquals("string", prop.getType());
        assertEquals("date-time", prop.getFormat());
      } else {
        fail("Unknown property '"+name+"'");
      }
    }
  }

  @Ignore
  public void testOrdering() throws Exception {
    ModelResolver modelResolver = modelResolver();
    ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

	Model model = context
      .resolve(JsonOrderBean.class);

    Map<String, Property> props = model.getProperties();

    assertEquals(4, props.size());
    /*
    assertEquals("abcd", _concat(props.keySet()));

    model = modelResolver().resolve(PositionBean.class);

    props = model.getProperties();
    Property prop = props.get("c");
    assertNotNull(prop);
    // assertEquals(Integer.valueOf(3), prop.getPosition());
    assertEquals(4, props.size());
    assertEquals("abcd", _concat(props.keySet()));
    */
  }

  public void testTheCountBean() throws Exception {
	  
	  ModelResolver modelResolver = modelResolver();
	  ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

		
    Model model = context
      .resolve(TheCount.class);

    Map<String, Property> props = model.getProperties();
    assertEquals(1, props.size());
    Property prop = props.values().iterator().next();

    assertEquals("theCount", prop.getName());
  }

  public void testStringDateMap() throws Exception {	
    final ObjectMapper M = new ObjectMapper();
    ModelResolver modelResolver = new ModelResolver(M);
    ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
	Model model = context
      .resolve(StringDateMapBean.class);

	
    Map<String, Property> props = model.getProperties();
    assertEquals(1, props.size());
    Property prop = props.values().iterator().next();
    assertEquals("stuff", prop.getName());
  }

  public void testIntArray() throws Exception {
      ModelResolver modelResolver = new ModelResolver(new ObjectMapper());
      ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
	Model model = context
        .resolve(IntArrayBean.class);

      Map<String, Property> props = model.getProperties();
      assertEquals(1, props.size());
      Property prop = props.values().iterator().next();
      assertEquals("b", prop.getName());
      
      // !!! TODO
  }
  
  protected String _concat(Set<String> input) {
    StringBuilder sb = new StringBuilder();
    for (String str : input) {
     sb.append(str);
    }
    return sb.toString();
  }
}
