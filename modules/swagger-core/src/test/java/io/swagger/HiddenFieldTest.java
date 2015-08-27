package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

import org.testng.annotations.Test;

import java.util.Map;

public class HiddenFieldTest {

    @Test(description = "it should ignore a hidden field")
    public void testHiddenField() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ModelWithHiddenFields.class);

        final Model model = models.get("ModelWithHiddenFields");
        assertNotNull(model);
        assertEquals(model.getProperties().size(), 2);

        final Property idValue = model.getProperties().get("id");
        assertTrue(idValue instanceof LongProperty);
        assertTrue(idValue.getRequired());

        final Property nameValue = model.getProperties().get("name");
        assertTrue(nameValue instanceof StringProperty);
    }

    class ModelWithHiddenFields {
        @ApiModelProperty(required = true)
        public Long id = null;

        @ApiModelProperty(required = true, hidden = false)
        public String name = null;

        @ApiModelProperty(hidden = true)
        public String password = null;
    }
}
