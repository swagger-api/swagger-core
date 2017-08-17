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

package io.swagger.oas.models.media;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PasswordSchema
 */

public class PasswordSchema extends Schema<String> {
    private String type = "string";
    private String format = "password";

    /**
     * returns the type property from a PasswordSchema instance.
     *
     * @return String type
     **/

    @Override
    public String getType() {
        return type;
    }

    /**
     * Sets the type property of a PasswordSchema instance
     * to the parameter.
     *
     * @param type
     */

    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the type property of a PasswordSchema instance
     * to the parameter and returns the instance.
     *
     * @param type
     * @return PasswordSchema instance with the modified type property
     */

    @Override
    public PasswordSchema type(String type) {
        this.type = type;
        return this;
    }

    /**
     * returns the format property from a PasswordSchema instance.
     *
     * @return String format
     **/

    @Override
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format property for a PasswordSchema instance
     * to the parameter.
     *
     * @param format
     **/

    @Override
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Sets the format property for a PasswordSchema instance
     * to the parameter and returns the instance.
     *
     * @param format
     * @return PasswordSchema instance with modified format
     **/

    @Override
    public PasswordSchema format(String format) {
        this.format = format;
        return this;
    }

    /**
     * Sets the inherited _default property of a PasswordSchema instance
     * to the parameter and returns the instance.
     * _default is inherited from Schema.
     *
     * @param _default
     * @return The instance of PasswordSchema with the set _default
     */

    public PasswordSchema _default(String _default) {
        this._default = _default;
        return this;
    }

    /**
     * Converts object to String representation
     *
     * @param Object value
     * @return String value
     */

    @Override
    protected String cast(Object value) {
        if (value != null) {
            try {
                return value.toString();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * Sets inherited _enum property of a PasswordSchema instance
     * to the parameter.
     * _enum is inherited from Schema.
     *
     * @param _enum
     * @return A PasswordSchema instance with set _enum
     * @see Schema
     */

    public PasswordSchema _enum(List<String> _enum) {
        this._enum = _enum;
        return this;
    }

    /**
     * Adds an item to _enum List.
     * If _enum is null, will create a new ArrayList and add the item.
     *
     * @param _enumItem to be added to the _enum List
     * @return PasswordSchema instance with the modified _enum
     */

    public PasswordSchema addEnumItem(String _enumItem) {
        if (this._enum == null) {
            this._enum = new ArrayList<String>();
        }
        this._enum.add(_enumItem);
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
        PasswordSchema passwordSchema = (PasswordSchema) o;
        return Objects.equals(this.type, passwordSchema.type) &&
               Objects.equals(this.format, passwordSchema.format) &&
               Objects.equals(this._default, passwordSchema._default) &&
               Objects.equals(this._enum, passwordSchema._enum) &&
               super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, format, _default, _enum, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PasswordSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
        sb.append("    _default: ").append(toIndentedString(_default)).append("\n");
        sb.append("    _enum: ").append(toIndentedString(_enum)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     * This method adds formatting to the general toString() method.
     *
     * @param o Java object to be represented as String
     * @return String representation of the object formatted
     */

    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
