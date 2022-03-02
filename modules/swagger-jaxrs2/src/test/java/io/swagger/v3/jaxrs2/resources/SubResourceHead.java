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

package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.Path;

@Path("/head")
public class SubResourceHead {

    /**
     * This path is expected to be collected
     *
     * @return class instance of sub-resource
     */
    @Path("tail")
    public Class<SubResourceTail> getTail() {
        return SubResourceTail.class;
    }

    /**
     * This path is expected to be collected
     *
     * @return class instance of sub-resource
     */
    @Path("noPath")
    public Class<NoPathSubResource> getNoPath() {
        return NoPathSubResource.class;
    }

    /**
     * This path is expected to be skipped as {@link String} doesn't process
     * any requests.
     *
     * @return string class
     */
    @Path("stringClass")
    public Class<String> getStringClass() {
        return String.class;
    }

    /**
     * This path is expected to be skipped as resource class is unknown here.
     *
     * @return {@code null}
     */
    @Path("anyClass")
    public <T> Class<T> getAnyClass() {
        return null;
    }

    /**
     * This path is expected to be skipped as resource class is unknown here.
     *
     * @return {@code null}
     */
    @Path("wildcardClass")
    public Class<?> getWildcardClass() {
        return null;
    }

    /**
     * This path is expected to be skipped as method result is an array.
     *
     * @return {@code null}
     */
    @Path("classes")
    public Class<?>[] getClasses() {
        return null;
    }
}
