package io.swagger.resolving;

import io.swagger.converter.ModelConverters;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class HiddenFieldTest {

    @Test(description = "it should ignore a hidden field")
    public void testHiddenField() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithHiddenFields.class);

        final Schema model = models.get("ModelWithHiddenFields");
        assertNotNull(model);
        assertEquals(model.getProperties().size(), 2);

        final Schema idValue = (Schema)model.getProperties().get("id");
        assertTrue(idValue instanceof IntegerSchema);

        assertTrue(model.getRequired().contains("id"));

        final Schema nameValue = (Schema)model.getProperties().get("name");
        assertTrue(nameValue instanceof StringSchema);
    }

    class ModelWithHiddenFields {
        @io.swagger.oas.annotations.media.Schema(required = true)
        public Long id = null;

        @io.swagger.oas.annotations.media.Schema(required = true, hidden = false)
        public String name = null;

        @io.swagger.oas.annotations.media.Schema(hidden = true)
        public String password = null;
    }
}
