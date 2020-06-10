package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.oas.models.ApiFirstRequiredFieldModel;
import io.swagger.v3.core.oas.models.RequiredRefFieldModel;
import io.swagger.v3.core.oas.models.XmlFirstRequiredFieldModel;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class RequiredFieldModelTest {
    @Test(description = "it should apply required flag when ApiProperty annotation first")
    public void testApiModelPropertyFirstPosition() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ApiFirstRequiredFieldModel.class);
        final Schema model = models.get("aaa");
        final Schema prop = (Schema) model.getProperties().get("bla");
        assertNotNull(prop);
        assertTrue(model.getRequired().contains("bla"));
    }

    @Test(description = "it should apply required flag when XmlElement annotation first")
    public void testApiModelPropertySecondPosition() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(XmlFirstRequiredFieldModel.class);
        final Schema model = models.get("aaa");
        final Schema prop = (Schema) model.getProperties().get("a");
        assertNotNull(prop);
        assertTrue(model.getRequired().contains("a"));
    }


    @Test(description = "it should apply required flag also to ref fields")
    public void testApiModelRefProperty() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(RequiredRefFieldModel.class);
        final Schema model = models.get("RequiredRefFieldModel");
        final Schema prop = (Schema) model.getProperties().get("a");
        assertNotNull(prop);
        final Schema prop2 = (Schema) model.getProperties().get("b");
        assertNotNull(prop2);
        assertTrue(model.getRequired().contains("a"));
        assertTrue(model.getRequired().contains("b"));
    }
}
