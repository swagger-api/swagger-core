package io.swagger.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class InheritedBeanTest extends SwaggerTestBase {

    private ModelResolver modelResolver;
    private ModelConverterContextImpl context;

    @BeforeTest
    public void setup() {
        modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }

    @Test
    public void testInheritedBean() throws Exception {
        final Model baseModel = context.resolve(BaseBean.class);

        assertNotNull(baseModel);
        assertBasePropertiesValid(baseModel.getProperties());

        final Model subModel = context.getDefinedModels().get("Sub1Bean");
        assertNotNull(subModel);
        // make sure child points at parent
        assertTrue(subModel instanceof ComposedModel);
        ComposedModel cm = (ComposedModel) subModel;
        assertEquals(cm.getParent().getReference(), "#/definitions/BaseBean");

        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm.getChild().getProperties());
    }

    @Test
    public void testInheritedChildBean() throws Exception {
        final Model subModel = context.resolve(Sub1Bean.class);
        assertNotNull(subModel);
        // make sure child points at parent
        assertTrue(subModel instanceof ComposedModel);
        ComposedModel cm = (ComposedModel) subModel;
        assertEquals(cm.getParent().getReference(), "#/definitions/BaseBean");

        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm.getChild().getProperties());

        final Model baseModel = context.getDefinedModels().get("BaseBean");
        assertNotNull(baseModel);
        assertBasePropertiesValid(baseModel.getProperties());
    }

    private void assertBasePropertiesValid(Map<String, Property> baseProperites) {
        assertEquals(baseProperites.size(), 3);
        for (Map.Entry<String, Property> entry : baseProperites.entrySet()) {
            final String name = entry.getKey();
            final Property prop = entry.getValue();
            if ("type".equals(name)) {
                assertEquals(prop.getType(), "string");
            } else if ("a".equals(name)) {
                assertEquals(prop.getType(), "integer");
                assertEquals(prop.getFormat(), "int32");
            } else if ("b".equals(name)) {
                assertEquals(prop.getType(), "string");
            }
        }
    }

    private void assertSub1PropertiesValid(Map<String, Property> subProperties) {
        assertEquals(subProperties.size(), 1);
        for (Map.Entry<String, Property> entry : subProperties.entrySet()) {
            final String name = entry.getKey();
            final Property prop = entry.getValue();
            if ("c".equals(name)) {
                assertEquals(prop.getType(), "integer");
                assertEquals(prop.getFormat(), "int32");
            }
        }
    }

    @JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
    @JsonSubTypes({@JsonSubTypes.Type(value = Sub1Bean.class, name = "sub1")})
    @ApiModel(description = "BaseBean", discriminator = "type", subTypes = {Sub1Bean.class})
    static class BaseBean {
        public String type;
        public int a;
        public String b;
    }

    @ApiModel(description = "Sub1Bean", parent = BaseBean.class)
    static class Sub1Bean extends BaseBean {
        public int c;
    }

    @Test
    public void testMultipleInheritedBean() throws Exception {
        final Model baseModel = context.resolve(MultipleBaseBean.class);

        assertNotNull(baseModel);
        assertBasePropertiesValid(baseModel.getProperties());

        final Model sub1Model = context.getDefinedModels().get("MultipleSub1Bean");
        assertNotNull(sub1Model);
        // make sure child points at parent
        assertTrue(sub1Model instanceof ComposedModel);
        ComposedModel cm1 = (ComposedModel) sub1Model;
        assertEquals(cm1.getParent().getReference(), "#/definitions/MultipleBaseBean");

        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm1.getChild().getProperties());

        final Model sub2Model = context.getDefinedModels().get("MultipleSub2Bean");
        assertNotNull(sub2Model);
        // make sure child points at parent
        assertTrue(sub2Model instanceof ComposedModel);
        ComposedModel cm2 = (ComposedModel) sub2Model;
        assertEquals(cm2.getParent().getReference(), "#/definitions/MultipleBaseBean");

        // make sure parent properties are filtered out of subclass
        assertSub2PropertiesValid(cm2.getChild().getProperties());
    }

    @Test
    public void testMultipleInheritedChildBean() throws Exception {
        final Model subModel = context.resolve(MultipleSub1Bean.class);
        assertNotNull(subModel);
        // make sure child points at parent
        assertTrue(subModel instanceof ComposedModel);
        ComposedModel cm = (ComposedModel) subModel;
        assertEquals(cm.getParent().getReference(), "#/definitions/MultipleBaseBean");

        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm.getChild().getProperties());

        final Model baseModel = context.getDefinedModels().get("MultipleBaseBean");
        assertNotNull(baseModel);
        assertBasePropertiesValid(baseModel.getProperties());

        final Model sub1Model = context.getDefinedModels().get("MultipleSub1Bean");
        assertNotNull(sub1Model);
        // make sure child points at parent
        assertTrue(sub1Model instanceof ComposedModel);
        ComposedModel cm1 = (ComposedModel) sub1Model;
        assertEquals(cm1.getParent().getReference(), "#/definitions/MultipleBaseBean");

        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm1.getChild().getProperties());

        final Model sub2Model = context.getDefinedModels().get("MultipleSub2Bean");
        assertNotNull(sub2Model);
        // make sure child points at parent
        assertTrue(sub2Model instanceof ComposedModel);
        ComposedModel cm2 = (ComposedModel) sub2Model;
        assertEquals(cm2.getParent().getReference(), "#/definitions/MultipleBaseBean");

        // make sure parent properties are filtered out of subclass
        assertSub2PropertiesValid(cm2.getChild().getProperties());
    }

    private void assertSub2PropertiesValid(Map<String, Property> subProperties) {
        assertEquals(subProperties.size(), 1);
        for (Map.Entry<String, Property> entry : subProperties.entrySet()) {
            final String name = entry.getKey();
            final Property prop = entry.getValue();
            if ("d".equals(name)) {
                assertEquals(prop.getType(), "integer");
                assertEquals(prop.getFormat(), "int32");
            }
        }
    }

    @JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = MultipleSub1Bean.class, name = "multipleSub1"),
            @JsonSubTypes.Type(value = MultipleSub2Bean.class, name = "multipleSub2")
    })
    @ApiModel(description = "MultipleBaseBean", discriminator = "type", subTypes = {MultipleSub1Bean.class,
            MultipleSub2Bean.class})
    static class MultipleBaseBean {
        public String type;
        public int a;
        public String b;
    }

    @ApiModel(description = "MultipleSub1Bean", parent = MultipleBaseBean.class)
    static class MultipleSub1Bean extends MultipleBaseBean {
        public int c;
    }

    @ApiModel(description = "MultipleSub2Bean", parent = MultipleBaseBean.class)
    static class MultipleSub2Bean extends MultipleBaseBean {
        public int d;
    }

}
