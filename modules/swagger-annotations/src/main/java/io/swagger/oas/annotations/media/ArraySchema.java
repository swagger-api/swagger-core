package io.swagger.oas.annotations.media;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD,
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ArraySchema {
    Schema schema() default @Schema;

    /**
     * sets the maximum number of items in an array.  Ignored if value is Integer.MIN_VALUE.
     **/
    int maxItems() default Integer.MIN_VALUE;

    /**
     * sets the minimum number of items in an array.  Ignored if value is Integer.MAX_VALUE.
     **/
    int minItems() default Integer.MAX_VALUE;

    /**
     * determines whether an array of items will be unique
     **/
    boolean uniqueItems() default false;
}
