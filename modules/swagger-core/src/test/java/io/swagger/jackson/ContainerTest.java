package io.swagger.jackson;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;

import org.testng.annotations.Test;

import java.util.Map;

public class ContainerTest extends SwaggerTestBase {

    @Test
    public void testArray() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());

        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Model model = context
                .resolve(ArrayBean.class);

        final Map<String, Property> props = model.getProperties();
        assertEquals(1, props.size());
        final Property prop = props.get("a");
        assertNotNull(prop);
        assertEquals(prop.getType(), "array");

        final Property items = ((ArrayProperty) prop).getItems();
        assertNotNull(items);
        assertEquals(items.getType(), "integer");
    }

    @Test
    public void testMap() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Model model = context
                .resolve(MapBean.class);

        final Map<String, Property> props = model.getProperties();
        assertEquals(1, props.size());
        final Property prop = props.get("stuff");
        assertNotNull(prop);
        assertEquals(prop.getType(), "object");

        final Property items = ((MapProperty) prop).getAdditionalProperties();
        assertNotNull(items);
        assertEquals(items.getType(), "string");
        assertEquals(items.getFormat(), "date-time");
    }

    @Test
    public void testComplexMap() throws Exception {
        ModelResolver resolver = new ModelResolver(mapper());

        final ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        context.resolve(WrapperType.class);

        final Map<String, Model> models = context.getDefinedModels();
        final Model innerType = models.get("InnerType");
        assertNotNull(innerType);
        final Map<String, Property> innerProps = innerType.getProperties();
        assertEquals(innerProps.size(), 2);
        final Property foo = innerProps.get("foo");
        assertEquals(foo.getType(), "integer");
        assertEquals(foo.getFormat(), "int32");
        final Property name = innerProps.get("name");
        assertEquals(name.getType(), "string");

        final Model wrapperType = models.get("WrapperType");
        assertNotNull(wrapperType);
        assertEquals(wrapperType.getProperties().get("innerType").getType(), "object");
    }

    static class ArrayBean {
        public int[] a;
    }

    static class MapBean {
        public Map<Short, java.util.Calendar> stuff;
    }

    static class WrapperType {
        public Map<String, InnerType> innerType;
    }
}
