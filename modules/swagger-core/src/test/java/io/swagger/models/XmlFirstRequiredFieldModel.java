package io.swagger.models;

import io.swagger.annotations.media.OASSchema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@OASSchema(title = "aaa")
public class XmlFirstRequiredFieldModel {
    @XmlElement(name = "a")
    @OASSchema(description = "bla", required = true)
    public String getA() {
        return "aaa";
    }

    public String getC() {
        return "kkk";
    }
}
