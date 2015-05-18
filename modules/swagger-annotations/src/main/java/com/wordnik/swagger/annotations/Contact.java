package com.wordnik.swagger.annotations;

public @interface Contact {

    String value() default "";
    String url() default "";
    String email() default "";
}
