package io.swagger.v3.oas.annotations.tags;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * The annotation may be applied at class or method level, or in {@link io.swagger.v3.oas.annotations.Operation#tags()} to define tags for the
 * single operation (when applied at method level) or for all operations of a class (when applied at class level).
 * <p>It can also be used in {@link io.swagger.v3.oas.annotations.OpenAPIDefinition#tags()} to define spec level tags.</p>
 * <p>When applied at method or class level, if only a name is provided, the tag will be added to operation only;
 * if additional fields are also defined, like description or externalDocs, the Tag will also be added to openAPI.tags
 * field</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#tagObject">Tag (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.OpenAPIDefinition
 **/
@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Tags.class)
@Inherited
public @interface Tag {

    /**
     * The name of this tag.
     *
     * @return the name of this tag
     */
    String name();

    /**
     * A short description for this tag.
     *
     * @return the description of this tag
     */
    String description() default "";

    /**
     * Additional external documentation for this tag.
     *
     * @return the external documentation for this tag
     */
    ExternalDocumentation externalDocs() default @ExternalDocumentation();

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};
}
