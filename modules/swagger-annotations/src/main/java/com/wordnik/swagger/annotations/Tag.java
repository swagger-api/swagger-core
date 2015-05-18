package com.wordnik.swagger.annotations;

public @interface Tag {
    String value() default "";

    String description() default "";

    ExternalDocs externalDocs() default @ExternalDocs( url = "");

    Extension [] extensions() default @Extension( properties = @ExtensionProperty(name="", value=""));
}
