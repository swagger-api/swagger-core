package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.oas.models.BeanValidationsModel;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class BeanValidatorTest {

    @Test(description = "read bean validations")
    public void readBeanValidatorTest() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(BeanValidationsModel.class);
        final Schema model = schemas.get("BeanValidationsModel");
        final Map<String, Schema> properties = model.getProperties();

        assertTrue(model.getRequired().contains("id"));
        assertTrue(model.getRequired().contains("username"));

        final StringSchema username = (StringSchema) properties.get("username");
        assertEquals((String) username.getPattern(), "(?![-._])[-._a-zA-Z0-9]{3,32}");

        final IntegerSchema age = (IntegerSchema) properties.get("age");
        assertEquals(age.getMinimum(), new BigDecimal(13.0));
        assertEquals(age.getMaximum(), new BigDecimal(99.0));

        final StringSchema password = (StringSchema) properties.get("password");
        assertEquals((int) password.getMinLength(), 6);
        assertEquals((int) password.getMaxLength(), 20);

        final EmailSchema email = (EmailSchema) properties.get("email");
        assertEquals((String) email.getFormat(), "email");

        final NumberSchema minBalance = (NumberSchema) properties.get("minBalance");
        assertTrue(minBalance.getExclusiveMinimum());

        final NumberSchema maxBalance = (NumberSchema) properties.get("maxBalance");
        assertTrue(maxBalance.getExclusiveMaximum());

        final ArraySchema items = (ArraySchema) properties.get("items");
        assertEquals((int) items.getMinItems(), 2);
        assertEquals((int) items.getMaxItems(), 10);
        assertEquals((int) items.getItems().getMinLength(), 3);
        assertEquals((int) items.getItems().getMaxLength(), 4);

        final StringSchema optionalValue = (StringSchema) properties.get("optionalValue");
        assertEquals((int) optionalValue.getMinLength(), 1);
        assertEquals((int) optionalValue.getMaxLength(), 10);

        final NumberSchema positiveValue = (NumberSchema) properties.get("positiveValue");
        assertEquals(positiveValue.getMinimum(), BigDecimal.ZERO);
        assertTrue(positiveValue.getExclusiveMinimum());

        final NumberSchema positiveOrZeroValue = (NumberSchema) properties.get("positiveOrZeroValue");
        assertEquals(positiveOrZeroValue.getMinimum(), BigDecimal.ZERO);

        final NumberSchema negativeValue = (NumberSchema) properties.get("negativeValue");
        assertEquals(negativeValue.getMaximum(), BigDecimal.ZERO);
        assertTrue(negativeValue.getExclusiveMaximum());

        final NumberSchema negativeOrZeroValue = (NumberSchema) properties.get("negativeOrZeroValue");
        assertEquals(negativeOrZeroValue.getMaximum(), BigDecimal.ZERO);

    }
}
