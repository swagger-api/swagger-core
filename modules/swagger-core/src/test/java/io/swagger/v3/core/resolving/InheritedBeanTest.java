package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
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
        final Schema baseModel = context.resolve(new AnnotatedType(BaseBean.class));
        assertNotNull(baseModel);
        assertBasePropertiesValid(baseModel.getProperties());

        assertEquals(baseModel.getDiscriminator().getPropertyName(), "type");
        assertEquals(baseModel.getDiscriminator().getMapping().get("Sub1BeanMapped"), "#/components/schemas/Sub1Bean");

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
        final Schema subModel = context.resolve(new AnnotatedType(Sub1Bean.class));
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
        final Schema subModel = context.resolve(new AnnotatedType(Sub2Bean.class));
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
        final Schema uberModel = context.resolve(new AnnotatedType(UberObject.class));
        assertNotNull(uberModel);
        // make sure child points at parent
        assertTrue(uberModel instanceof ComposedSchema);
        ComposedSchema cm = (ComposedSchema) uberModel;
        assertEquals(cm.getAnyOf().size(), 2);
        assertEquals(cm.getAnyOf().get(0).get$ref(), "#/components/schemas/UserObject");
        // parent properties are filtered out of subclass when parent doesn't define subtypes
        assertNotNull(cm.getProperties());
        assertEquals(cm.getProperties().size(), 3);

        final Schema interfaceModel = context.getDefinedModels().get("UserObject");
        assertNotNull(interfaceModel);
        assertUserObjectPropertiesValid(interfaceModel.getProperties());

    }

    @Test
    public void testHierarchy() throws Exception {
        final Schema baseModel = context.resolve(new AnnotatedType(BaseBean3.class));
        assertNotNull(baseModel);
        assertBasePropertiesValid(baseModel.getProperties());

        assertEquals(baseModel.getDiscriminator().getPropertyName(), "type");
        assertEquals(baseModel.getDiscriminator().getMapping().get("ChildBean3Mapped"), "#/components/schemas/ChildBean3");

        final Schema subModel = context.getDefinedModels().get("ChildBean3");
        assertNotNull(subModel);
        // make sure child points at parent
        assertTrue(subModel instanceof ComposedSchema);
        ComposedSchema cm = (ComposedSchema) subModel;
        assertEquals(cm.getAllOf().get(0).get$ref(), "#/components/schemas/BaseBean3");
        // make sure parent properties are filtered out of subclass
        assertSub1PropertiesValid(cm.getProperties());

        // assert grandchild
        final Schema subSubModel = context.getDefinedModels().get("GrandChildBean3");
        assertNotNull(subSubModel);
        // make sure child points at parent
        assertTrue(subSubModel instanceof ComposedSchema);
        cm = (ComposedSchema) subSubModel;
        assertEquals(cm.getAllOf().get(0).get$ref(), "#/components/schemas/ChildBean3");
        // make sure parent properties are filtered out of subclass
        assertSub2PropertiesValid(cm.getProperties());

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
    @io.swagger.v3.oas.annotations.media.Schema(description = "BaseBean"
            , discriminatorProperty = "type", discriminatorMapping = {@DiscriminatorMapping(value = "Sub1BeanMapped", schema = Sub1Bean.class)}
    )
    static class BaseBean {
        public String type;
        public int a;
        public String b;
    }

    @io.swagger.v3.oas.annotations.media.Schema(description = "Sub1Bean", allOf = {BaseBean.class})
    static class Sub1Bean extends BaseBean {
        public int c;
    }

    @JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
    @JsonSubTypes({@JsonSubTypes.Type(value = Sub2Bean.class, name = "sub2")})
    @io.swagger.v3.oas.annotations.media.Schema(description = "BaseBean2"
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

    @io.swagger.v3.oas.annotations.media.Schema(description = "Sub2Bean", allOf = {BaseBean2.class}, anyOf = {BaseBean.class})
    static class Sub2Bean extends BaseBean2 {
        public int a;
        public int c;
    }

    @JsonSubTypes({@JsonSubTypes.Type(value = ChildBean3.class, name = "childBean3")})
    @io.swagger.v3.oas.annotations.media.Schema(description = "BaseBean3"
            , discriminatorProperty = "type", discriminatorMapping = {@DiscriminatorMapping(value = "ChildBean3Mapped", schema = ChildBean3.class)}
    )
    static class BaseBean3 {
        public String type;
        public int a;
        public String b;
    }

    @JsonSubTypes({@JsonSubTypes.Type(value = GrandChildBean3.class, name = "grandChildBean3")})
    @io.swagger.v3.oas.annotations.media.Schema(description = "ChildBean3", allOf = {BaseBean3.class})
    static class ChildBean3 extends BaseBean3 {
        public int c;
    }

    @io.swagger.v3.oas.annotations.media.Schema(description = "GrandChildBean3", allOf = {ChildBean3.class})
    static class GrandChildBean3 extends ChildBean3 {
        public int d;
    }

    @io.swagger.v3.oas.annotations.media.Schema(anyOf = {UserObject.class, EmployeeObject.class})
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

    @io.swagger.v3.oas.annotations.media.Schema(description = "A User Object")
    interface UserObject {
        @io.swagger.v3.oas.annotations.media.Schema(format = "uuid", required = true)
        String getId();

        String getName();
    }

    @io.swagger.v3.oas.annotations.media.Schema(description = "An Employee Object", requiredProperties = {"department"})
    interface EmployeeObject {
        @io.swagger.v3.oas.annotations.media.Schema(format = "email")
        String getId();

        String getDepartment();
    }

    @Test
    public void testMultipleInheritedBean() throws Exception {
        final Schema baseModel = context.resolve(new AnnotatedType(MultipleBaseBean.class));

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

    @Test
    public void testMultipleInheritedChildBean() throws Exception {
        final Schema subModel = context.resolve(new AnnotatedType(MultipleSub1Bean.class));
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
    @io.swagger.v3.oas.annotations.media.Schema(description = "MultipleBaseBean"
//            , discriminator = "type", subTypes = {MultipleSub1Bean.class, MultipleSub2Bean.class}
    )
    static class MultipleBaseBean {
        public String type;
        public int a;
        public String b;
    }

    @io.swagger.v3.oas.annotations.media.Schema(description = "MultipleSub1Bean", allOf = {MultipleBaseBean.class})
    static class MultipleSub1Bean extends MultipleBaseBean {
        public int c;
    }

    @io.swagger.v3.oas.annotations.media.Schema(description = "MultipleSub2Bean", allOf = {MultipleBaseBean.class})
    static class MultipleSub2Bean extends MultipleBaseBean {
        public int d;
    }

}
