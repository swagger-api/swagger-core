package io.swagger.v3.jaxrs2.resources.jspecify.nullunmarked;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class Address {
    private String street; // in NullUnmarked package: unspecified nullness
    private @Nullable String city; // explicitly nullable

    @NonNull
    public String address2;

    public Address() {}

    public Address(String street, @Nullable String city) {
        this.street = street;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public @Nullable String getCity() {
        return city;
    }

    public void setCity(@Nullable String city) {
        this.city = city;
    }
}
