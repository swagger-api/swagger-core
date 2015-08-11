package io.swagger.models;

public class Model1155 {
    private boolean valid;
    private String value;
    public boolean is;
    public String get;
    public boolean isA;
    public String getA;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // jackson treats this as getter
    public boolean is_persistent() {
        return true;
    }

    // jackson treats this as getter
    public String gettersAndHaters() {
        return null;
    }

    // jackson doesn't treat this as getter
    boolean isometric() {
        return true;
    }
}
