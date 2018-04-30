package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.oas.models.media.Schema;
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
        final Schema model = mr.resolve(new AnnotatedType(ModelWithJodaDateTime.class), new ModelConverterContextImpl(mr), null);
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
        @io.swagger.v3.oas.annotations.media.Schema(description = "Name!")
        public String name;

        @io.swagger.v3.oas.annotations.media.Schema(description = "creation timestamp", required = true)
        public DateTime createdAt;
    }
}
