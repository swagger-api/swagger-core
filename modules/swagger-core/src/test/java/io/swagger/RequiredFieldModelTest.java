package io.swagger;

import static org.testng.Assert.assertTrue;

import io.swagger.converter.ModelConverters;
import io.swagger.models.ApiFirstRequiredFieldModel;
import io.swagger.models.Model;
import io.swagger.models.XmlFirstRequiredFieldModel;
import io.swagger.models.properties.Property;

import org.testng.annotations.Test;

import java.util.Map;

public class RequiredFieldModelTest {
    @Test(description = "it should apply read only flag when ApiProperty annotation first")
    public void testApiModelPropertyFirstPosition() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ApiFirstRequiredFieldModel.class);
        final Model  model = models.get("aaa");
        final Property prop = model.getProperties().get("a");
        assertTrue(prop.getRequired());
    }

    @Test(description = "it should apply read only flag when XmlElement annotation first")
    public void testApiModelPropertySecondPosition() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(XmlFirstRequiredFieldModel.class);
        final Model model = models.get("aaa");
        final Property prop = model.getProperties().get("a");
        assertTrue(prop.getRequired());
    }
}
