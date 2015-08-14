package io.swagger.resources.generics;

import io.swagger.annotations.ApiModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "UserEntity")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserEntity extends AbstractEntity {
    public String name;
}
