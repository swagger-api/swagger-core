package io.swagger.v3.jaxrs2.resources;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

public class MyOtherClass extends MySuperClass {

    public void populate(final String myPropertyName) {
        this.myProperty = myPropertyName;
    }

    @XmlElement(name = "MyPrOperTyName")
    @XmlAttribute(name = "MyPrOperTyName")
    private String myProperty;

}
