package com.wordnik.swagger.test.jaxrs.apt;

/**
 * Just a sample bean
 * @author Heiko W. Rupp
 */
public class FooBean {

    int id;
    String name;
    double value;

    public FooBean(int id, String name, double value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
