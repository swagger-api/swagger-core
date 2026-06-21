package io.swagger.v3.jaxrs2.petstore31;

import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Schema;

public class SimpleTag {

    @Schema(
            properties = {
                    @StringToClassMapItem(key = "foo", value =  Foo.class)
            },
            ref = "#/components/schemas/SimpleCategory",
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
}
