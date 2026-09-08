package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.*;

public class HiddenFieldTest {

    @BeforeMethod
    public void resetSingletons() {
        // Singletons need to be reset due to configuration changes between tests
        ModelConverters.reset();
    }

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

    @Test(description = "it should include hidden fields when ignoreHidden is true")
    public void testIgnoreHiddenConfiguration() {
        Configuration configuration = new Configuration()
                .ignoreHidden(Boolean.TRUE);

        final Map<String, Schema> models = ModelConverters.getInstance(configuration).read(ModelWithHiddenFields.class);

        final Schema model = models.get("ModelWithHiddenFields");
        assertNotNull(model);
        assertEquals(model.getProperties().size(), 3);

        final Schema idValue = (Schema) model.getProperties().get("id");
        assertTrue(idValue instanceof IntegerSchema);

        assertTrue(model.getRequired().contains("id"));

        final Schema nameValue = (Schema) model.getProperties().get("name");
        assertTrue(nameValue instanceof StringSchema);

        // password field should now be included when ignoreHidden is true
        final Schema passwordValue = (Schema) model.getProperties().get("password");
        assertNotNull(passwordValue);
        assertTrue(passwordValue instanceof StringSchema);
    }

    @Test(description = "it should include hidden fields in @JsonCreator when ignoreHidden is true")
    public void testIgnoreHiddenConfigurationWithJsonCreator() {
        Configuration configuration = new Configuration()
                .ignoreHidden(Boolean.TRUE);

        final Map<String, Schema> models = ModelConverters.getInstance(configuration).read(ModelWithHiddenFieldsInJsonCreator.class);

        final Schema model = models.get("ModelWithHiddenFieldsInJsonCreator");
        assertNotNull(model);
        assertEquals(model.getProperties().size(), 2);

        final Schema idValue = (Schema) model.getProperties().get("id");
        assertTrue(idValue instanceof IntegerSchema);

        // hidden field should now be included when ignoreHidden is true
        final Schema hiddenValue = (Schema) model.getProperties().get("hidden");
        assertNotNull(hiddenValue);
        assertTrue(hiddenValue instanceof StringSchema);
    }
}
