package io.swagger.v3.jaxrs2.petstore31;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Schema;

@XmlRootElement(name = "Tag")
public class Tag {
    private long id;
    private String name;

    @XmlElement(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Schema(
            properties = {
                    @StringToClassMapItem(key = "foo", value =  Foo.class),
                    @StringToClassMapItem(key = "bar", value =  Bar.class)
            },
            ref = "#/components/schemas/Category",
            description = "child description"
    )
    public Object annotated;

    @Schema(description = "Foo", deprecated = true)
    static class Foo {
        @Schema(_const = "foo")
        public String foo;
        @Schema(exclusiveMaximumValue = 2)
        public int bar;

        @Schema(types = {"string", "object"})
        public int foobar;
    }

    @Schema(description = "Bar", deprecated = true)
    static class Bar {
        @Schema(_const = "bar")
        public String foo;
        @Schema(exclusiveMaximumValue = 4)
        public int bar;

        @Schema(types = {"string", "integer"})
        public int foobar;
    }
}
