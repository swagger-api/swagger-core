package io.swagger.v3.core.oas.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ModelWithJaxBDefaultValues {
    @XmlElement(defaultValue = "Tony")
    public String name;

    @XmlElement(defaultValue = "100")
    public Integer age;

}