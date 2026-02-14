package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class BeanValidationTest {

    @Test(description = "read javax sign constraints")
    public void readJavaxSignConstraints() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(JavaxSignConstraintsModel.class);
        final Schema model = schemas.get("JavaxSignConstraintsModel");
        final Map<String, Schema> properties = model.getProperties();

        final Schema positive = properties.get("positive");
        assertEquals(positive.getMinimum(), BigDecimal.ZERO);
        assertEquals(positive.getExclusiveMinimum(), Boolean.TRUE);

        final Schema negative = properties.get("negative");
        assertEquals(negative.getMaximum(), BigDecimal.ZERO);
        assertEquals(negative.getExclusiveMaximum(), Boolean.TRUE);

        final Schema positiveOrZero = properties.get("positiveOrZero");
        assertEquals(positiveOrZero.getMinimum(), BigDecimal.ZERO);
        assertEquals(positiveOrZero.getExclusiveMinimum(), Boolean.FALSE);

        final Schema negativeOrZero = properties.get("negativeOrZero");
        assertEquals(negativeOrZero.getMaximum(), BigDecimal.ZERO);
        assertEquals(negativeOrZero.getExclusiveMaximum(), Boolean.FALSE);
    }

    @Test(description = "read javax sign constraints with stronger min and max")
    public void readJavaxSignConstraintsWithStrongerBounds() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(JavaxSignAndRangeConstraintsModel.class);
        final Schema model = schemas.get("JavaxSignAndRangeConstraintsModel");
        final Map<String, Schema> properties = model.getProperties();

        final Schema positiveWithMin = properties.get("positiveWithMin");
        assertEquals(positiveWithMin.getMinimum(), new BigDecimal("5"));
        assertNull(positiveWithMin.getExclusiveMinimum());

        final Schema negativeWithMax = properties.get("negativeWithMax");
        assertEquals(negativeWithMax.getMaximum(), new BigDecimal("-5"));
        assertNull(negativeWithMax.getExclusiveMaximum());
    }

    public static class JavaxSignConstraintsModel {
        @Positive
        protected Integer positive;

        @Negative
        protected Integer negative;

        @PositiveOrZero
        protected Integer positiveOrZero;

        @NegativeOrZero
        protected Integer negativeOrZero;

        public Integer getPositive() {
            return positive;
        }

        public void setPositive(Integer positive) {
            this.positive = positive;
        }

        public Integer getNegative() {
            return negative;
        }

        public void setNegative(Integer negative) {
            this.negative = negative;
        }

        public Integer getPositiveOrZero() {
            return positiveOrZero;
        }

        public void setPositiveOrZero(Integer positiveOrZero) {
            this.positiveOrZero = positiveOrZero;
        }

        public Integer getNegativeOrZero() {
            return negativeOrZero;
        }

        public void setNegativeOrZero(Integer negativeOrZero) {
            this.negativeOrZero = negativeOrZero;
        }
    }

    public static class JavaxSignAndRangeConstraintsModel {
        @Positive
        @Min(5)
        protected Integer positiveWithMin;

        @Negative
        @Max(-5)
        protected Integer negativeWithMax;

        public Integer getPositiveWithMin() {
            return positiveWithMin;
        }

        public void setPositiveWithMin(Integer positiveWithMin) {
            this.positiveWithMin = positiveWithMin;
        }

        public Integer getNegativeWithMax() {
            return negativeWithMax;
        }

        public void setNegativeWithMax(Integer negativeWithMax) {
            this.negativeWithMax = negativeWithMax;
        }
    }
}
