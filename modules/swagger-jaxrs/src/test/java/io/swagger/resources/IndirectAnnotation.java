package io.swagger.resources;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiImplicitParams(value = {
        @ApiImplicitParam(
                paramType = "header",
                name = "myHeader",
                dataType = "java.lang.String"
        )
})
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface IndirectAnnotation {

}
