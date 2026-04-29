package io.swagger.v3.jaxrs2.resources.jspecify.nullmarked;

import io.swagger.v3.jaxrs2.resources.jspecify.nullmarked.nullunmarked.Address;

public class Department {
    private Long id; // default not-null due to @NullMarked on package
    private Address address; // from NullUnmarked package -> may be nullable inside

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
