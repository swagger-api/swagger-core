package io.swagger.models;

import javax.xml.bind.annotation.XmlElement;

public class Address {
    private Integer streetNumber;

    @XmlElement(name = "streetNumber")
    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }
}
