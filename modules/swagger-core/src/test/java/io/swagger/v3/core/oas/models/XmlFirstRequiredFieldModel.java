package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(name = "aaa")
public class XmlFirstRequiredFieldModel {
    @XmlElement(name = "a")
    @Schema(description = "bla", required = true)
    public String getA() {
        return "aaa";
    }

    public String getC() {
        return "kkk";
    }
}
