package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.*;

public class JsonSubTypesAndSchemaOneOfTest extends SwaggerTestBase {

    private static final String BASE_PROPERTY = "a";
    private static final String BASE_PROPERTY_2 = "b";
    private static final String SUB_BEAN_1_NAME = "SubBean1";
    private static final String SUB_BEAN_2_NAME = "SubBean2";
    private static final String SUB_BEAN_INTERFACE_1_NAME = "SubBean1InterfaceImplementor";
    private static final String SUB_BEAN_INTERFACE_2_NAME = "SubBean2InterfaceImplementor";
    private static final String CHILD_1_PROPERTY = "c";
    private static final String CHILD_2_PROPERTY = "d";
    private static final String DISCRIMINATOR_PROPERTY_NAME = "type";

    private ModelConverterContextImpl context;

    @BeforeMethod
    public void setup() {
        ModelResolver.composedModelPropertiesAsSibling = false;
        ModelResolver modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }

    @AfterTest
    public void afterTest() {
        ModelResolver.composedModelPropertiesAsSibling = false;
        ModelResolver.jsonSubTypesOneOf = false;
    }

    @Test
    public void beanWithJsonSubTypesAndSchemaOneOfHasAllOfAndOneOfInModelSchemaObject() {
        final Schema<?> baseModel = context.resolve(new AnnotatedType(BaseBean.class));
        assertNotNull(baseModel);
        assertBasePropertiesValid(baseModel.getProperties());

        // The base class contains a oneOf-definition which will make the child classes have a recursive reference.
        // Child's allOf -> Parent -> Parent's oneOf -> Child -> Child's allOf -> ...
        assertNotNull(baseModel.getOneOf());
        assertEquals(baseModel.getOneOf().size(), 2);

        assertEquals(baseModel.getDiscriminator().getPropertyName(), DISCRIMINATOR_PROPERTY_NAME);
        assertEquals(baseModel.getDiscriminator().getMapping().get(SUB_BEAN_1_NAME), "#/components/schemas/SubBean1");
        assertEquals(baseModel.getDiscriminator().getMapping().get(SUB_BEAN_2_NAME), "#/components/schemas/SubBean2");

        final Schema<?> subModel1 = context.getDefinedModels().get(SUB_BEAN_1_NAME);
        assertNotNull(subModel1);
        // make sure child points at parent
        assertTrue(subModel1 instanceof ComposedSchema);
        ComposedSchema cm1 = (ComposedSchema) subModel1;
        assertEquals(cm1.getAllOf().get(0).get$ref(), "#/components/schemas/BaseBean");

        // make sure parent properties are filtered out of subclass
        assertSubPropertiesValid(cm1.getAllOf().get(1).getProperties(), CHILD_1_PROPERTY);

        final Schema<?> subModel2 = context.getDefinedModels().get(SUB_BEAN_2_NAME);
        assertNotNull(subModel2);
        // make sure child points at parent
        assertTrue(subModel2 instanceof ComposedSchema);
        ComposedSchema cm2 = (ComposedSchema) subModel2;
        assertEquals(cm2.getAllOf().get(0).get$ref(), "#/components/schemas/BaseBean");

        // make sure parent properties are filtered out of subclass
        assertSubPropertiesValid(cm1.getAllOf().get(1).getProperties(), CHILD_2_PROPERTY);
    }

    private void assertBasePropertiesValid(Map<String, Schema> baseProperties) {
        assertEquals(baseProperties.size(), 3);
        for (Map.Entry<String, Schema> entry : baseProperties.entrySet()) {
            final String name = entry.getKey();
            final Schema<?> prop = entry.getValue();
            if ("type".equals(name)) {
                assertEquals(prop.getType(), "string");
            } else if (BASE_PROPERTY.equals(name)) {
                assertEquals(prop.getType(), "integer");
                assertEquals(prop.getFormat(), "int32");
            } else if (BASE_PROPERTY_2.equals(name)) {
                assertEquals(prop.getType(), "string");
            }
        }
    }

