/**
 * Copyright 2017 SmartBear Software
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

package io.swagger.oas.models.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * SecurityRequirement
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc2/versions/3.0.md#securityRequirementObject"
 */

public class SecurityRequirement extends LinkedHashMap<String, List<String>> {
    public SecurityRequirement() {}

    /**
     * Adds a List item to a SecurityRequirement instance
     * based on the name and item parameters provided as key-value pair.
     * <p>
     * Takes value as a String.
     * 
     * @param name
     * @param item
     * @return Updated SecurityRequirements instance
     */

    public SecurityRequirement addList(String name, String item) {
        this.put(name, Arrays.asList(item));
        return this;
    }

    /**
     * Adds a List item to a SecurityRequirement instance
     * based on the name and item parameters provided as key-value pair
     * to the map.
     * <p>
     * Takes value as a List of strings.
     * 
     * @param name
     * @param item
     * @return Updated SecurityRequirements instance
     */

    public SecurityRequirement addList(String name, List<String> item) {
        this.put(name, item);
        return this;
    }

    /**
     * Adds a new empty List item to a SecurityRequirement instance
     * based on the name parameter provided as key to the map.
     * 
     * @param name
     * @return Updated SecurityRequirements instance
     */

    public SecurityRequirement addList(String name) {
        this.put(name, new ArrayList<>());
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SecurityRequirement {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Converts the given object to string with each line indented by 4 spaces
     * (except the first line).
     * This method adds formatting to the general toString() method.
     *
     * @param o Java object to be represented as String
     * @return Formatted String representation of the object
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
