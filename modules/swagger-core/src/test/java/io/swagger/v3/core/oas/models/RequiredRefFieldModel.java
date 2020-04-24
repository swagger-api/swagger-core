package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequiredRefFieldModel {

    @XmlElement(name = "a")
    @Schema(required = true)
    public String getA() {
        return "aaa";
    }

    @XmlElement(name = "b")
    @Schema(required = true)
    public B getB() {
        return null;
    }

    static class B {
        public String foo;
    }
}
