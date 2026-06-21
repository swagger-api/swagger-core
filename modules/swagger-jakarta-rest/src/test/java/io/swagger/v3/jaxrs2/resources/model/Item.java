package io.swagger.v3.jaxrs2.resources.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class Item {
    private String name;
    private String value;
    @Schema(example = "Ynl0ZQ==")
    private byte[] bytes;
    @Schema(format = "binary", example = "YmluYXJ5")
    private byte[] binary;

    private byte[] byteNoAnnotation;

    public Item() {
    }
    
    public void setBinary(byte[] binary) {
        this.binary = binary;
    }
    
    public byte[] getBinary() {
        return binary;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    
    public byte[] getBytes() {
        return bytes;
    }

    public void setByteNoAnnotation(byte[] byteNoAnnotation) {
        this.byteNoAnnotation = byteNoAnnotation;
    }

    public byte[] getByteNoAnnotation() {
        return byteNoAnnotation;
    }
}
