package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

public class SimpleGenerationTest extends SwaggerTestBase {
    private final ModelResolver modelResolver = new ModelResolver(new ObjectMapper());
    private final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

    @Test
    public void testSimple() throws Exception {
        final Schema model = context.resolve(new AnnotatedType(SimpleBean.class));
        assertNotNull(model);
        assertEquals(model.getDescription(), "DESC");

        final Map<String, Schema> props = model.getProperties();
        assertEquals(props.size(), 6);

        for (Map.Entry<String, Schema> entry : props.entrySet()) {
            final String name = entry.getKey();
            final Schema prop = entry.getValue();

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
        final Schema jsonOrderBean = context.resolve(new AnnotatedType(JsonOrderBean.class));
        final Map<String, Schema> props = jsonOrderBean.getProperties();
        assertEquals(new ArrayList<String>(props.keySet()), Arrays.asList("a", "b", "c", "d"));
    }

    @Test
    public void testTheCountBean() throws Exception {
        final Schema model = context.resolve(new AnnotatedType(TheCount.class));
        final Map<String, Schema> props = model.getProperties();
        assertEquals(props.size(), 1);

        String key = props.keySet().iterator().next();

        final Schema prop = props.get(key);
        assertEquals(key, "theCount");
    }

    @Test
    public void testStringDateMap() throws Exception {
        final Schema model = context.resolve(new AnnotatedType(StringDateMapBean.class));
        final Map<String, Schema> props = model.getProperties();
        assertEquals(props.size(), 1);

        String key = props.keySet().iterator().next();

        final Schema prop = props.get(key);
        assertEquals(key, "stuff");
    }

    @Test
    public void testIntArray() throws Exception {
        final Schema model = context.resolve(new AnnotatedType(IntArrayBean.class));
        final Map<String, Schema> props = model.getProperties();
        assertEquals(props.size(), 1);

        String key = props.keySet().iterator().next();
        final Schema prop = props.get(key);
        assertEquals(key, "b");
        assertEquals(prop.getType(), "array");
    }



    protected boolean isJacksonAtLeast2_9() throws NoSuchMethodException {

        try {
            Method m = BeanDescription.class.getMethod("findJsonValueAccessor", null);
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }



    @Test
    public void testJsonValue_Ticket3409() throws Exception {

        DeserializationFeature aa = DeserializationFeature.valueOf("FAIL_ON_UNKNOWN_PROPERTIES");

        Map<String, Schema> models = ModelConverters.getInstance().readAll(PlanetName.Planet.class);
        assertNotNull(models.get("Planet"));
        if (isJacksonAtLeast2_9()) {
            assertNull(models.get("PlanetName"));
            assertEquals(((Schema) models.get("Planet").getProperties().get("name")).getType(), "string");
        }
    }

    @Test
    public void testComplex() throws Exception {

        final Schema model = context.resolve(new AnnotatedType(ComplexBean.class));
        assertNotNull(model);
        final Map<String, Schema> props = model.getProperties();
        assertEquals(props.size(), 6);
    }

  /*
  /**********************************************************
  /* Test methods
  /**********************************************************
   */

    @JsonPropertyOrder({"a", "b"})
    @io.swagger.v3.oas.annotations.media.Schema(description = "DESC")
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

    static class ComplexBean {
        public String j;
        @JsonIgnore
        public SimpleBean simpleBean;

        @JsonCreator
        public ComplexBean(@JsonProperty("b") int b,
                           @JsonProperty("c") long c, @JsonProperty("d") float d, @JsonProperty("e") double e,
                           @JsonProperty("j") String j) {
            simpleBean = new SimpleBean();
            simpleBean.b = b;
            simpleBean.c = c;
            simpleBean.d = d;
            simpleBean.e = e;
            this.j = j;
        }

        public SimpleBean getSimpleBean() {
            return simpleBean;
        }

        public String getA() {
            return simpleBean.getA();
        }

        public int getB() {
            return simpleBean.b;
        }

        public long getC() {
            return simpleBean.c;
        }

        public float getD() {
            return simpleBean.d;
        }

        public double getE() {
            return simpleBean.e;
        }

        public String getJ() {
            return j;
        }
    }


    static class PlanetName {

        @JsonValue
        private final String value;

        @JsonCreator
        private PlanetName(String value) {
            this.value = value;
        }

        public static PlanetName valueOf(String value) {
            return new PlanetName(value);
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }


        static class Planet {

            private PlanetName name;

            public PlanetName getName() {
                return name;
            }

            public void setName(PlanetName name) {
                this.name = name;
            }
        }
    }

}
