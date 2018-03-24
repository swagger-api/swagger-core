package io.swagger.v3.jaxrs2.resources;

import javax.xml.bind.annotation.XmlElement;

public class MyClass extends MySuperClass {

    @XmlElement(name = "beerDrink")
    public String isotonicDrink;

    @XmlElement(name = "sugarDrink")
    public String softDrink;


}