    @JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SubBean1.class, name = "SubBean1"),
            @JsonSubTypes.Type(value = SubBean2.class, name = "SubBean2")
    })
    @io.swagger.v3.oas.annotations.media.Schema(
            description = "BaseBean",
            discriminatorProperty = "type",
            discriminatorMapping = {
                    @DiscriminatorMapping(value = "SubBean1", schema = SubBean1.class),
                    @DiscriminatorMapping(value = "SubBean2", schema = SubBean2.class)
            },
            oneOf = {SubBean1.class, SubBean2.class}
    )
    static class BaseBean {
        public String type;
        public int a;
        public String b;
    }

    static class SubBean1 extends BaseBean {
        public int a;
        public int c;
    }

    static class SubBean2 extends BaseBean {
        public int a;
        public int d;
    }

    @Test
    public void beanWithJsonSubTypesImplementsBeanWithSchemaOneOfHasOnlyOneOfInModelSchemaObject() {
        final Schema<?> baseModel = context.resolve(new AnnotatedType(BaseBeanInterfaceImplementor.class));
        assertNotNull(baseModel);
        assertNull(baseModel.getProperties());

        assertEquals(baseModel.getDiscriminator().getPropertyName(), DISCRIMINATOR_PROPERTY_NAME);
        assertEquals(baseModel.getDiscriminator().getMapping().get(SUB_BEAN_INTERFACE_1_NAME), "#/components/schemas/SubBean1InterfaceImplementor");
        assertEquals(baseModel.getDiscriminator().getMapping().get(SUB_BEAN_INTERFACE_2_NAME), "#/components/schemas/SubBean2InterfaceImplementor");

        final Schema<?> subModel1 = context.getDefinedModels().get(SUB_BEAN_INTERFACE_1_NAME);
        assertNotNull(subModel1);
        //We should not have a parent in the schema
        assertNull(subModel1.getAllOf());

        //The child should have the parent's properties and its own
        assertEquals(subModel1.getProperties().size(), 4);
        assertNotNull(subModel1.getProperties().get(CHILD_1_PROPERTY));

        final Schema<?> subModel2 = context.getDefinedModels().get(SUB_BEAN_INTERFACE_2_NAME);
        assertNotNull(subModel2);
        //We should not have a parent in the schema
        assertNull(subModel2.getAllOf());

        //The child should have the parent's properties and its own
        assertEquals(subModel2.getProperties().size(), 4);
        assertNotNull(subModel2.getProperties().get(CHILD_2_PROPERTY));
    }

    @io.swagger.v3.oas.annotations.media.Schema(
            description = "InterfaceBean",
            discriminatorProperty = "type",
            discriminatorMapping = {
                    @DiscriminatorMapping(value = "SubBean1InterfaceImplementor", schema = SubBean1InterfaceImplementor.class),
                    @DiscriminatorMapping(value = "SubBean2InterfaceImplementor", schema = SubBean2InterfaceImplementor.class)
            },
            oneOf = {SubBean1.class, SubBean2.class}
    )
    interface InterfaceBean {
        String type();

        int a();

        String b();
    }

    @JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SubBean1InterfaceImplementor.class, name = "SubBean1InterfaceImplementor"),
            @JsonSubTypes.Type(value = SubBean2InterfaceImplementor.class, name = "SubBean2InterfaceImplementor")
    })
    @io.swagger.v3.oas.annotations.media.Schema(implementation = InterfaceBean.class)
    static class BaseBeanInterfaceImplementor {
        public String type;
        public int a;
        public String b;
    }

    static class SubBean1InterfaceImplementor extends BaseBeanInterfaceImplementor {
        public int c;
    }

    static class SubBean2InterfaceImplementor extends BaseBeanInterfaceImplementor {
        public int d;
    }

    @Test
    public void jsonSubTypesOnlyParentHasNoOneOfByDefault() {
        // Default behavior (opt-in disabled): a @JsonSubTypes-only type is NOT composed into a oneOf,
        // matching the 3.0.0 behavior.
        ModelResolver.jsonSubTypesOneOf = false;
        final Schema<?> model = context.resolve(new AnnotatedType(PolymorphicInterface.class));
        assertNotNull(model);
        assertTrue(model.getOneOf() == null || model.getOneOf().isEmpty(),
                "with the default (opt-in disabled) the @JsonSubTypes-only parent must not be composed into a oneOf");
    }

    @Test
    public void jsonSubTypesOnlyParentIsComposedIntoOneOfWhenOptedIn() {
        ModelResolver.jsonSubTypesOneOf = true;
        try {
            final Schema<?> model = context.resolve(new AnnotatedType(PolymorphicInterface.class));
            assertTrue(model instanceof ComposedSchema,
                    "when opted in the @JsonSubTypes-only parent should be converted to a oneOf ComposedSchema");
            assertNotNull(model.getOneOf());
            assertEquals(model.getOneOf().size(), 2);
            assertEquals(model.getOneOf().get(0).get$ref(), "#/components/schemas/PolymorphicSubtype1");
            assertEquals(model.getOneOf().get(1).get$ref(), "#/components/schemas/PolymorphicSubtype2");
        } finally {
            ModelResolver.jsonSubTypesOneOf = false;
        }
    }

    @Test
    public void extensionPointAllowsSubclassToRestorePreviousBehavior() {
        ModelResolver.jsonSubTypesOneOf = true;
        try {
            final ModelConverterContextImpl subclassContext =
                    new ModelConverterContextImpl(new RestorePreviousBehaviorModelResolver(new ObjectMapper()));
            final Schema<?> restoredModel = subclassContext.resolve(new AnnotatedType(PolymorphicInterface.class));
            assertFalse(restoredModel instanceof ComposedSchema && ((ComposedSchema) restoredModel).getOneOf() != null,
                    "the resolved parent must not be auto-converted to a oneOf ComposedSchema when resolveSubtypes() is overridden");
        } finally {
            ModelResolver.jsonSubTypesOneOf = false;
        }
    }

    static class RestorePreviousBehaviorModelResolver extends ModelResolver {
        RestorePreviousBehaviorModelResolver(ObjectMapper mapper) {
            super(mapper);
        }

        @Override
        protected boolean resolveSubtypes(Schema model, BeanDescription bean,
                                          ModelConverterContext context, JsonView jsonViewAnnotation) {
            return false;
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = PolymorphicSubtype1.class, name = "subtype1"),
            @JsonSubTypes.Type(value = PolymorphicSubtype2.class, name = "subtype2")
    })
    interface PolymorphicInterface {
        String getType();
        String getName();
    }

    static class PolymorphicSubtype1 implements PolymorphicInterface {
        public String type = "subtype1";
        public String name = "subtype1";

        @Override
        public String getType() {
            return type;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    static class PolymorphicSubtype2 implements PolymorphicInterface {
        public String type = "subtype2";
        public String name = "subtype2";

        @Override
        public String getType() {
            return type;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private void assertSubPropertiesValid(Map<String, Schema> subProperties, final String childPropertyName) {
        assertEquals(subProperties.size(), 1);
        for (Map.Entry<String, Schema> entry : subProperties.entrySet()) {
            final String name = entry.getKey();
            final Schema<?> prop = entry.getValue();
            if (childPropertyName.equals(name)) {
                assertEquals(prop.getType(), "integer");
                assertEquals(prop.getFormat(), "int32");
            }
        }
    }

}
