package io.swagger.models;

import java.util.Date;
import java.util.Map;

public class Person {
    private Long id;
    private String firstName;
    private Address address;
    private Map<String, String> properties;
    private Date birthDate;
    private Float floatValue;
    private Double doubleValue;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Float getFloat() {
        return floatValue;
    }

    public void setFloat(Float floatValue) {
        this.floatValue = floatValue;
    }

    public Double getDouble() {
        return doubleValue;
    }

    public void setDouble(Double doubleValue) {
        this.doubleValue = doubleValue;
    }
}
