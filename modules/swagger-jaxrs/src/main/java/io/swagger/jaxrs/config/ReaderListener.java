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

package io.swagger.jaxrs.config;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Swagger;

/**
 * Listener providing hooks for customizing automatically generated Swagger definitions in a JAX-RS
 * environment. Any classes picked up during the scanning process implementing this interface will
 * be instantiated via newInstance() and invoked before and after generating Swagger definitions, allowing
 * code to add additional data or change the generated definition.
 */

public interface ReaderListener {

    /**
     * Called before the Swagger definition gets populated from scanned classes. Use this method to
     * pre-process the Swagger definition before it gets populated.
     *
     * @param reader  the reader used to read annotations and build the Swagger definition
     * @param swagger the initial swagger definition
     */

    void beforeScan(Reader reader, Swagger swagger);

    /**
     * Called after a Swagger definition has been populated from scanned classes. Use this method to
     * post-process Swagger definitions.
     *
     * @param reader  the reader used to read annotations and build the Swagger definition
     * @param swagger the configured Swagger definition
     */

    void afterScan(Reader reader, Swagger swagger);
}
