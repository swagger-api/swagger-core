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

package io.swagger.v3.core.converting.override;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converting.override.resources.MyCustomClass;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

public class ModelPropertyOverrideTest {
    @Test
    public void overrideTest() throws Exception {
        ModelConverters.getInstance().addConverter(new SamplePropertyConverter());
        final Map<String, Schema> model = ModelConverters.getInstance().read(MyPojo.class);
        final String expected = "{" +
                "  \"MyPojo\" : {" +
                "    \"type\" : \"object\"," +
                "    \"properties\" : {" +
                "      \"id\" : {" +
                "        \"type\" : \"string\"" +
                "      }," +
                "      \"myCustomClass\" : {" +
                "        \"type\" : \"string\"," +
                "        \"format\" : \"date-time\"" +
                "      }" +
                "    }" +
                "  }" +
                "}";
        SerializationMatchers.assertEqualsToJson(model, expected);
    }

    @Test
    public void extendedOverrideTest() throws Exception {
        ModelConverters.getInstance().addConverter(new SamplePropertyExtendedConverter(Json.mapper()));
        final Map<String, Schema> model = ModelConverters.getInstance().read(MyPojo.class);
        final String expected = "{" +
                "  \"MyPojo\" : {" +
                "    \"type\" : \"object\"," +
                "    \"properties\" : {" +
                "      \"id\" : {" +
                "        \"type\" : \"string\"" +
                "      }," +
                "      \"myCustomClass\" : {" +
                "        \"type\" : \"string\"," +
                "        \"format\" : \"date-time\"," +
                "        \"description\" : \"instead of modeling this class in the documentation, we will model a string\"" +
                "      }" +
                "    }" +
                "  }" +
                "}";
        SerializationMatchers.assertEqualsToJson(model, expected);
    }

    public static class MyPojo {
        public String getId() {
            return "";
        }

        public void setId(String id) {
        }

        @io.swagger.v3.oas.annotations.media.Schema(required = false, description = "instead of modeling this class in the documentation, we will model a string")
        public MyCustomClass getMyCustomClass() {
            return null;
        }

        public void setMyCustomClass(MyCustomClass myCustomClass) {
        }
    }
}
