package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rootName")
public class ModelWithJAXBAnnotations {
    @XmlAttribute
    public String id;

    @XmlElement(name = "renamed")
    public String name;

    @JsonIgnore
    @XmlAttribute
    public String hidden;

    public List<String> list;

    @XmlElementWrapper
    public List<String> wrappedList;

    @XmlAttribute(name = "doNotUseMe")
    public List<String> forcedElement;
}
