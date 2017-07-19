package io.swagger.resolving;

import io.swagger.converter.ModelConverters;
import io.swagger.oas.models.media.DateTimeSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class JodaDateTimeConverterTest {

    @Test
    public void testJodaDateTime() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithJodaDateTime.class);
        assertEquals(models.size(), 1); // don't create a Joda DateTime object

        final Schema model = models.get("ModelWithJodaDateTime");

        final Schema dateTimeProperty = (Schema)model.getProperties().get("createdAt");
        assertTrue(dateTimeProperty instanceof DateTimeSchema);
        assertTrue(model.getRequired().contains("createdAt"));
        assertEquals(dateTimeProperty.getDescription(), "creation timestamp");

        final Schema nameProperty = (Schema)model.getProperties().get("name");
        assertTrue(nameProperty instanceof StringSchema);
        assertEquals(nameProperty.getDescription(), "name of the model");
    }

    class ModelWithJodaDateTime{

        @io.swagger.oas.annotations.media.Schema(description = "name of the model")
        public String name;

        @io.swagger.oas.annotations.media.Schema(description = "creation timestamp", required = true)
        public DateTime createdAt;
    }

}
