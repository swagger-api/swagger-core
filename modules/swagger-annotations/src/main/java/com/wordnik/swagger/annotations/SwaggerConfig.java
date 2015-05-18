package com.wordnik.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that configures definition level metadata. Still missing are the following:
 * - Security Definitions
 * - Security Requirements
 * - Responses
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SwaggerConfig {

    /**
     * @return the host to specify in the generated Swagger definition - keep empty for default
     */

    String host() default "";

    /**
     * @return the basePath to specify in the generated Swagger definition - keep empty for default
     */

    String basePath() default "";

    /**
     * Global level consumes for this swagger definition, will be added to all api definitions that
     * don't have local overrides - see
     * https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#swagger-object
     *
     * @return a list of global level consumes
     */

    String [] consumes() default "";

    /**
     * Global level produces for this swagger definition, will be added to all api definitions that
     * don't have local overrides - see
     * https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#swagger-object
     *
     * @return a list of global level consumes
     */

    String [] produces() default "";

    /**
     * The transfer protocol of the API. Setting this to Scheme.DEFAULT will result in
     * the result being generated from the hosting container.
     *
     * @return list of supported transfer protocols, keep empty for default
     */

    Scheme [] schemes() default Scheme.DEFAULT;

    /**
     * Enumeration with valid schemes
     */

    enum Scheme { DEFAULT, HTTP, HTTPS, WS, WSS };

    /**
     * Global tags that can be used to tag inidividual Apis and ApiOperations. See
     * https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#tagObject
     *
     * @return list of globally defined tags
     */

    Tag [] tags() default @Tag();

    /**
     * General metadata for this Swagger definition. See
     * https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md#infoObject
     *
     * @return general metadata for this Swagger definition
     */

    Info info() default @Info();

    /**
     * Shared parameter definitions that can be referred to in operations using the localReference
     * annotation property.
     *
     * @return list of shared parameter definitions
     */

    ApiParam [] parameters() default @ApiParam();

    /**
     * Reference to external documentation for this Swagger definition
     *
     * @return a reference to external documentation
     */

    ExternalDocs externalDocs() default @ExternalDocs( url = "");
}
