package io.swagger.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "aaa")
public class XmlFirstRequiredFieldModel {
    @XmlElement(name = "a")
    @ApiModelProperty(value = "bla", required = true)
    public String getA() {
        return "aaa";
    }

    public String getC() {
        return "kkk";
    }
}
