package io.swagger.v3.core.resolving.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Schema(name = "TestObject2616", description = "Nutritional value specification")
public class TestObject2915 implements Serializable {
    private final static long serialVersionUID = 1L;
    private String name;

    private QuantitativeValueDTO perServing;
    private QuantitativeValueDTO per100Gram;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuantitativeValueDTO getPerServing() {
        return perServing;
    }

    public void setPerServing(QuantitativeValueDTO perServing) {
        this.perServing = perServing;
    }

    public QuantitativeValueDTO getPer100Gram() {
        return per100Gram;
    }

    public void setPer100Gram(QuantitativeValueDTO per100Gram) {
        this.per100Gram = per100Gram;
    }

    @Schema(name = "QuantitativeValue", description = "A combination of a value and associated unit")
    public class QuantitativeValueDTO implements Serializable {

        private final static long serialVersionUID = 1L;

        @NotNull
        private double value;

        private String unitText;
        private String unitCode;

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getUnitText() {
            return unitText;
        }

        public void setUnitText(String unitText) {
            this.unitText = unitText;
        }

        public String getUnitCode() {
            return unitCode;
        }

        public void setUnitCode(String unitCode) {
            this.unitCode = unitCode;
        }
    }
}
