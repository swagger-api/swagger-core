package io.swagger.jackson;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.FileAssert.fail;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;

import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.util.Map;

public class JodaTest extends SwaggerTestBase {

    @Test
    public void testSimple() throws Exception {
        final ModelConverter mr = modelResolver();
        final Model model = mr.resolve(ModelWithJodaDateTime.class, new ModelConverterContextImpl(mr), null);
        assertNotNull(model);

        final Map<String, Property> props = model.getProperties();
        assertEquals(props.size(), 2);

        for (Map.Entry<String, Property> entry : props.entrySet()) {
            final String name = entry.getKey();
            final Property prop = entry.getValue();

            if ("name".equals(name)) {
                assertEquals(prop.getType(), "string");
            } else if ("createdAt".equals(name)) {
                assertEquals(prop.getType(), "string");
                assertEquals(prop.getFormat(), "date-time");
            } else {
                fail(String.format("Unknown property '%s'", name));
            }
        }
    }

    static class ModelWithJodaDateTime {
        @ApiModelProperty(value = "Name!", position = 2)
        public String name;

        @ApiModelProperty(value = "creation timestamp", required = true, position = 1)
        public DateTime createdAt;
    }
}
