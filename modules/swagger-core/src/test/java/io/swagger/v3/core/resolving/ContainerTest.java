package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.resolving.resources.InnerType;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ContainerTest extends SwaggerTestBase {

    @Test
    public void testArray() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());

        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context
                .resolve(new AnnotatedType(ArrayBean.class));

        final Map<String, Schema> props = model.getProperties();
        assertEquals(1, props.size());
        final Schema prop = props.get("a");
        assertNotNull(prop);
        assertEquals(prop.getType(), "array");

        final Schema items = ((ArraySchema) prop).getItems();
        assertNotNull(items);
        assertEquals(items.getType(), "integer");
    }

    @Test
    public void testMap() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context
                .resolve(new AnnotatedType(MapBean.class));

        final Map<String, Schema> props = model.getProperties();
        assertEquals(1, props.size());
        final Schema prop = props.get("stuff");
        assertNotNull(prop);
        assertEquals(prop.getType(), "object");

        final Schema items = (Schema)prop.getAdditionalProperties();
        assertNotNull(items);
        assertEquals(items.getType(), "string");
        assertEquals(items.getFormat(), "date-time");
    }

    @Test
    public void testComplexMap() throws Exception {
        ModelResolver resolver = new ModelResolver(mapper());

        final ModelConverterContextImpl context = new ModelConverterContextImpl(resolver);
        context.resolve(new AnnotatedType(WrapperType.class));

        final Map<String, Schema> models = context.getDefinedModels();
        final Schema innerType = models.get("InnerType");
        assertNotNull(innerType);
        final Map<String, Schema> innerProps = innerType.getProperties();
        assertEquals(innerProps.size(), 2);
        final Schema foo = innerProps.get("foo");
        assertEquals(foo.getType(), "integer");
        assertEquals(foo.getFormat(), "int32");
        final Schema name = innerProps.get("name");
        assertEquals(name.getType(), "string");

        final Schema wrapperType = models.get("WrapperType");
        assertNotNull(wrapperType);
        assertEquals(((Schema) wrapperType.getProperties().get("innerType")).getType(), "object");
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
