package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

import org.testng.annotations.Test;

import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

public class XMLGregorianCalendarTest {

    @Test(description = "it should read a model with XMLGregorianCalendar")
    public void testXMLGregorianCalendar() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ModelWithCalendar.class);
        assertEquals(models.size(), 1); // don't create a Joda DateTime object

        final Map<String, Property> properties = models.get("ModelWithCalendar").getProperties();

        final Property nameProperty = properties.get("name");
        assertTrue(nameProperty instanceof StringProperty);
        assertEquals((int) nameProperty.getPosition(), 2);
        assertEquals(nameProperty.getDescription(), "name of the model");

        final Property dateTimeProperty = properties.get("createdAt");
        assertTrue(dateTimeProperty instanceof DateTimeProperty);
        assertEquals((int) dateTimeProperty.getPosition(), 1);
        assertTrue(dateTimeProperty.getRequired());
        assertEquals(dateTimeProperty.getDescription(), "creation timestamp");
    }

    class ModelWithCalendar {
        @ApiModelProperty(value = "name of the model", position = 2)
        public String name;

        @ApiModelProperty(value = "creation timestamp", required = true, position = 1)
        public XMLGregorianCalendar createdAt;
    }
}
