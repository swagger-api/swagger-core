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

package io.swagger.oas.models.servers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ServerVariable
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc2/versions/3.0.md#serverVariableObject"
 */

public class ServerVariable {
    private List<String> _enum = null;
    private String _default = null;
    private String description = null;
    private java.util.Map<String, Object> extensions = null;

    /**
     * returns the _enum property from a ServerVariable instance.
     *
     * @return List&lt;String&gt; _enum
     **/

    public List<String> getEnum() {
        return _enum;
    }

    /**
     * Sets the _enum property of a ServerVariable instance
     * to parameter.
     * 
     * @param _enum
     */

    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }

    /**
     * Sets the _enum property of a ServerVariable instance
     * to parameter and returns the instance.
     * 
     * @param _enum
     * @return ServerVariable instance with the set _enum property
     */

    public ServerVariable _enum(List<String> _enum) {
        this._enum = _enum;
        return this;
    }

    /**
     * Adds a string item to _enum list of a ServerVariable instance
     * and returns the instance.
     * <p>
     * If the _enum list is null, creates a new ArrayList and adds the item.
     * 
     * @param _enumItem
     * @return ServerVariable instance with the added enum item.
     */

    public ServerVariable addEnumItem(String _enumItem) {
        if (this._enum == null) {
            this._enum = new ArrayList<String>();
        }
        this._enum.add(_enumItem);
        return this;
    }

    /**
     * returns the _default property from a ServerVariable instance.
     *
     * @return String _default
     **/

    public String getDefault() {
        return _default;
    }

    /**
     * Sets the _default property of a ServerVariable instance
     * to parameter.
     * 
     * @param _default
     */

    public void setDefault(String _default) {
        this._default = _default;
    }

    /**
     * Sets the _default property of a ServerVariable instance
     * to parameter and returns the instance.
     * 
     * @param _default
     * @return ServerVariable instance with the set _default property
     */

    public ServerVariable _default(String _default) {
        this._default = _default;
        return this;
    }

    /**
     * returns the description property from a ServerVariable instance.
     *
     * @return String description
     **/

    public String getDescription() {
        return description;
    }

    /**
     * Sets the description property of a ServerVariable instance
     * to parameter.
     * 
     * @param description
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description property of a ServerVariable instance
     * to parameter and returns the instance.
     * 
     * @param description
     * @return ServerVariable instance with the set description property
     */

    public ServerVariable description(String description) {
        this.description = description;
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
        ServerVariable serverVariable = (ServerVariable) o;
        return Objects.equals(this._enum, serverVariable._enum) &&
               Objects.equals(this._default, serverVariable._default) &&
               Objects.equals(this.description, serverVariable.description) &&
               Objects.equals(this.extensions, serverVariable.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_enum, _default, description, extensions);
    }

    /**
     * Returns extensions property of a ServerVariable instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     */

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds an object item to extensions map of a ServerVariable instance
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
     * Sets extensions property of a ServerVariable instance
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
        sb.append("class ServerVariable {\n");

        sb.append("    _enum: ").append(toIndentedString(_enum)).append("\n");
        sb.append("    _default: ").append(toIndentedString(_default)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
