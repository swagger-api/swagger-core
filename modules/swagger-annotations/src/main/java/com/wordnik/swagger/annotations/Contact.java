package com.wordnik.swagger.annotations;

public @interface Contact {

    String name() default "";
    String url() default "";
    String email() default "";
}
