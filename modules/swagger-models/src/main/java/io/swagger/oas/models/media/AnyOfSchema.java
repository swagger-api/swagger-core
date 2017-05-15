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

import java.util.Objects;
import io.swagger.oas.models.ExternalDocumentation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AnyOfSchema
 */


public class AnyOfSchema extends Schema {
  private List<Schema> anyOf = null;

  /**
   * returns the anyOf property from a AnyOfSchema instance.
   *
   * @return List<Schema> anyOf
   **/

  public List<Schema> getAnyOf() {
    return anyOf;
  }

  public void setAnyOf(List<Schema> anyOf) {
    this.anyOf = anyOf;
  }

  public AnyOfSchema anyOf(List<Schema> anyOf) {
    this.anyOf = anyOf;
    return this;
  }

  public AnyOfSchema addAnyOfItem(Schema anyOfItem) {
    if(this.anyOf == null) {
      this.anyOf = new ArrayList<Schema>();
    }
    this.anyOf.add(anyOfItem);
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
    AnyOfSchema anyOfSchema = (AnyOfSchema) o;
    return Objects.equals(this.anyOf, anyOfSchema.anyOf) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anyOf, super.hashCode());
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnyOfSchema {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    anyOf: ").append(toIndentedString(anyOf)).append("\n");
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

