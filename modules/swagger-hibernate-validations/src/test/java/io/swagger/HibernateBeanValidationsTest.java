package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.models.HibernateBeanValidationsModel;
import io.swagger.models.Model;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HibernateBeanValidationsTest {

    @Test(description = "it should read hibernate validations")
    public void readHibernateValidations() {
        final Map<String, Model> schemas = ModelConverters.getInstance().readAll(HibernateBeanValidationsModel.class);
        final Map<String, Property> properties = schemas.get("HibernateBeanValidationsModel").getProperties();

        final IntegerProperty age = (IntegerProperty) properties.get("age");
        assertEquals(age.getMinimum().doubleValue(), 13.0, 0.01);
        assertEquals(age.getMaximum().doubleValue(), 99.0, 0.01);

        final StringProperty password = (StringProperty) properties.get("password");
        assertEquals((int) password.getMinLength(), 6);
        assertEquals((int) password.getMaxLength(), 20);

        assertTrue(((DoubleProperty) properties.get("minBalance")).getExclusiveMinimum());
        assertTrue(((DoubleProperty) properties.get("maxBalance")).getExclusiveMaximum());
    }

    @Test
    public void shouldUnderstandNotEmpty() {
        final Map<String, Model> schemas = ModelConverters.getInstance().readAll(HibernateBeanValidationsModel.class);
        final Map<String, Property> properties = schemas.get("HibernateBeanValidationsModel").getProperties();
        final StringProperty notEmptyString = (StringProperty) properties.get("notEmptyString");
        assertEquals((int) notEmptyString.getMinLength(), 1);

        final ArrayProperty notEmptyArray = (ArrayProperty) properties.get("notEmptyArray");
        assertEquals((int) notEmptyArray.getMinItems(), 1);
    }
}
