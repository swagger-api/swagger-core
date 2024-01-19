package io.swagger.v3.core.resolving.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import javax.validation.constraints.Size;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class BidimensionalArray {
    @ArraySchema(maxItems = 3)
    @MySizeAnnotation(maxItems = 2)
    public List<List<String>> withCustomAnnotation;

    @ArraySchema(maxItems = 3)
    public List<Foo<String>> withHelperClass;


    @ArraySchema(maxItems = 2)
    @JsonIgnoreProperties({"empty", "first", "last"})
    public static interface Foo<T> extends List<T> {

    }

    @Size(max = 2)
    public List<String> sized;

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public static @interface MySizeAnnotation {
        int maxItems() default Integer.MIN_VALUE;
    }
}
