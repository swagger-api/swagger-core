package io.swagger.oas.models;

import java.util.HashSet;

public class Manufacturers {
    private HashSet<String> countries;

    public HashSet<String> getCountries() {
        return countries;
    }

    public void setCountries(HashSet<String> countries) {
        this.countries = countries;
    }
}
