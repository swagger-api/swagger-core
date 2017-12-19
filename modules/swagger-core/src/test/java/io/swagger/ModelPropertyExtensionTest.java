package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.testng.annotations.Test;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

public class ModelPropertyExtensionTest {
    @Test(description = "it should parse extensions on a model property")
    public void testHiddenField() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ModelWithHiddenFields.class);

        final Model model = models.get("ModelWithHiddenFields");
        assertNotNull(model);
        assertEquals(model.getProperties().size(), 3);

        final Property idValue = model.getProperties().get("id");
        assertTrue(idValue instanceof LongProperty);
        assertTrue(idValue.getRequired());

        final Property nameValue = model.getProperties().get("name");
        assertTrue(nameValue instanceof StringProperty);
        assertEquals(nameValue.getVendorExtensions(), Collections.<String, Object>emptyMap());


        final Property extendedValue = model.getProperties().get("extended");
        assertTrue(extendedValue instanceof StringProperty);
        assertEquals(extendedValue.getVendorExtensions().get("x-proprietary"), "corporate");
    }

    class ModelWithHiddenFields {
        @ApiModelProperty(required = true)
        public Long id = null;

        @ApiModelProperty(required = true, hidden = false, extensions = @Extension(properties = {}))
        public String name = null;

        @ApiModelProperty(required = true, extensions = @Extension(properties = {@ExtensionProperty(name = "x-proprietary", value = "corporate")}))
        public String extended = null;
    }
}
