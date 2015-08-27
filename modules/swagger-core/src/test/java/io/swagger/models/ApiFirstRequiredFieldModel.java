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
public class ApiFirstRequiredFieldModel {
    @ApiModelProperty(value = "bla", required = true)
    @XmlElement(name = "a")
    public String getA() {
        return "aaa";
    }

    public String getC() {
        return "kkk";
    }
}
