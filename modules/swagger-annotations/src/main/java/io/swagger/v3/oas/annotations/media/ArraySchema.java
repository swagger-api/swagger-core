package io.swagger.v3.oas.annotations.media;


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

    /**
     * The schema of the items in the array
     * 
     * @return schema
     */
    Schema schema() default @Schema;

    /**
     * sets the maximum number of items in an array.  Ignored if value is Integer.MIN_VALUE.
     * 
     * @return integer representing maximum number of items in array
     **/
    int maxItems() default Integer.MIN_VALUE;

    /**
     * sets the minimum number of items in an array.  Ignored if value is Integer.MAX_VALUE.
     * 
     * @return integer representing minimum number of items in array
     **/
    int minItems() default Integer.MAX_VALUE;

    /**
     * determines whether an array of items will be unique
     * 
     * @return boolean - whether items in an array are unique or repeating
     **/
    boolean uniqueItems() default false;
}