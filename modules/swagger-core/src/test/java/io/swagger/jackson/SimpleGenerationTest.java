package io.swagger.jackson;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;
import static org.testng.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class SimpleGenerationTest extends SwaggerTestBase {
    private final ModelResolver modelResolver = new ModelResolver(new ObjectMapper());
    private final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

    @Test
    public void testSimple() throws Exception {
        final Model model = context.resolve(SimpleBean.class);
        assertNotNull(model);
        assertEquals(model.getDescription(), "DESC");

        final Map<String, Property> props = model.getProperties();
        assertEquals(props.size(), 6);

        for (Map.Entry<String, Property> entry : props.entrySet()) {
            final String name = entry.getKey();
            final Property prop = entry.getValue();

            if ("a".equals(name)) {
                assertEquals(prop.getType(), "string");
            } else if ("b".equals(name)) {
                assertEquals(prop.getType(), "integer");
                assertEquals(prop.getFormat(), "int32");
            } else if ("c".equals(name)) {
                assertEquals(prop.getType(), "integer");
                assertEquals(prop.getFormat(), "int64");
            } else if ("d".equals(name)) {
                assertEquals(prop.getType(), "number");
                assertEquals(prop.getFormat(), "float");
            } else if ("e".equals(name)) {
                assertEquals(prop.getType(), "number");
                assertEquals(prop.getFormat(), "double");
            } else if ("f".equals(name)) {
                assertEquals(prop.getType(), "string");
                assertEquals(prop.getFormat(), "date-time");
            } else {
                fail(String.format("Unknown property '%s'", name));
            }
        }
    }

    @Test
    public void testOrdering() throws Exception {
        final Model jsonOrderBean = context.resolve(JsonOrderBean.class);
        final Map<String, Property> props = jsonOrderBean.getProperties();
        assertEquals(new ArrayList<String>(props.keySet()), Arrays.asList("a", "b", "c", "d"));

        final Model positionBean = context.resolve(PositionBean.class);
        final Map<String, Property> positionBeanProps = positionBean.getProperties();
        assertEquals(positionBeanProps.size(), 4);

        final Property prop = positionBeanProps.get("c");
        assertNotNull(prop);
        assertEquals((int)prop.getPosition(), 3);
    }

    @Test
    public void testTheCountBean() throws Exception {
        final Model model = context.resolve(TheCount.class);
        final Map<String, Property> props = model.getProperties();
        assertEquals(props.size(), 1);

        final Property prop = props.values().iterator().next();
        assertEquals(prop.getName(), "theCount");
    }

    @Test
    public void testStringDateMap() throws Exception {
        final Model model = context.resolve(StringDateMapBean.class);
        final Map<String, Property> props = model.getProperties();
        assertEquals(props.size(), 1);

        final Property prop = props.values().iterator().next();
        assertEquals(prop.getName(), "stuff");
    }

    @Test
    public void testIntArray() throws Exception {
        final Model model = context.resolve(IntArrayBean.class);
        final Map<String, Property> props = model.getProperties();
        assertEquals(props.size(), 1);

        final Property prop = props.values().iterator().next();
        assertEquals(prop.getName(), "b");
        assertEquals(prop.getType(), "array");
    }

  /*
  /**********************************************************
  /* Test methods
  /**********************************************************
   */

    @JsonPropertyOrder({"a", "b"})
    @ApiModel(description = "DESC")
    static class SimpleBean {
        public int b;
        public long c;
        public float d;
        public double e;
        public java.util.Date f;

        public String getA() {
            return null;
        }
    }

    @JsonPropertyOrder({"a", "b", "c", "d"})
    static class JsonOrderBean {
        public int d;
        public int a;
        public int c;
        public int b;
    }

    static class PositionBean {
        @ApiModelProperty(position = 4)
        public int d;

        @ApiModelProperty(position = 1)
        public int a;

        @ApiModelProperty(position = 3)
        public int c;

        @ApiModelProperty(position = 2)
        public int b;
    }

    static class TheCount {
        @JsonProperty("theCount")
        private Integer count;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }

    static class StringDateMapBean {
        public Map<String, Date> stuff;
    }

    @JsonPropertyOrder({"a", "b"})
    static class IntArrayBean {
        public int[] b;
    }
}
