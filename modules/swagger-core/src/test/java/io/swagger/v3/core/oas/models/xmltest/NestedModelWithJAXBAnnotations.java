package io.swagger.v3.core.oas.models.xmltest;


import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RootName")
public class NestedModelWithJAXBAnnotations {
    @XmlAttribute
    public String id;

    @XmlElement(name = "named")
    public String name;

    @XmlElement(name = "SubName")
    public SubModelWithJAXBAnnotations subName;
}
