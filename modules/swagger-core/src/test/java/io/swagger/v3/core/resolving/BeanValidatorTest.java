package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.oas.models.BeanValidationsModel;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

public class BeanValidatorTest {

    @Test(description = "read bean validations")
    public void readBeanValidatorTest() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(BeanValidationsModel.class);
        final Schema model = schemas.get("BeanValidationsModel");
        final Map<String, Schema> properties = model.getProperties();

        Assert.assertTrue(model.getRequired().contains("id"));

        final IntegerSchema age = (IntegerSchema) properties.get("age");
        Assert.assertEquals(age.getMinimum(), new BigDecimal(13.0));
        Assert.assertEquals(age.getMaximum(), new BigDecimal(99.0));

        final StringSchema password = (StringSchema) properties.get("password");
        Assert.assertEquals((int) password.getMinLength(), 6);
        Assert.assertEquals((int) password.getMaxLength(), 20);

        final StringSchema email= (StringSchema) properties.get("email");
        Assert.assertEquals((String) email.getPattern(), "(.+?)@(.+?)");

        final NumberSchema minBalance = (NumberSchema) properties.get("minBalance");
        Assert.assertTrue(minBalance.getExclusiveMinimum());

        final NumberSchema maxBalance = (NumberSchema) properties.get("maxBalance");
        Assert.assertTrue(maxBalance.getExclusiveMaximum());

        final ArraySchema items = (ArraySchema) properties.get("items");
        Assert.assertEquals((int) items.getMinItems(), 2);
        Assert.assertEquals((int) items.getMaxItems(), 10);
    }
}
