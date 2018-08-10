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

package io.swagger.v3.oas.models.media;

import java.util.Objects;

/**
 * ObjectSchema
 */

public class ObjectSchema extends Schema<Object> {
    private String type = "object";
    private Object defaultObject = null;

    /**
     * returns the type property from a ObjectSchema instance.
     *
     * @return String type
     **/

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ObjectSchema type(String type) {
        this.type = type;
        return this;
    }

    public Schema example(Object example) {
        if (example != null) {
            super.example = example.toString();
        }
        return this;
    }

    @Override
    protected Object cast(Object value) {
        return value;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ObjectSchema objectSchema = (ObjectSchema) o;
        return Objects.equals(this.type, objectSchema.type) &&
                Objects.equals(this.defaultObject, objectSchema.defaultObject) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, defaultObject, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ObjectSchema {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    defaultObject: ").append(toIndentedString(defaultObject)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

