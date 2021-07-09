package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum
public enum TestEnum2092 {
    @XmlEnumValue("xmlPrivate")
    @JsonProperty("jsonPrivate")
    A_PRIVATE,
    
    @XmlEnumValue("xmlPublic")
    A_PUBLIC,
    
    @JsonProperty("jsonSystem")
    A_SYSTEM,
    
    @XmlEnumValue("invite only")
    A_INVITE_ONLY,

    A_NOTHING
}
