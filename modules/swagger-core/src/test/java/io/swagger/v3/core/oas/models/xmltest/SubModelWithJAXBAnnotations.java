package io.swagger.v3.core.oas.models.xmltest;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubType", propOrder = {
        "id",
        "name"
})
public class SubModelWithJAXBAnnotations {
    @XmlAttribute
    public String id;

    @XmlElement
    public String name;
}
