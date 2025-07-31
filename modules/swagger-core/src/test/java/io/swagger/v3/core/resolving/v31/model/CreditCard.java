package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public class CreditCard {

    private String billingAddress;

    private Set<Address.CountryEnum> acceptingCountries;

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    @ArraySchema(schema = @Schema(description = "accepting country"))
    public Set<Address.CountryEnum> getAcceptingCountries() {
        return acceptingCountries;
    }

    public void setAcceptingCountries(Set<Address.CountryEnum> acceptingCountries) {
        this.acceptingCountries = acceptingCountries;
    }
}
