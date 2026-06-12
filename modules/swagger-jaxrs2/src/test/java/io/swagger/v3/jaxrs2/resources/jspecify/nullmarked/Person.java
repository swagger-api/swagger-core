package io.swagger.v3.jaxrs2.resources.jspecify.nullmarked;

import io.swagger.v3.jaxrs2.resources.jspecify.nullmarked.nullunmarked.Address;
import org.jspecify.annotations.Nullable;

public class Person {
    private Long id; // default not-null due to @NullMarked on package

    private String firstName; // default not-null
    private @Nullable String lastName; // explicitly nullable

    private Address address; // from NullUnmarked package -> may be nullable inside

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public @Nullable String getLastName() {
        return lastName;
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Department department;
}
