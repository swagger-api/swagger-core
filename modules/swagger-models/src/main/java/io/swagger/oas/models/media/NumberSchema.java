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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * NumberSchema
 */

public class NumberSchema extends Schema<BigDecimal> {
    private String type = "number";

    /**
     * returns the type property from a NumberSchema instance.
     *
     * @return String type
     **/

    @Override
    public String getType() {
        return type;
    }

    /**
     * Sets the type property of NumberSchema instance
     * to the parameter.
     *
     * @param type
     */

    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the type property of a NumberSchema instance
     * to the parameter.
     *
     * @param type
     * @return NumberSchema instance with the modified type property
     */

    @Override
    public NumberSchema type(String type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the _default property of a NumberSchema instance
     * to the parameter and returns the instance.
     * _default property is inherited from super class Schema
     * Method setDefault inherited from Schema super class.
     *
     * @param _default
     * @return The instance of NumberSchema with the modified _default
     * @see Schema.setDefault
     */

    public NumberSchema _default(BigDecimal _default) {
        super.setDefault(_default);
        return this;
    }

    /**
     * Casts an object to BigDecimal type.
     *
     * @param value
     * @return BigDecimal value or null
     * @see java.math.BigDecimal
     */

    @Override
    protected BigDecimal cast(Object value) {
        if (value != null) {
            try {
                return new BigDecimal(value.toString());
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * Sets inherited _enum property of a NumberSchema instance
     * to the parameter.
     * _enum is inherited from Schema.
     *
     * @param _enum A list of BigDecimal values
     * @return A NumberSchema instance with the set _enum
     * @see Schema
     */

    public NumberSchema _enum(List<BigDecimal> _enum) {
        this._enum = _enum;
        return this;
    }

    /**
     * Adds an item to _enum List.
     * If _enum is null, will create a new ArrayList and add the item.
     *
     * @param BigDecimal _enumItem
     * @return NumberSchema instance with the modified _enum item
     */

    public NumberSchema addEnumItem(BigDecimal _enumItem) {
        if (this._enum == null) {
            this._enum = new ArrayList<BigDecimal>();
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
        NumberSchema numberSchema = (NumberSchema) o;
        return Objects.equals(this.type, numberSchema.type) &&
               Objects.equals(this._default, numberSchema._default) &&
               Objects.equals(this._enum, numberSchema._enum) &&
               super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, _default, _enum, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NumberSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
