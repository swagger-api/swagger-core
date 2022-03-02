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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import javax.servlet.ServletConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

@Path("/resource/{id}")
@Produces({"application/json", "application/xml"})
public class ResourceWithKnownInjections {

    private Integer constructorParam;
    @QueryParam("fieldParam")
    private String fieldParam; // injection into a class field

    @Parameter(hidden = true)
    @QueryParam("hiddenParam")
    private String hiddenParam;

    // injection into a constructor parameter
    public ResourceWithKnownInjections(@PathParam("id") Integer constructorParam,
                                       @QueryParam("hiddenParam") @Parameter(hidden = true) String hiddenParam,
                                       @Context ServletConfig context) {
        this.constructorParam = constructorParam;
    }

    private ResourceWithKnownInjections(@PathParam("id") Integer constructorParam, @QueryParam("fakeParam") String
            fakeParam) {
        this.constructorParam = constructorParam;
    }

    @GET
    @Operation
    public String get(@QueryParam("methodParam") String methodParam) {
        // injection into a resource method parameter
        final StringBuilder sb = new StringBuilder();
        sb.append("Constructor param: ").append(constructorParam).append("\n");
        sb.append("Field param: ").append(fieldParam).append("\n");
        sb.append("Method param: ").append(methodParam).append("\n");
        return sb.toString();
    }
}
