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

package io.swagger.v3.core.resolving.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestObject3697 {
    @JsonProperty("id")
    private final Long id;

    @io.swagger.v3.oas.annotations.media.Schema(hidden = true)
    @JsonProperty("hidden")
    private final String hidden;


    @JsonCreator
    public TestObject3697(@JsonProperty("id") Long id,
                          @io.swagger.v3.oas.annotations.media.Schema(hidden = true)
                                              @JsonProperty("hidden") String hidden) {
        this.id = id;
        this.hidden = hidden;
    }

    public Long getId() {
        return id;
    }

    public String getHidden() {
        return hidden;
    }
}
