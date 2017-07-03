package io.swagger.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.oas.models.media.ComposedSchema;
import io.swagger.oas.models.media.Schema;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
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
        final Schema baseModel = context.resolve(BaseBean.class);

        assertNotNull(baseModel);
        assertBasePropertiesValid(baseModel.getProperties());

        final Schema subModel = context.getDefinedModels().get("Sub1Bean");
        assertNotNull(subModel);
        // make sure child points at parent
        assertTrue(subModel instanceof ComposedSchema);
        ComposedSchema cm = (ComposedSchema) subModel;
        assertEquals(cm.getAllOf().get(0).get$ref(), "#/components/schemas/BaseBean");

        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm.getProperties());
    }

    @Test
    public void testInheritedChildBean() throws Exception {
        final Schema subModel = context.resolve(Sub1Bean.class);
        assertNotNull(subModel);
        // make sure child points at parent
        assertTrue(subModel instanceof ComposedSchema);
        ComposedSchema cm = (ComposedSchema) subModel;
        assertEquals(cm.getAllOf().get(0).get$ref(), "#/components/schemas/BaseBean");

        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm.getProperties());

        final Schema baseModel = context.getDefinedModels().get("BaseBean");
        assertNotNull(baseModel);
        assertBasePropertiesValid(baseModel.getProperties());
    }

    @Test
    public void testComposedChildBean() throws Exception {
        final Schema subModel = context.resolve(Sub2Bean.class);
        assertNotNull(subModel);
        // make sure child points at parent
        assertTrue(subModel instanceof ComposedSchema);
        ComposedSchema cm = (ComposedSchema) subModel;
        assertEquals(cm.getAllOf().get(0).get$ref(), "#/components/schemas/BaseBean2");

        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm.getProperties());

        final Schema baseModel = context.getDefinedModels().get("BaseBean2");
        assertNotNull(baseModel);
        assertBase2PropertiesValid(baseModel.getProperties());
    }

    @Test
    public void testComposedUberObject() throws Exception {
        final Schema uberModel = context.resolve(UberObject.class);
        assertNotNull(uberModel);
        // make sure child points at parent
        assertTrue(uberModel instanceof ComposedSchema);
        ComposedSchema cm = (ComposedSchema) uberModel;
        assertEquals(cm.getAnyOf().size(), 2);
        //assertEquals(cm.getAnyOf().get(0).get$ref(), "#/components/schemas/UserObject");
        assertEquals(cm.getAnyOf().get(0).getProperties().size(), 2);
        // make sure parent properties are filtered out of subclass
        assertNull(cm.getProperties());

/*
        final Schema interfaceModel = context.getDefinedModels().get("UserObject");
        assertNotNull(interfaceModel);
        assertUserObjectPropertiesValid(interfaceModel.getProperties());
*/
    }


    private void assertBasePropertiesValid(Map<String, Schema> baseProperites) {
        assertEquals(baseProperites.size(), 3);
        for (Map.Entry<String, Schema> entry : baseProperites.entrySet()) {
            final String name = entry.getKey();
            final Schema prop = entry.getValue();
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

    private void assertBase2PropertiesValid(Map<String, Schema> baseProperites) {
        assertEquals(baseProperites.size(), 4);
        for (Map.Entry<String, Schema> entry : baseProperites.entrySet()) {
            final String name = entry.getKey();
            final Schema prop = entry.getValue();
            if ("type".equals(name)) {
                assertEquals(prop.getType(), "string");
            } else if ("a".equals(name)) {
                assertEquals(prop.getType(), "integer");
                assertEquals(prop.getFormat(), "int32");
            } else if ("b".equals(name)) {
                assertEquals(prop.getType(), "string");
            } else if ("d".equals(name)) {
                assertEquals(prop.getType(), "integer");
                assertEquals(prop.getFormat(), "int32");
            }
        }
    }

    private void assertSub1PropertiesValid(Map<String, Schema> subProperties) {
        assertEquals(subProperties.size(), 1);
        for (Map.Entry<String, Schema> entry : subProperties.entrySet()) {
            final String name = entry.getKey();
            final Schema prop = entry.getValue();
            if ("c".equals(name)) {
                assertEquals(prop.getType(), "integer");
                assertEquals(prop.getFormat(), "int32");
            }
        }
    }

    private void assertUserObjectPropertiesValid(Map<String, Schema> subProperties) {
        assertEquals(subProperties.size(), 2);
        for (Map.Entry<String, Schema> entry : subProperties.entrySet()) {
            final String name = entry.getKey();
            final Schema prop = entry.getValue();
            if ("id".equals(name)) {
                assertEquals(prop.getType(), "string");
            }
            if ("name".equals(name)) {
                assertEquals(prop.getType(), "string");
            }
        }
    }


    @JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
    @JsonSubTypes({@JsonSubTypes.Type(value = Sub1Bean.class, name = "sub1")})
    @io.swagger.oas.annotations.media.Schema(description = "BaseBean"
//            , discriminator = "type", subTypes = {Sub1Bean.class}
    )
    static class BaseBean {
        public String type;
        public int a;
        public String b;
    }

    @io.swagger.oas.annotations.media.Schema(description = "Sub1Bean", allOf = {BaseBean.class})
    static class Sub1Bean extends BaseBean {
        public int c;
    }

    @JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
    @JsonSubTypes({@JsonSubTypes.Type(value = Sub2Bean.class, name = "sub2")})
    @io.swagger.oas.annotations.media.Schema(description = "BaseBean2"
//            , discriminator = "type", subTypes = {Sub1Bean.class}
    )
    static class BaseBean2 {
        public String type;
        public int a;
        public String b;

        public int getD() {
            return d;
        }

        public void setD(int d) {
            this.d = d;
        }

        private int d;
    }

    @io.swagger.oas.annotations.media.Schema(description = "Sub2Bean", allOf = {BaseBean2.class}, anyOf = {BaseBean.class})
    static class Sub2Bean extends BaseBean2 {
        public int a;
        public int c;
    }

    @io.swagger.oas.annotations.media.Schema(anyOf = {UserObject.class, EmployeeObject.class})
    static class UberObject implements UserObject, EmployeeObject {
        private String id;
        private String name;
        private String department;

        @Override
        public String getDepartment() {
            return department;
        }
        @Override
        public String getId() {
            return id;
        }
        @Override
        public String getName() {
            return name;
        }
    }

    @io.swagger.oas.annotations.media.Schema(description = "A User Object")
    interface UserObject {
        @io.swagger.oas.annotations.media.Schema(format = "uuid", required = true)
        String getId();
        String getName();
    }

    @io.swagger.oas.annotations.media.Schema(description = "An Employee Object", requiredProperties = {"department"})
    interface EmployeeObject {
        @io.swagger.oas.annotations.media.Schema(format = "email")
        String getId();
        String getDepartment();
    }

    @Test
    public void testMultipleInheritedBean() throws Exception {
        final Schema baseModel = context.resolve(MultipleBaseBean.class);

        assertNotNull(baseModel);
        assertBasePropertiesValid(baseModel.getProperties());

        final Schema sub1Model = context.getDefinedModels().get("MultipleSub1Bean");
        assertNotNull(sub1Model);
        // make sure child points at parent
        assertTrue(sub1Model instanceof ComposedSchema);
        ComposedSchema cm1 = (ComposedSchema) sub1Model;
        assertEquals(cm1.getAllOf().get(0).get$ref(), "#/components/schemas/MultipleBaseBean");
        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm1.getProperties());

        final Schema sub2Model = context.getDefinedModels().get("MultipleSub2Bean");
        assertNotNull(sub2Model);
        assertTrue(sub2Model instanceof ComposedSchema);
        ComposedSchema cm2 = (ComposedSchema) sub2Model;
        assertEquals(cm2.getAllOf().get(0).get$ref(), "#/components/schemas/MultipleBaseBean");
        // make sure parent properties are filtered out of subclass
        assertSub2PropertiesValid(cm2.getProperties());    }

    @Test
    public void testMultipleInheritedChildBean() throws Exception {
        final Schema subModel = context.resolve(MultipleSub1Bean.class);
        assertNotNull(subModel);
        // make sure child points at parent
        assertTrue(subModel instanceof ComposedSchema);
        ComposedSchema cm = (ComposedSchema) subModel;
        assertEquals(cm.getAllOf().get(0).get$ref(), "#/components/schemas/MultipleBaseBean");
        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm.getProperties());

        final Schema baseModel = context.getDefinedModels().get("MultipleBaseBean");
        assertNotNull(baseModel);
        assertBasePropertiesValid(baseModel.getProperties());

        final Schema sub1Model = context.getDefinedModels().get("MultipleSub1Bean");
        assertNotNull(sub1Model);
        // make sure child points at parent
        assertTrue(sub1Model instanceof ComposedSchema);
        ComposedSchema cm1 = (ComposedSchema) sub1Model;
        assertEquals(cm1.getAllOf().get(0).get$ref(), "#/components/schemas/MultipleBaseBean");
        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm1.getProperties());

        final Schema sub2Model = context.getDefinedModels().get("MultipleSub2Bean");
        assertNotNull(sub2Model);
        assertTrue(sub2Model instanceof ComposedSchema);
        ComposedSchema cm2 = (ComposedSchema) sub2Model;
        assertEquals(cm2.getAllOf().get(0).get$ref(), "#/components/schemas/MultipleBaseBean");
        // make sure parent properties are filtered out of subclass
        assertSub2PropertiesValid(cm2.getProperties());
    }


    private void assertSub2PropertiesValid(Map<String, Schema> subProperties) {
        assertEquals(subProperties.size(), 1);
        for (Map.Entry<String, Schema> entry : subProperties.entrySet()) {
            final String name = entry.getKey();
            final Schema prop = entry.getValue();
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
    @io.swagger.oas.annotations.media.Schema(description = "MultipleBaseBean"
//            , discriminator = "type", subTypes = {MultipleSub1Bean.class, MultipleSub2Bean.class}
    )
    static class MultipleBaseBean {
        public String type;
        public int a;
        public String b;
    }

    @io.swagger.oas.annotations.media.Schema(description = "MultipleSub1Bean", allOf = {MultipleBaseBean.class})
    static class MultipleSub1Bean extends MultipleBaseBean {
        public int c;
    }

    @io.swagger.oas.annotations.media.Schema(description = "MultipleSub2Bean", allOf = {MultipleBaseBean.class})
    static class MultipleSub2Bean extends MultipleBaseBean {
        public int d;
    }

}
