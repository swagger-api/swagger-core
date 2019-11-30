package io.swagger.v3.core.oas.models;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum(String.class)
public enum TestSecondEnum {
    @XmlEnumValue("A_PRIVATE") A_PRIVATE,
    @XmlEnumValue("A_PUBLIC") A_PUBLIC,
    @XmlEnumValue("A_SYSTEM") A_SYSTEM,
    @XmlEnumValue("A_INVITE_ONLY") A_INVITE_ONLY;
}
