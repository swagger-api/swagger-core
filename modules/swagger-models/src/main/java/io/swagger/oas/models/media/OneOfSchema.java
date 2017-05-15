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
 * OneOfSchema
 */


public class OneOfSchema extends Schema {
  private List<Schema> oneOf = null;

  /**
   * returns the oneOf property from a OneOfSchema instance.
   *
   * @return List<Schema> oneOf
   **/

  public List<Schema> getOneOf() {
    return oneOf;
  }

  public void setOneOf(List<Schema> oneOf) {
    this.oneOf = oneOf;
  }

  public OneOfSchema oneOf(List<Schema> oneOf) {
    this.oneOf = oneOf;
    return this;
  }

  public OneOfSchema addOneOfItem(Schema oneOfItem) {
    if(this.oneOf == null) {
      this.oneOf = new ArrayList<Schema>();
    }
    this.oneOf.add(oneOfItem);
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
    OneOfSchema oneOfSchema = (OneOfSchema) o;
    return Objects.equals(this.oneOf, oneOfSchema.oneOf) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oneOf, super.hashCode());
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OneOfSchema {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    oneOf: ").append(toIndentedString(oneOf)).append("\n");
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

