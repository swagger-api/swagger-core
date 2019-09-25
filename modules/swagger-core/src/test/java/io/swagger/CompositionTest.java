package io.swagger;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Model;
import io.swagger.models.composition.AbstractBaseModelWithoutFields;
import io.swagger.models.composition.Animal;
import io.swagger.models.composition.Human;
import io.swagger.models.composition.ModelWithFieldWithSubTypes;
import io.swagger.util.ResourceUtils;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class CompositionTest {

    @Test(description = "read a model with required params and description")
    public void readModelWithRequiredParams() throws IOException {
        compareAsJson(Human.class, "Human.json");
    }

    @Test(description = "read a model with composition")
    public void readModelWithComposition() throws IOException {
        compareAsJson(Animal.class, "Animal.json");
    }

    @Test(description = "create a model")
    public void createModel() throws IOException {
        compareAsJson(AbstractBaseModelWithoutFields.class, "AbstractBaseModelWithoutFields.json");
    }

    @Test(description = "create a ModelWithFieldWithSubTypes")
    public void createModelWithFieldWithSubTypes() throws IOException {
        compareAsJson(ModelWithFieldWithSubTypes.class, "ModelWithFieldWithSubTypes.json");
    }

    @Test
    public void testTicket2189() throws IOException {
        final Map<String, Model> schemas = ModelConverters.getInstance().readAll(BaseClass.class);
        SerializationMatchers.assertEqualsToYaml(schemas, "BaseClass:\n" +
                "  type: \"object\"\n" +
                "  properties:\n" +
                "    property:\n" +
                "      type: \"string\"\n" +
                "    type:\n" +
                "      type: \"string\"\n" +
                "SubClass:\n" +
                "  allOf:\n" +
                "  - $ref: \"#/definitions/BaseClass\"\n" +
                "  - type: \"object\"\n" +
                "    properties:\n" +
                "      subClassProperty:\n" +
                "        type: \"string\"");
    }

    static class SubClass extends BaseClass {
        public String subClassProperty;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SubClass.class, name = "SubClass"),
            @JsonSubTypes.Type(value = BaseClass.class, name = "BaseClass"),
    })
    static class BaseClass {
        public String property;
        public String type;
    }


    private void compareAsJson(Class<?> cls, String fileName) throws IOException {
        final Map<String, Model> schemas = ModelConverters.getInstance().readAll(cls);
        final String json = ResourceUtils.loadClassResource(getClass(), fileName);
        SerializationMatchers.assertEqualsToJson(schemas, json);
    }
}
