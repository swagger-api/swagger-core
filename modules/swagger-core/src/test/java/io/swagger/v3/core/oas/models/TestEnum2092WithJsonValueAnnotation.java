package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum
public enum TestEnum2092WithJsonValueAnnotation {
    @XmlEnumValue("xmlPrivate")
    @JsonProperty("jsonPrivate")
    A_PRIVATE("private"),

    @XmlEnumValue("xmlPublic")
    A_PUBLIC("public"),

    @JsonProperty("jsonSystem")
    A_SYSTEM("system"),

    @XmlEnumValue("invite only")
    A_INVITE_ONLY("invite"),

    @XmlEnumValue("none")
    NULL_VALUE(null);

    private final String value;

    TestEnum2092WithJsonValueAnnotation(String value) {
        this.value = value;
    }

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}

