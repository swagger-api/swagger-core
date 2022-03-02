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

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.*;

public class FormParamBeanTest {

    private static class FormParamBean {
        @FormParam(value = "param1")
        private String param1;

        @FormParam(value = "param2")
        private String param2;

        public String getParam1() {
            return param1;
        }

        public void setParam1(String param1) {
            this.param1 = param1;
        }

        public String getParam2() {
            return param2;
        }

        public void setParam2(String param2) {
            this.param2 = param2;
        }
    }

    @Path("/")
    private static class MyFormBeanParamResource {
        @GET
        @Consumes("application/x-www-form-urlencoded")
        public String getWithFormBeanParam(@BeanParam FormParamBean formParamBean) {
            return "result";
        }
    }

    @Test(description = "check schema of serialized BeanParam containing FormParams")
    public void shouldSerializeTypeParameter() {
        OpenAPI openApi = new Reader(new OpenAPI()).read(MyFormBeanParamResource.class);
        RequestBody requestBody = openApi.getPaths().get("/").getGet().getRequestBody();
        Assert.assertNotNull(requestBody);
        MediaType mediaType = requestBody.getContent().get("application/x-www-form-urlencoded");
        Assert.assertNotNull(mediaType);
        Schema schema = mediaType.getSchema();
        Assert.assertEquals(schema.getProperties().size(), 2);
        Assert.assertEquals(schema.getProperties().get("param1"), new StringSchema());
        Assert.assertEquals(schema.getProperties().get("param2"), new StringSchema());
    }

}