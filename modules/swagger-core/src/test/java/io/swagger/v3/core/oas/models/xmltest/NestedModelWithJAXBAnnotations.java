package io.swagger.v3.core.oas.models.xmltest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "RootName")
public class NestedModelWithJAXBAnnotations {
    @XmlAttribute
    public String id;

    @XmlElement(name = "named")
    public String name;

    @XmlElement(name = "SubName")
    public SubModelWithJAXBAnnotations subName;
}
