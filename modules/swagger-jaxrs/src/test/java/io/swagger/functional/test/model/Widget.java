package io.swagger.functional.test.model;

/**
 * Created by rbolles on 2/16/16.
 */
public class Widget {

    private String a;
    private String b;
    private String id;

    public String getA() {
        return a;
    }

    public Widget setA(String a) {
        this.a = a;
        return this;
    }

    public String getB() {
        return b;
    }

    public Widget setB(String b) {
        this.b = b;
        return this;
    }

    public String getId() {
        return id;
    }

    public Widget setId(String id) {
        this.id = id;
        return this;
    }
}
