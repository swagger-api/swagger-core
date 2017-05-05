package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

import org.joda.time.LocalDate;
import org.testng.annotations.Test;

import java.util.Map;

public class JodaLocalDateConverterTest {

    @Test
    public void testJodaLocalDate() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ModelWithJodaLocalDate.class);
        assertEquals(models.size(), 1);

        final Model model = models.get("ModelWithJodaLocalDate");

        final Property dateTimeProperty = model.getProperties().get("createdAt");
        assertTrue(dateTimeProperty instanceof DateProperty);
        assertEquals((int) dateTimeProperty.getPosition(), 1);
        assertTrue(dateTimeProperty.getRequired());
        assertEquals(dateTimeProperty.getDescription(), "creation localDate");

        final Property nameProperty = model.getProperties().get("name");
        assertTrue(nameProperty instanceof StringProperty);
        assertEquals((int) nameProperty.getPosition(), 2);
        assertEquals(nameProperty.getDescription(), "name of the model");
    }

    class ModelWithJodaLocalDate {
        @ApiModelProperty(value = "name of the model", position = 2)
        public String name;

        @ApiModelProperty(value = "creation localDate", required = true, position = 1)
        public LocalDate createdAt;
    }
}
