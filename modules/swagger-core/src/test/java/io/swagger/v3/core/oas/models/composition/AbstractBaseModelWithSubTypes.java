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

package io.swagger.v3.core.oas.models.composition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonSubTypes({@JsonSubTypes.Type(value = Thing1.class, name = "thing3"), @JsonSubTypes.Type(value = Thing2.class, name = "thing2")})
@Schema(description = "I am an Abstract Base Model with Sub-Types", discriminatorProperty = "_type")
abstract public class AbstractBaseModelWithSubTypes {

    @Schema(description = "This value is used as a discriminator for serialization")
    public String _type;
    @Schema(description = "An arbitrary field")
    public String a;
    @Schema(description = "An arbitrary field")
    public String b;
}
