package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.oas.models.BeanValidationsModel;
import io.swagger.v3.oas.models.media.*;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class BeanValidatorTest {

    @Test(description = "read bean validations")
    public void readBeanValidatorOAS30Test() {
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

        final NumberSchema positiveAmount = (NumberSchema) properties.get("positiveAmount");
        assertEquals(positiveAmount.getMinimum(), BigDecimal.ZERO);
        assertTrue(positiveAmount.getExclusiveMinimum());

        final NumberSchema positiveOrZeroAmount = (NumberSchema) properties.get("positiveOrZeroAmount");
        assertEquals(positiveOrZeroAmount.getMinimum(), BigDecimal.ZERO);
        assertNull(positiveOrZeroAmount.getExclusiveMinimum());

        final NumberSchema negativeAmount = (NumberSchema) properties.get("negativeAmount");
        assertEquals(negativeAmount.getMaximum(), BigDecimal.ZERO);
        assertTrue(negativeAmount.getExclusiveMaximum());

        final NumberSchema negativeOrZeroAmount = (NumberSchema) properties.get("negativeOrZeroAmount");
        assertEquals(negativeOrZeroAmount.getMaximum(), BigDecimal.ZERO);
        assertNull(negativeOrZeroAmount.getExclusiveMaximum());

        final NumberSchema positiveWithMin = (NumberSchema) properties.get("positiveWithMin");
        assertEquals(positiveWithMin.getMinimum(), new BigDecimal("5"));
        assertNull(positiveWithMin.getExclusiveMinimum());

        final NumberSchema positiveWithDecimalMin = (NumberSchema) properties.get("positiveWithDecimalMin");
        assertEquals(positiveWithDecimalMin.getMinimum(), new BigDecimal("5.5"));
        assertTrue(positiveWithDecimalMin.getExclusiveMinimum());

        final NumberSchema positiveOrZeroWithMin = (NumberSchema) properties.get("positiveOrZeroWithMin");
        assertEquals(positiveOrZeroWithMin.getMinimum(), new BigDecimal("3"));
        assertNull(positiveOrZeroWithMin.getExclusiveMinimum());

        final NumberSchema negativeWithMax = (NumberSchema) properties.get("negativeWithMax");
        assertEquals(negativeWithMax.getMaximum(), new BigDecimal("-3"));
        assertNull(negativeWithMax.getExclusiveMaximum());

        final NumberSchema negativeOrZeroWithMax = (NumberSchema) properties.get("negativeOrZeroWithMax");
        assertEquals(negativeOrZeroWithMax.getMaximum(), new BigDecimal("-2"));
        assertNull(negativeOrZeroWithMax.getExclusiveMaximum());
    }

    @Test(description = "read bean validations")
    public void readBeanValidatorOAS31Test() {
        final Map<String, Schema> schemas = ModelConverters.getInstance(true).readAll(BeanValidationsModel.class);
        final Schema model = schemas.get("BeanValidationsModel");
        final Map<String, Schema> properties = model.getProperties();

        assertTrue(model.getRequired().contains("id"));
        assertTrue(model.getRequired().contains("username"));

        final JsonSchema username = (JsonSchema) properties.get("username");
        assertEquals(username.getPattern(), "(?![-._])[-._a-zA-Z0-9]{3,32}");

        final JsonSchema age = (JsonSchema) properties.get("age");
        assertEquals(age.getMinimum(), new BigDecimal(13.0));
        assertEquals(age.getMaximum(), new BigDecimal(99.0));

        final JsonSchema password = (JsonSchema) properties.get("password");
        assertEquals((int) password.getMinLength(), 6);
        assertEquals((int) password.getMaxLength(), 20);

        final JsonSchema email = (JsonSchema) properties.get("email");
        assertEquals(email.getFormat(), "email");

        final JsonSchema minBalance = (JsonSchema) properties.get("minBalance");
        assertTrue(minBalance.getExclusiveMinimum());

        final JsonSchema maxBalance = (JsonSchema) properties.get("maxBalance");
        assertTrue(maxBalance.getExclusiveMaximum());

        final JsonSchema items = (JsonSchema) properties.get("items");
        assertEquals((int) items.getMinItems(), 2);
        assertEquals((int) items.getMaxItems(), 10);
        assertEquals((int) items.getItems().getMinLength(), 3);
        assertEquals((int) items.getItems().getMaxLength(), 4);

        final JsonSchema optionalValue = (JsonSchema) properties.get("optionalValue");
        assertEquals((int) optionalValue.getMinLength(), 1);
        assertEquals((int) optionalValue.getMaxLength(), 10);

        final JsonSchema positiveAmount = (JsonSchema) properties.get("positiveAmount");
        assertEquals(positiveAmount.getExclusiveMinimumValue(), BigDecimal.ZERO);

        final JsonSchema positiveOrZeroAmount = (JsonSchema) properties.get("positiveOrZeroAmount");
        assertEquals(positiveOrZeroAmount.getMinimum(), BigDecimal.ZERO);
        assertNull(positiveOrZeroAmount.getExclusiveMinimum());

        final JsonSchema negativeAmount = (JsonSchema) properties.get("negativeAmount");
        assertEquals(negativeAmount.getExclusiveMaximumValue(), BigDecimal.ZERO);

        final JsonSchema negativeOrZeroAmount = (JsonSchema) properties.get("negativeOrZeroAmount");
        assertEquals(negativeOrZeroAmount.getMaximum(), BigDecimal.ZERO);
        assertNull(negativeOrZeroAmount.getExclusiveMaximum());

        final JsonSchema positiveWithMin = (JsonSchema) properties.get("positiveWithMin");
        assertEquals(positiveWithMin.getMinimum(), new BigDecimal("5"));
        assertNull(positiveWithMin.getExclusiveMinimum());
        assertNull(positiveWithMin.getExclusiveMinimumValue());

        final JsonSchema positiveWithDecimalMin = (JsonSchema) properties.get("positiveWithDecimalMin");
        assertEquals(positiveWithDecimalMin.getMinimum(), new BigDecimal("5.5"));
        assertNull(positiveWithMin.getExclusiveMinimum());
        assertNull(positiveWithMin.getExclusiveMinimumValue());

        final JsonSchema positiveOrZeroWithMin = (JsonSchema) properties.get("positiveOrZeroWithMin");
        assertEquals(positiveOrZeroWithMin.getMinimum(), new BigDecimal("3"));
        assertNull(positiveWithMin.getExclusiveMinimum());
        assertNull(positiveWithMin.getExclusiveMinimumValue());

        final JsonSchema negativeWithMax = (JsonSchema) properties.get("negativeWithMax");
        assertEquals(negativeWithMax.getMaximum(), new BigDecimal("-3"));
        assertNull(positiveWithMin.getExclusiveMaximum());
        assertNull(positiveWithMin.getExclusiveMaximumValue());

        final JsonSchema negativeOrZeroWithMax = (JsonSchema) properties.get("negativeOrZeroWithMax");
        assertEquals(negativeOrZeroWithMax.getMaximum(), new BigDecimal("-2"));
        assertNull(positiveWithMin.getExclusiveMaximum());
        assertNull(positiveWithMin.getExclusiveMaximumValue());
    }
}
