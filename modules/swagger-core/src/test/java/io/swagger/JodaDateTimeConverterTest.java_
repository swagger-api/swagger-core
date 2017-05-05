package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.util.Map;

public class JodaDateTimeConverterTest {

    @Test
    public void testJodaDateTime() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ModelWithJodaDateTime.class);
        assertEquals(models.size(), 1); // don't create a Joda DateTime object

        final Model model = models.get("ModelWithJodaDateTime");

        final Property dateTimeProperty = model.getProperties().get("createdAt");
        assertTrue(dateTimeProperty instanceof DateTimeProperty);
        assertEquals((int)dateTimeProperty.getPosition(), 1);
        assertTrue(dateTimeProperty.getRequired());
        assertEquals(dateTimeProperty.getDescription(), "creation timestamp");

        final Property nameProperty = model.getProperties().get("name");
        assertTrue(nameProperty instanceof StringProperty);
        assertEquals((int)nameProperty.getPosition(), 2);
        assertEquals(nameProperty.getDescription(), "name of the model");
    }

    class ModelWithJodaDateTime{
        @ApiModelProperty(value = "name of the model", position = 2)
        public String name;

        @ApiModelProperty(value = "creation timestamp", required = true, position = 1)
        public DateTime createdAt;
    }

}
