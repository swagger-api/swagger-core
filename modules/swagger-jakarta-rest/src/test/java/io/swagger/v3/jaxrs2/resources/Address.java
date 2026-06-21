package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.DependentRequired;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        _if = Address.AnnotatedCountry.class,
        then = PostalCodeNumberPattern.class,
        _else = PostalCodePattern.class,
        propertyNames = Address.PropertyNamesPattern.class,
        dependentRequiredMap = {
                @DependentRequired(
                        name = "street",
                        value = { "country" }
                )
        }
)
public class Address {

    private String street;
    private CountryEnum country;

    public enum CountryEnum {
        UNITED_STATES_OF_AMERICA("United States of America"),
        CANADA("Canada");

        private String value;

        CountryEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static CountryEnum fromValue(String value) {
            for (CountryEnum b : CountryEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public CountryEnum getCountry() {
        return country;
    }

    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    class AnnotatedCountry {

        private Object country;

        @Schema(
                _const = "United States"

        )
        public Object getCountry() {
            return country;
        }

        public void setCountry(Object country) {
            this.country = country;
        }
    }

    @Schema(
            pattern = "^[A-Za-z_][A-Za-z0-9_]*$"
    )
    class PropertyNamesPattern {
    }
}
