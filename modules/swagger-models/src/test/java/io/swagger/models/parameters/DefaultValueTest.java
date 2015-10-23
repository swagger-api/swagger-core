package io.swagger.models.parameters;

import io.swagger.models.properties.BaseIntegerProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.DecimalProperty;
import io.swagger.models.properties.EmailProperty;
import io.swagger.models.properties.FloatProperty;
import io.swagger.models.properties.LongProperty;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class DefaultValueTest {

    @Test
    public void booleanTypeReturnsDefaultValueAsBoolean() {
        final QueryParameter prop1 = new QueryParameter();
        prop1.setDefaultValue("true");
        prop1.setType(BooleanProperty.TYPE);

        assertTrue(prop1.getDefaultValue() instanceof Boolean);
    }

    @Test
    public void decimalTypeReturnsDefaultValueAsDouble() {
        final QueryParameter prop1 = new QueryParameter();
        prop1.setDefaultValue("1.2");
        prop1.setType(DecimalProperty.TYPE);

        assertTrue(prop1.getDefaultValue() instanceof Double);
    }

    @Test
    public void floatTypeReturnsDefaultValueAsDouble() {
        final QueryParameter prop1 = new QueryParameter();
        prop1.setDefaultValue("1.2");
        prop1.setType(FloatProperty.TYPE);

        assertTrue(prop1.getDefaultValue() instanceof Double);
    }

    @Test
    public void integerTypeReturnsDefaultValueAsLong() {
        final QueryParameter prop1 = new QueryParameter();
        prop1.setDefaultValue("1");
        prop1.setType(BaseIntegerProperty.TYPE);

        assertTrue(prop1.getDefaultValue() instanceof Long);
    }

    @Test
    public void longTypeReturnsDefaultValueAsLong() {
        final QueryParameter prop1 = new QueryParameter();
        prop1.setDefaultValue("1");
        prop1.setType(LongProperty.TYPE);

        assertTrue(prop1.getDefaultValue() instanceof Long);
    }

    @Test
    public void emailTypeReturnsDefaultValueAsString() {
        final QueryParameter prop1 = new QueryParameter();
        prop1.setDefaultValue("email@abc.com");
        prop1.setType(EmailProperty.TYPE);

        assertTrue(prop1.getDefaultValue() instanceof String);
    }

}