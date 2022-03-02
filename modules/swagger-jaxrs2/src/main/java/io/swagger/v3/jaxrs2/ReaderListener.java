/**
 * Copyright 2021 SmartBear Software
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

package io.swagger.v3.jaxrs2;

import io.swagger.v3.oas.integration.api.OpenApiReader;
import io.swagger.v3.oas.models.OpenAPI;

/**
 * Listener providing hooks for customizing automatically generated OpenAPI definitions in a JAX-RS
 * environment. Any classes picked up during the scanning process implementing this interface will
 * be instantiated via newInstance() and invoked before and after generating OpenAPI definitions, allowing
 * code to add additional data or change the generated definition.
 */

public interface ReaderListener {

    /**
     * Called before the OpenAPI definition gets populated from scanned classes. Use this method to
     * pre-process the OpenAPI definition before it gets populated.
     *
     * @param reader  the reader used to read annotations and build the openAPI definition
     * @param openAPI the initial OpenAPI definition
     */

    void beforeScan(OpenApiReader reader, OpenAPI openAPI);

    /**
     * Called after a OpenAPI definition has been populated from scanned classes. Use this method to
     * post-process OpenAPI definitions.
     *
     * @param reader  the reader used to read annotations and build the OpenAPI definition
     * @param openAPI the configured OpenAPI definition
     */

    void afterScan(OpenApiReader reader, OpenAPI openAPI);
}