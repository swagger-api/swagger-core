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
 * StringSchema
 */

public class StringSchema extends Schema<String> {
    private String type = "string";

    /**
     * returns the type property from a StringSchema instance.
     *
     * @return String type
     **/

    @Override
    public String getType() {
        return type;
    }

    /**
     * Sets the type property of a StringSchema instance
     * to the parameter.
     *
     * @param type
     */

    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the type property of a StringSchema instance
     * to the parameter and returns the instance.
     *
     * @param type
     * @return StringSchema instance with modified type property.
     */

    @Override
    public StringSchema type(String type) {
        this.type = type;
        return this;
    }

    /**
     * Sets inherited _default property of a StringSchema instance
     * to the parameter and returns the instance.
     * _default is inherited from super class Schema
     *
     * @param _default
     * @return StringSchema instance with the modified _default property
     * @see Schema
     */

    public StringSchema _default(String _default) {
        super.setDefault(_default);
        return this;
    }

    /**
     * Casts Java object to String type.
     * Returns String representation of object
     * or null.
     *
     * @param value
     * @return String value or null
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
     * Sets inherited _enum property of a StringSchema instance
     * to the parameter.
     * <p>
     * _enum is inherited from super class Schema.
     * <p>
     * Uses super class method setEnum() and returns
     * the instance.
     *
     * @param _enum
     * @return StringSchema instance with modified _enum.
     * @see Schema
     */

    public StringSchema _enum(List<String> _enum) {
        super.setEnum(_enum);
        return this;
    }

    /**
     * Adds an item to _enum of a StringSchema instance
     * and returns the instance.
     * If _enum is null, creates a new ArrayList and adds item.
     *
     * @param _enumItem
     * @return StringSchema instance with the added _enum item.
     */

    public StringSchema addEnumItem(String _enumItem) {
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
        StringSchema stringSchema = (StringSchema) o;
        return Objects.equals(this.type, stringSchema.type) &&
               Objects.equals(this._default, stringSchema._default) &&
               Objects.equals(this._enum, stringSchema._enum) &&
               super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, _default, _enum, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class StringSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    _default: ").append(toIndentedString(_default)).append("\n");
        sb.append("    _enum: ").append(toIndentedString(_enum)).append("\n");
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
