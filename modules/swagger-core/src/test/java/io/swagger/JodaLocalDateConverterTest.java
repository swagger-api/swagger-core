package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.oas.models.media.DateSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import org.joda.time.LocalDate;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class JodaLocalDateConverterTest {

    @Test
    public void testJodaLocalDate() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithJodaLocalDate.class);
        assertEquals(models.size(), 1);

        final Schema model = models.get("ModelWithJodaLocalDate");

        final Schema dateTimeProperty = (Schema)model.getProperties().get("createdAt");
        assertTrue(dateTimeProperty instanceof DateSchema);
        // TODO
//        assertEquals((int) dateTimeProperty.getPosition(), 1);
        // TODO
//        assertTrue(dateTimeProperty.getRequired());
        assertEquals(dateTimeProperty.getDescription(), "creation localDate");

        final Schema nameProperty = (Schema)model.getProperties().get("name");
        assertTrue(nameProperty instanceof StringSchema);
        // TODO
//        assertEquals((int) nameProperty.getPosition(), 2);
        assertEquals(nameProperty.getDescription(), "name of the model");
    }

    class ModelWithJodaLocalDate {
        @io.swagger.oas.annotations.media.Schema(description = "name of the model"/*, position = 2*/)
        public String name;

        @io.swagger.oas.annotations.media.Schema(description = "creation localDate", required = true/*, position = 1*/)
        public LocalDate createdAt;
    }
}
