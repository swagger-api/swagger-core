package com.wordnik.swagger.annotations;

public @interface Tag {
    String name() default "";

    String description() default "";

    ExternalDocs externalDocs() default @ExternalDocs( url = "");

    Extension [] extensions() default @Extension( properties = @ExtensionProperty(name="", value=""));
}
