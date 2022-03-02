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

package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.oas.models.JCovariantGetter;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class CovariantGetterTest {
    @Test(description = "it should read a getter with covariant return type")
    public void testCovariantGetter() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(JCovariantGetter.Sub.class);
        assertEquals(models.size(), 1);
        final String json = "{" +
                "   \"Sub\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"myProperty\":{" +
                "            \"type\":\"integer\"," +
                "            \"format\":\"int32\"" +
                "         }," +
                "         \"myOtherProperty\":{" +
                "            \"type\":\"integer\"," +
                "            \"format\":\"int32\"" +
                "         }" +
                "      }" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(models, json);
    }
}
