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

package io.swagger.v3.jaxrs2.resources.generics.ticket2144;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/item")
public class ItemResource extends BaseResource<Item> {

    @GET
    @Path("/{id}")
    @Override
    public ItemWithChildren getById(@PathParam("id") String id) { return null; }

    @GET
    @Path("/nogeneric/{id}")
    @Override
    public ItemWithChildren getByIdNoGeneric(@PathParam("id") String id) { return null; }

    @GET
    @Path("/nogenericsamereturn/{id}")
    @Override
    public BaseDTO getByIdNoGenericSameReturn(@PathParam("id") String id) { return null; }



    @POST
    @Path("/genericparam")
    public BaseDTO genericParam(ItemWithChildren id) { return null; }

}
