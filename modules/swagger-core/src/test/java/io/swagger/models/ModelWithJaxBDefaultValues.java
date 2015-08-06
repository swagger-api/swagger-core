package io.swagger.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ModelWithJaxBDefaultValues {
    @XmlElement(defaultValue = "Tony")
    public String name;

    @XmlElement(defaultValue = "100")
    public Integer age;

}