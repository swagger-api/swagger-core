package io.swagger.v3.core.converting;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.oas.models.Model1979;
import io.swagger.v3.core.oas.models.ModelWithEnumField;
import io.swagger.v3.core.oas.models.ModelWithEnumProperty;
import io.swagger.v3.core.oas.models.ModelWithEnumRefProperty;
import io.swagger.v3.core.oas.models.ModelWithJacksonEnumField;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class EnumPropertyTest {

    private ModelResolver modelResolver;
    private ModelConverterContextImpl context;

    @BeforeMethod
    public void setup() {
        ModelResolver.enumsAsRef = false;
        modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }


    @AfterTest
    public void afterTest() {
        ModelResolver.enumsAsRef = false;
    }

    @Test(description = "it should read a model with an enum property")
    public void testEnumProperty() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithEnumProperty.class);
        final String json = "{" +
                "   \"ModelWithEnumProperty\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"enumValue\":{" +
                "            \"type\":\"string\"," +
                "            \"enum\":[" +
                "               \"PRIVATE\"," +
                "               \"PUBLIC\"," +
                "               \"SYSTEM\"," +
                "               \"INVITE_ONLY\"" +
                "            ]" +
                "         }" +
                "      }" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(models, json);
    }

    @Test(description = "it should extract enum values from fields")
    public void testExtractEnumFields() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithEnumField.class);
        final Schema model = models.get("ModelWithEnumField");
        final Schema enumProperty = (Schema) model.getProperties().get("enumValue");
        assertTrue(enumProperty instanceof StringSchema);

        final StringSchema stringProperty = (StringSchema) enumProperty;
        assertEquals(stringProperty.getEnum(), Arrays.asList("PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY"));
    }

    @Test(description = "it should extract enum values from method return types")
    public void testExtractEnumReturnType() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithEnumProperty.class);
        final Schema model = models.get("ModelWithEnumProperty");
        final Schema enumProperty = (Schema) model.getProperties().get("enumValue");
        assertTrue(enumProperty instanceof StringSchema);

        final StringSchema stringProperty = (StringSchema) enumProperty;
        assertEquals(stringProperty.getEnum(), Arrays.asList("PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY"));
    }

    @Test(description = "it should read a model with an enum property as a reference")
    public void testEnumRefProperty() {
        Schema schema = context.resolve(new AnnotatedType(ModelWithEnumRefProperty.class));
        final Map<String, Schema> models = context.getDefinedModels();
        final String yaml = "ModelWithEnumRefProperty:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    a:\n" +
                "      $ref: '#/components/schemas/TestEnum'\n" +
                "    b:\n" +
                "      $ref: '#/components/schemas/TestEnum'\n" +
                "    c:\n" +
                "      $ref: '#/components/schemas/TestSecondEnum'\n" +
                "    d:\n" +
                "      type: string\n" +
                "      enum:\n" +
                "      - A_PRIVATE\n" +
                "      - A_PUBLIC\n" +
                "      - A_SYSTEM\n" +
                "      - A_INVITE_ONLY\n" +
                "TestEnum:\n" +
                "  type: string\n" +
                "  enum:\n" +
                "  - PRIVATE\n" +
                "  - PUBLIC\n" +
                "  - SYSTEM\n" +
                "  - INVITE_ONLY\n" +
                "TestSecondEnum:\n" +
                "  type: string\n" +
                "  enum:\n" +
                "  - A_PRIVATE\n" +
                "  - A_PUBLIC\n" +
                "  - A_SYSTEM\n" +
                "  - A_INVITE_ONLY\n";
        SerializationMatchers.assertEqualsToYaml(models, yaml);

    }

    @Test(description = "it should read a model with an enum property as a reference with fqn TypeNameResolver")
    public void testEnumRefPropertyWithFQNTypeNameResolver() {
        TypeNameResolver.std.setUseFqn(true);
        Schema schema = context.resolve(new AnnotatedType(ModelWithEnumRefProperty.class));
        final Map<String, Schema> models = context.getDefinedModels();
        final String yaml = "io.swagger.v3.core.oas.models.ModelWithEnumRefProperty:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    a:\n" +
                "      $ref: '#/components/schemas/io.swagger.v3.core.oas.models.TestEnum'\n" +
                "    b:\n" +
                "      $ref: '#/components/schemas/io.swagger.v3.core.oas.models.TestEnum'\n" +
                "    c:\n" +
                "      $ref: '#/components/schemas/io.swagger.v3.core.oas.models.TestSecondEnum'\n" +
                "    d:\n" +
                "      type: string\n" +
                "      enum:\n" +
                "      - A_PRIVATE\n" +
                "      - A_PUBLIC\n" +
                "      - A_SYSTEM\n" +
                "      - A_INVITE_ONLY\n" +
                "io.swagger.v3.core.oas.models.TestEnum:\n" +
                "  type: string\n" +
                "  enum:\n" +
                "  - PRIVATE\n" +
                "  - PUBLIC\n" +
                "  - SYSTEM\n" +
                "  - INVITE_ONLY\n" +
                "io.swagger.v3.core.oas.models.TestSecondEnum:\n" +
                "  type: string\n" +
                "  enum:\n" +
                "  - A_PRIVATE\n" +
                "  - A_PUBLIC\n" +
                "  - A_SYSTEM\n" +
                "  - A_INVITE_ONLY\n";
        TypeNameResolver.std.setUseFqn(false);
        SerializationMatchers.assertEqualsToYaml(models, yaml);

    }

    @Test(description = "it should read a model with an enum property as a reference, set via static var or sys prop")
    public void testEnumRefPropertyGlobal() {
        ModelResolver.enumsAsRef = true;
        Schema schema = context.resolve(new AnnotatedType(ModelWithEnumProperty.class));
        final Map<String, Schema> models = context.getDefinedModels();
        final String yaml = "ModelWithEnumProperty:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    enumValue:\n" +
                "      $ref: '#/components/schemas/TestEnum'\n" +
                "TestEnum:\n" +
                "  type: string\n" +
                "  enum:\n" +
                "  - PRIVATE\n" +
                "  - PUBLIC\n" +
                "  - SYSTEM\n" +
                "  - INVITE_ONLY\n";
        SerializationMatchers.assertEqualsToYaml(models, yaml);
        ModelResolver.enumsAsRef = false;
    }

    @Test(description = "it should not affect non-enum models when the enumsAsRef property is enabled globally")
    public void testEnumRefPropertyGlobalNotAffectingNonEnums() {
        ModelResolver.enumsAsRef = true;
        Schema schema = context.resolve(new AnnotatedType(Model1979.class));
        final Map<String, Schema> models = context.getDefinedModels();
        final String yaml = "Model1979:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: string\n" +
                "      nullable: true";
        SerializationMatchers.assertEqualsToYaml(models, yaml);
        ModelResolver.enumsAsRef = false;
    }

    @Test(description = "it should extract enum values from fields using JsonProperty and JsonValue")
    public void testExtractJacksonEnumFields() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithJacksonEnumField.class);
        final Schema model = models.get("ModelWithJacksonEnumField");

        final Schema firstEnumProperty = (Schema) model.getProperties().get("firstEnumValue");
        assertTrue(firstEnumProperty instanceof StringSchema);
        final StringSchema stringProperty = (StringSchema) firstEnumProperty;
        assertEquals(stringProperty.getEnum(), Arrays.asList("p1", "p2", "SYSTEM", "INVITE_ONLY"));

        final Schema secondEnumProperty = (Schema) model.getProperties().get("secondEnumValue");
        assertTrue(secondEnumProperty instanceof StringSchema);
        final StringSchema secondStringProperty = (StringSchema) secondEnumProperty;
        assertEquals(secondStringProperty.getEnum(), Arrays.asList("one", "two", "three"));

        final Schema thirdEnumProperty = (Schema) model.getProperties().get("thirdEnumValue");
        assertTrue(thirdEnumProperty instanceof StringSchema);
        final StringSchema thirdStringProperty = (StringSchema) thirdEnumProperty;
        assertEquals(thirdStringProperty.getEnum(), Arrays.asList("2", "4", "6"));

        final Schema fourthEnumProperty = (Schema) model.getProperties().get("fourthEnumValue");
        assertTrue(fourthEnumProperty instanceof StringSchema);
        final StringSchema fourthStringProperty = (StringSchema) fourthEnumProperty;
        assertEquals(fourthEnumProperty.getEnum(), Arrays.asList("one", "two", "three"));

        final Schema fifthEnumProperty = (Schema) model.getProperties().get("fifthEnumValue");
        assertTrue(fifthEnumProperty instanceof StringSchema);
        final StringSchema fifthStringProperty = (StringSchema) fifthEnumProperty;
        assertEquals(fifthEnumProperty.getEnum(), Arrays.asList("2", "4", "6"));

        final Schema sixthEnumProperty = (Schema) model.getProperties().get("sixthEnumValue");
        assertTrue(sixthEnumProperty instanceof StringSchema);
        final StringSchema sixthStringProperty = (StringSchema) sixthEnumProperty;
        assertEquals(sixthEnumProperty.getEnum(), Arrays.asList("one", "two", "three"));
    }
}
