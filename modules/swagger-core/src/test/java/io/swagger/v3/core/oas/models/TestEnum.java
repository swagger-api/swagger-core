package io.swagger.v3.core.oas.models;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum(String.class)
public enum TestEnum {
    @XmlEnumValue("PRIVATE") PRIVATE,
    @XmlEnumValue("PUBLIC") PUBLIC,
    @XmlEnumValue("SYSTEM") SYSTEM,
    @XmlEnumValue("INVITE_ONLY") INVITE_ONLY;
}
