package io.swagger;

import static org.testng.Assert.assertTrue;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Model;
import io.swagger.models.composition.AbstractBaseModelWithoutFields;
import io.swagger.models.composition.Animal;
import io.swagger.models.composition.Human;
import io.swagger.models.composition.ModelWithFieldWithSubTypes;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CompositionTest {

    @Test(description = "read a model with required params and description")
    public void readModelWithRequiredParams() throws IOException {
        compareAsJson(Human.class, "CompositionTestModelWithRequiredParams.json");
    }

    @Test(description = "read a model with composition")
    public void readModelWithComposition() throws IOException {
        compareAsJson(Animal.class, "CompositionTestModelWithComposition.json");
    }

    @Test(description = "create a model")
    public void createModel() throws IOException {
        compareAsJson(AbstractBaseModelWithoutFields.class, "CompositionTestAbstractBaseModelWithoutFields.json");
    }

    @Test(description = "create a ModelWithFieldWithSubTypes")
    public void createModelWithFieldWithSubTypes() throws IOException {
        compareAsJson(ModelWithFieldWithSubTypes.class, "CompositionTestModelWithFieldWithSubTypes.json");
    }

    private void compareAsJson(Class<?> cls, String json) throws IOException {
        final Map<String, Model> schemas = ModelConverters.getInstance().readAll(cls);
        final InputStream in = getClass().getClassLoader().getResourceAsStream(json);
        assertTrue(SerializationMatchers.compareAsJson(schemas, IOUtils.toString(in, StandardCharsets.UTF_8)));
    }
}
