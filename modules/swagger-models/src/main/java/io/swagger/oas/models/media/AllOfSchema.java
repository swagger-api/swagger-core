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
 * AllOfSchema
 */


public class AllOfSchema extends Schema {
  private List<Schema> allOf = null;

  /**
   * returns the allOf property from a AllOfSchema instance.
   *
   * @return List&lt;Schema&gt; allOf
   **/
  public List<Schema> getAllOf() {
    return allOf;
  }

  /**
   * sets this AllOfSchema's allOf property to the given allOf.
   *
   * @param List&lt;Schema&gt; allOf
   */
  public void setAllOf(List<Schema> allOf) {
    this.allOf = allOf;
  }

  /**
   * sets this AllOfSchema's allOf property to the given allOf and
   * returns this instance of AllOfSchema
   *
   * @param List&lt;Schema&gt; allOf
   * @return AllOfSchema
   */
  public AllOfSchema allOf(List<Schema> allOf) {
    this.allOf = allOf;
    return this;
  }

  /**
   * Adds the given allOfItem to this AllOfSchema's map of allOfItems, with the given key as its key.
   *
   * @param Schema allOfItem
   * @return AllOfSchema
   */
  public AllOfSchema addAllOfItem(Schema allOfItem) {
    if(this.allOf == null) {
      this.allOf = new ArrayList<Schema>();
    }
    this.allOf.add(allOfItem);
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
    AllOfSchema allOfSchema = (AllOfSchema) o;
    return Objects.equals(this.allOf, allOfSchema.allOf) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allOf, super.hashCode());
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllOfSchema {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    allOf: ").append(toIndentedString(allOf)).append("\n");
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

