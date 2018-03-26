package io.swagger.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class MyOtherClass extends MySuperClass {

    public void populate(final String myPropertyName) {
        this.myProperty = myPropertyName;
    }

    @XmlElement(name = "MyPrOperTyName")
    @XmlAttribute(name = "MyPrOperTyName")
    private String myProperty;

}
