/**
 * Copyright 2016 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that configures definition level metadata. Still missing are the following:
 * - Security Requirements
 * - Parameters
 * - Responses
 *
 * @since 1.5.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SwaggerDefinition {

    /**
     * The host to specify in the generated Swagger definition.
     *
     * @return the host to specify in the generated Swagger definition - keep empty for default
     */
    String host() default "";

    /**
     * The basePath to specify in the generated Swagger definition.
     *
     * @return the basePath to specify in the generated Swagger definition - keep empty for default
     */
    String basePath() default "";

    /**
     * Global level consumes for this swagger definition.
     * <p>
     * These will be added to all api definitions that don't have local overrides - see
     * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#swagger-object
     *
     * @return a list of global level consumes.
     */
    String[] consumes() default "";

    /**
     * Global level produces for this swagger definition.
     * <p>
     * These will be added to all api definitions that don't have local overrides - see
     * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#swagger-object
     *
     * @return a list of global level consumes
     */
    String[] produces() default "";

    /**
     * The transfer protocol of the API.
     * <p>
     * Setting this to Scheme.DEFAULT will result in the result being generated from the hosting container.
     *
     * @return list of supported transfer protocols, keep empty for default
     */
    Scheme[] schemes() default Scheme.DEFAULT;

    /**
     * Global tags that can be used to tag individual Apis and ApiOperations.
     * <p>
     * See https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#tagObject
     *
     * @return list of globally defined tags
     */
    Tag[] tags() default @Tag(name = "");


    /**
     * Defintions for security schemes
     *
     * @return defintions for security schemes
     */
    SecurityDefinition securityDefinition() default @SecurityDefinition();

    /**
     * General metadata for this Swagger definition.
     * <p>
     * See https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#infoObject
     *
     * @return general metadata for this Swagger definition
     */
    Info info() default @Info(title = "", version = "");

    /**
     * Reference to external documentation for this Swagger definition.
     *
     * @return a reference to external documentation
     */
    ExternalDocs externalDocs() default @ExternalDocs(url = "");

    /**
     * Enumeration with valid schemes
     */
    enum Scheme {
        DEFAULT, HTTP, HTTPS, WS, WSS
    }
}
