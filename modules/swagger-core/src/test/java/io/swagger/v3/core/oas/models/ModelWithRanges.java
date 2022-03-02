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

package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class ModelWithRanges {
    @Schema(description = "values with include range", minimum = "1", maximum = "5")
    public int getInclusiveRange() {
        return 2;
    }

    @Schema(description = "values with include range", minimum = "1", exclusiveMinimum = true, maximum = "5", exclusiveMaximum = true)
    public int getExclusiveRange() {
        return 2;
    }

    @Schema(description = "values with include range", minimum = "1")
    public int getPositiveInfinityRange() {
        return 2;
    }

    @Schema(description = "values with include range", maximum = "5")
    public int getNegativeInfinityRange() {
        return 2;
    }

    @Schema(description = "some string values", allowableValues = {"str1", "str2"})
    public String getStringValues() {
        return "test";
    }

    @Schema(description = "some string values", minimum = "1.0", maximum = "8.0", exclusiveMaximum = true)
    public Double getDoubleValues() {
        return 1.0;
    }

    @Schema(description = "some int values", allowableValues = {"1", "2"})
    public int getIntAllowableValues() {
        return 2;
    }

    @Schema(description = "some int values with null", allowableValues = {"1", "2", "null"})
    public int getIntAllowableValuesWithNull() {
        return 2;
    }
}
