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
 * ArraySchema
 */


public class ArraySchema extends Schema {
  private String type = "array";
  private Schema items = null;

  /**
   * returns the type property from a ArraySchema instance.
   *
   * @return String type
   **/

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ArraySchema type(String type) {
    this.type = type;
    return this;
  }

  /**
   * returns the items property from a ArraySchema instance.
   *
   * @return Schema items
   **/

  public Schema getItems() {
    return items;
  }

  public void setItems(Schema items) {
    this.items = items;
  }

  public ArraySchema items(Schema items) {
    this.items = items;
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
    ArraySchema arraySchema = (ArraySchema) o;
    return Objects.equals(this.type, arraySchema.type) &&
        Objects.equals(this.items, arraySchema.items) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, items, super.hashCode());
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ArraySchema {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

