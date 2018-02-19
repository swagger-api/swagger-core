package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.oas.models.ApiFirstRequiredFieldModel;
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
}
