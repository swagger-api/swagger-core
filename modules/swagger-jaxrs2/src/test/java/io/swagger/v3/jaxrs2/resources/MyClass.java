package io.swagger.v3.jaxrs2.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.xml.bind.annotation.XmlElement;

public class MyClass extends MySuperClass {

    public void populate(final String isotonicDrink, final String softDrink,
                         final String isoDrink, final String isotonicDrinkOnlyXmlElement) {
        this.isotonicDrink = isotonicDrink;
        this.softDrink = softDrink;
        this.isoDrink = isoDrink;
        this.isotonicDrinkOnlyXmlElement = isotonicDrinkOnlyXmlElement;
    }

    @XmlElement(name = "beerDrink")
    @JsonProperty("beerDrink")
    private String isotonicDrink;

    @XmlElement(name = "sugarDrink")
    private String softDrink;

    @Schema(name = "saltDrink")
    private String isoDrink;

    @XmlElement(name = "beerDrinkXmlElement")
    private String isotonicDrinkOnlyXmlElement;

}
