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
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;

import static io.swagger.v3.core.util.TestUtils.normalizeLineEnds;
import static org.testng.Assert.assertEquals;

public class NumericFormatTest {
    @Test
    public void testFormatOfInteger() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithIntegerFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);
        assertEquals(normalizeLineEnds(json),
                "{\n" +
                        "  \"ModelWithIntegerFields\" : {\n" +
                        "    \"type\" : \"object\",\n" +
                        "    \"properties\" : {\n" +
                        "      \"id\" : {\n" +
                        "        \"minimum\" : 3,\n" +
                        "        \"type\" : \"integer\",\n" +
                        "        \"format\" : \"int32\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");
    }

    @Test
    public void testFormatOfDecimal() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithDecimalFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);
        assertEquals(normalizeLineEnds(json),
                "{\n" +
                        "  \"ModelWithDecimalFields\" : {\n" +
                        "    \"type\" : \"object\",\n" +
                        "    \"properties\" : {\n" +
                        "      \"id\" : {\n" +
                        "        \"minimum\" : 3.3,\n" +
                        "        \"exclusiveMinimum\" : false,\n" +
                        "        \"type\" : \"number\",\n" +
                        "        \"format\" : \"double\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");
    }

    @Test
    public void testFormatOfBigDecimal() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithoutScientificFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);

        assertEquals(normalizeLineEnds(json),
                "{\n" +
                        "  \"ModelWithoutScientificFields\" : {\n" +
                        "    \"type\" : \"object\",\n" +
                        "    \"properties\" : {\n" +
                        "      \"id\" : {\n" +
                        "        \"maximum\" : 9999999999999999.99,\n" +
                        "        \"exclusiveMaximum\" : false,\n" +
                        "        \"minimum\" : -9999999999999999.99,\n" +
                        "        \"exclusiveMinimum\" : false,\n" +
                        "        \"type\" : \"number\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");

    }

    static class ModelWithIntegerFields {
        @io.swagger.v3.oas.annotations.media.Schema
        @Min(value = 3)
        public Integer id;
    }

    static class ModelWithDecimalFields {
        @io.swagger.v3.oas.annotations.media.Schema
        @DecimalMin(value = "3.3")
        public Double id;
    }

    static class ModelWithoutScientificFields {
        @io.swagger.v3.oas.annotations.media.Schema
        @DecimalMin(value = "-9999999999999999.99")
        @DecimalMax(value = "9999999999999999.99")
        public BigDecimal id;
    }
}
