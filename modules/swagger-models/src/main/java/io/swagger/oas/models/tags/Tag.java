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

package io.swagger.oas.models.tags;

import java.util.Objects;

import io.swagger.oas.models.ExternalDocumentation;

/**
 * Tag
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc2/versions/3.0.md#tagObject"
 */

public class Tag {
    private String name = null;
    private String description = null;
    private ExternalDocumentation externalDocs = null;
    private java.util.Map<String, Object> extensions = null;

    /**
     * returns the name property from a Tag instance.
     *
     * @return String name
     **/

    public String getName() {
        return name;
    }

    /**
     * Sets the name property of a Tag instance to the
     * parameter.
     * 
     * @param name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the name property of a Tag instance to the
     * parameter and returns the instance.
     * 
     * @param name
     * @return Tag instance with the set name property
     */

    public Tag name(String name) {
        this.name = name;
        return this;
    }

    /**
     * returns the description property from a Tag instance.
     *
     * @return String description
     **/

    public String getDescription() {
        return description;
    }

    /**
     * Sets the description property of a Tag instance to the
     * parameter.
     * 
     * @param description
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description property of a Tag instance to the
     * parameter and returns the instance.
     * 
     * @param description
     * @return Tag instance with the set description property
     */

    public Tag description(String description) {
        this.description = description;
        return this;
    }

    /**
     * returns the externalDocs property from a Tag instance.
     *
     * @return ExternalDocumentation externalDocs
     **/

    public ExternalDocumentation getExternalDocs() {
        return externalDocs;
    }

    /**
     * Sets the externalDocs property of a Tag instance to the
     * parameter.
     * 
     * @param externalDocs
     */

    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }

    /**
     * Sets the externalDocs property of a Tag instance to the
     * parameter and returns the instance.
     * 
     * @param externalDocs
     * @return Tag instance with the set externalDocs property
     */

    public Tag externalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
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
        Tag tag = (Tag) o;
        return Objects.equals(this.name, tag.name) &&
               Objects.equals(this.description, tag.description) &&
               Objects.equals(this.externalDocs, tag.externalDocs) &&
               Objects.equals(this.extensions, tag.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, externalDocs, extensions);
    }

    /**
     * Returns extensions property of a Tag instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     */

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds an object item to extensions map of a Tag instance
     * at the specified key.
     * If extensions is null, then creates a new HashMap and adds the item.
     *
     * @param name
     * @param value
     */

    public void addExtension(String name, Object value) {
        if (this.extensions == null) {
            this.extensions = new java.util.HashMap<>();
        }
        this.extensions.put(name, value);
    }

    /**
     * Sets extensions property of a Tag instance
     * to the parameter.
     *
     * @param extensions
     */

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Tag {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    externalDocs: ").append(toIndentedString(externalDocs)).append("\n");
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
