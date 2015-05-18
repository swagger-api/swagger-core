package com.wordnik.swagger.annotations;

public @interface Info {

    String description() default "";

    String version() default "";

    String title() default "";

    String termsOfService() default "";

    Contact contact() default @Contact();

    License license() default @License();

    Extension[] extensions() default @Extension( properties = @ExtensionProperty(name="",value=""));
}
