package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

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
