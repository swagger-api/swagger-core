package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.oas.models.media.DateTimeSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class XMLGregorianCalendarTest {
    @Test(description = "it should read a model with XMLGregorianCalendar")
    public void testXMLGregorianCalendar() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithCalendar.class);
        assertEquals(models.size(), 1); // don't create a Joda DateTime object

        Schema model = models.get("ModelWithCalendar");

        final Map<String, Schema> properties = model.getProperties();

        final Schema nameProperty = properties.get("name");
        assertTrue(nameProperty instanceof StringSchema);
        assertEquals(nameProperty.getDescription(), "name of the model");

        final Schema dateTimeSchema = properties.get("createdAt");
        assertTrue(dateTimeSchema instanceof DateTimeSchema);
        assertTrue(model.getRequired().contains("createdAt"));
        assertEquals(dateTimeSchema.getDescription(), "creation timestamp");
    }

    class ModelWithCalendar {
        @io.swagger.oas.annotations.media.Schema(description = "name of the model")
        public String name;

        @io.swagger.oas.annotations.media.Schema(description = "creation timestamp", required = true)
        public XMLGregorianCalendar createdAt;
    }
}
