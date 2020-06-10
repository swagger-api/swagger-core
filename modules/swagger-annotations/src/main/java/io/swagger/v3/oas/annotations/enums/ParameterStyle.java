package io.swagger.v3.oas.annotations.enums;

public enum ParameterStyle {
    DEFAULT(""),
    MATRIX("matrix"),
    LABEL("label"),
    FORM("form"),
    SPACEDELIMITED("spaceDelimited"),
    PIPEDELIMITED("pipeDelimited"),
    DEEPOBJECT("deepObject"),
    SIMPLE("simple");

    private String value;

    ParameterStyle(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}