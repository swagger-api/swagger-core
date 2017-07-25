package io.swagger.resolving;

import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.oas.models.media.Schema;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.FileAssert.fail;

public class JodaTest extends SwaggerTestBase {

    @Test
    public void testSimple() throws Exception {
        final ModelConverter mr = modelResolver();
        final Schema model = mr.resolve(ModelWithJodaDateTime.class, new ModelConverterContextImpl(mr), null);
        assertNotNull(model);

        final Map<String, Schema> props = model.getProperties();
        assertEquals(props.size(), 2);

        for (Map.Entry<String, Schema> entry : props.entrySet()) {
            final String name = entry.getKey();
            final Schema prop = entry.getValue();

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
        @io.swagger.oas.annotations.media.Schema(description = "Name!")
        public String name;

        @io.swagger.oas.annotations.media.Schema(description = "creation timestamp", required = true)
        public DateTime createdAt;
    }
}
