package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class HiddenFieldTest {

    @Test(description = "it should ignore a hidden field")
    public void testHiddenField() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithHiddenFields.class);

        final Schema model = models.get("ModelWithHiddenFields");
        assertNotNull(model);
        assertEquals(model.getProperties().size(), 2);

        final Schema idValue = (Schema) model.getProperties().get("id");
        assertTrue(idValue instanceof IntegerSchema);

        assertTrue(model.getRequired().contains("id"));

        final Schema nameValue = (Schema) model.getProperties().get("name");
        assertTrue(nameValue instanceof StringSchema);

        final Schema passwordValue = (Schema) model.getProperties().get("password");
        assertNull(passwordValue);
    }

    static class ModelWithHiddenFields {
        @io.swagger.v3.oas.annotations.media.Schema(required = true)
        public Long id = null;

        @io.swagger.v3.oas.annotations.media.Schema(required = true, hidden = false)
        public String name = null;

        @io.swagger.v3.oas.annotations.media.Schema(hidden = true)
        public String password = null;
    }

    @Test(description = "it should ignore a hidden field in @JsonCreator")
    public void testHiddenFieldInJsonCreator() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithHiddenFieldsInJsonCreator.class);

        final Schema model = models.get("ModelWithHiddenFieldsInJsonCreator");
        assertNotNull(model);
        assertEquals(model.getProperties().size(), 1);

        final Schema idValue = (Schema) model.getProperties().get("id");
        assertTrue(idValue instanceof IntegerSchema);

        final Schema hiddenValue = (Schema) model.getProperties().get("hidden");
        assertNull(hiddenValue);
    }

    static class ModelWithHiddenFieldsInJsonCreator {
        @JsonProperty("id")
        private final Long id;

        @io.swagger.v3.oas.annotations.media.Schema(hidden = true)
        @JsonProperty("hidden")
        private final String hidden;

        @JsonCreator
        public ModelWithHiddenFieldsInJsonCreator(@JsonProperty("id") Long id,
                                                  @JsonProperty("hidden") @io.swagger.v3.oas.annotations.media.Schema(hidden = true) String hidden) {
            this.id = id;
            this.hidden = hidden;
        }

        public Long getId() {
            return id;
        }

        @io.swagger.v3.oas.annotations.media.Schema(hidden = true)
        public String getHidden() {
            return hidden;
        }
    }
}
