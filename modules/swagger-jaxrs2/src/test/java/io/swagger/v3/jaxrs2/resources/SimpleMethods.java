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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

public class SimpleMethods {

    @GET
    @Path("/object")
    public TestBean getTestBean() {
        return new TestBean();
    }

    @GET
    @Path("/int")
    public int getInt() {
        return 0;
    }

    @GET
    @Path("/intArray")
    public int[] getIntArray() {
        return new int[]{0};
    }

    @GET
    @Path("/string")
    public String[] getStringArray() {
        return new String[]{};
    }

    @GET
    @Path("/stringArray")
    public void getWithIntArrayInput(@QueryParam("ids") int[] inputs) {
    }

    static class TestBean {
        public String foo;
        public TestChild testChild;
    }

    static class TestChild {
        public String foo;
    }
}