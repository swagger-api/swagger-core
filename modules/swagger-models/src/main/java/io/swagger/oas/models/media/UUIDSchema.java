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
import java.util.UUID;

/**
 * UUIDSchema
 */

public class UUIDSchema extends Schema<UUID> {
    private String type = "string";
    private String format = "uuid";

    /**
     * returns the type property from a UUIDSchema instance.
     *
     * @return String type
     **/

    @Override
    public String getType() {
        return type;
    }

    /**
     * Sets the type property of a UUIDSchema instance
     * to the parameter.
     *
     * @param type
     */

    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the type property of a UUIDSchema instance
     * to the parameter and returns the instance.
     *
     * @param type
     * @return UUIDSchema instance with modified type property.
     */

    @Override
    public UUIDSchema type(String type) {
        this.type = type;
        return this;
    }

    /**
     * returns the format property from a UUIDSchema instance.
     *
     * @return String format
     **/

    @Override
    public String getFormat() {
        return format;
    }

    /**
     * Sets format property of a UUIDSchema instance
     * to the parameter.
     *
     * @param format
     */

    @Override
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Sets format property of a UUIDSchema instance
     * to the parameter and returns the instance.
     *
     * @param format
     * @return UUIDSchema instance with the modified format
     */

    @Override
    public UUIDSchema format(String format) {
        this.format = format;
        return this;
    }

    /**
     * Sets inherited _default property of a UUIDSchema instance
     * to the parameter and returns the instance.
     * _default is inherited from super class Schema
     *
     * <p>
     * Sets _default from UUID argument
     *
     * @param _default
     * @return UUIDSchema instance with the modified _default property
     * @see Schema
     */

    public UUIDSchema _default(UUID _default) {
        super.setDefault(_default);
        return this;
    }

    /**
     * Sets inherited _default property of a UUIDSchema instance
     * to the parameter and returns the instance.
     * _default is inherited from super class Schema
     *
     * <p>
     * Sets _default from String argument
     *
     * @param _default
     * @return UUIDSchema instance with the modified _default property
     * @see Schema
     */

    public UUIDSchema _default(String _default) {
        if (_default != null) {
            super.setDefault(UUID.fromString(_default));
        }
        return this;
    }

    /**
     * Casts Java object to UUID type.
     * Returns UUID representation of object
     * or null.
     *
     * @param value
     * @return UUID value or null
     */

    @Override
    protected UUID cast(Object value) {
        if (value != null) {
            try {
                return UUID.fromString(value.toString());
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * Sets inherited _enum property of a UUIDSchema instance
     * to the parameter.
     * _enum is inherited from super class Schema.
     *
     * @param _enum
     * @return UUIDSchema instance with modified _enum.
     * @see Schema
     */

    public UUIDSchema _enum(List<UUID> _enum) {
        this._enum = _enum;
        return this;
    }

    /**
     * Adds an item to _enum of a UUIDSchema instance
     * to the parameter and returns the instance.
     * If _enum is null, creates a new ArrayList and adds item.
     *
     * @param _enumItem
     * @return UUIDSchema instance with the added _enum item.
     */

    public UUIDSchema addEnumItem(UUID _enumItem) {
        if (this._enum == null) {
            this._enum = new ArrayList<UUID>();
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
        UUIDSchema uuIDSchema = (UUIDSchema) o;
        return Objects.equals(this.type, uuIDSchema.type) &&
               Objects.equals(this.format, uuIDSchema.format) &&
               Objects.equals(this._default, uuIDSchema._default) &&
               Objects.equals(this._enum, uuIDSchema._enum) &&
               super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, format, _default, _enum, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UUIDSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
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
