package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.models.BeanValidationsModel;
import io.swagger.models.Model;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class BeanValidatorTest {

    @Test(description = "read bean validations")
    public void readBeanValidatorTest() {
        final Map<String, Model> schemas = ModelConverters.getInstance().readAll(BeanValidationsModel.class);
        final Model model = schemas.get("BeanValidationsModel");
        final Map<String, Property> properties = model.getProperties();

        final IntegerProperty age = (IntegerProperty) properties.get("age");
        Assert.assertEquals(age.getMinimum(), 13.0);
        Assert.assertEquals(age.getMaximum(), 99.0);

        final StringProperty password = (StringProperty) properties.get("password");
        Assert.assertEquals((int) password.getMinLength(), 6);
        Assert.assertEquals((int) password.getMaxLength(), 20);

        final StringProperty email= (StringProperty) properties.get("email");
        Assert.assertEquals((String) email.getPattern(), "(.+?)@(.+?)");

        final DoubleProperty minBalance = (DoubleProperty) properties.get("minBalance");
        Assert.assertTrue(minBalance.getExclusiveMinimum());

        final DoubleProperty maxBalance = (DoubleProperty) properties.get("maxBalance");
        Assert.assertTrue(maxBalance.getExclusiveMaximum());
    }
}
