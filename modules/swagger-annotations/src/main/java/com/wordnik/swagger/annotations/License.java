package com.wordnik.swagger.annotations;

public @interface License {
    String name() default "";

    /**
     * URL for the license.
     */
    String url() default "";
}
