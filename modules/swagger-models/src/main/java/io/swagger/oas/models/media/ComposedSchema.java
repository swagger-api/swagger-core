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
 * ComposedSchema
 */


public class ComposedSchema extends Schema {
  private List<Schema> allOf = null;
  private List<Schema> anyOf = null;
  private List<Schema> oneOf = null;

  /**
   * returns the allOf property from a ComposedSchema instance.
   *
   * @return List&lt;Schema&gt; allOf
   **/

  public List<Schema> getAllOf() {
    return allOf;
  }

  /**
   * sets the allOf property of this instance of ComposedSchema
   * 
   * @param List&lt;Schema&gt; allOf
   */
  public void setAllOf(List<Schema> allOf) {
    this.allOf = allOf;
  }

  /**
   * sets the allOf property of this instance of ComposedSchema 
   * and returns this ComposedSchema
   * 
   * @param List&lt;Schema&gt; allOf
   * @return ComposedSchema
   */
  public ComposedSchema allOf(List<Schema> allOf) {
    this.allOf = allOf;
    return this;
  }

  /**
    * adds the given allOfItem Schema to this ComposedSchema's list of allOfItems
    * 
    * @param Schema allOfItem
    * @return ComposedSchema
    */
  public ComposedSchema addAllOfItem(Schema allOfItem) {
    if(this.allOf == null) {
      this.allOf = new ArrayList<Schema>();
    }
    this.allOf.add(allOfItem);
    return this;
  }

  /**
   * returns the anyOf property from a ComposedSchema instance.
   *
   * @return List&lt;Schema&gt; anyOf
   **/

  public List<Schema> getAnyOf() {
    return anyOf;
  }

  /**
   * sets the anyOf property of this instance of ComposedSchema
   * 
   * @param List&lt;Schema&gt; anyOf
   */
  public void setAnyOf(List<Schema> anyOf) {
    this.anyOf = anyOf;
  }

  /**
   * sets the anyOf property of this instance of ComposedSchema 
   * and returns this ComposedSchema
   * 
   * @param List&lt;Schema&gt; anyOf
   * @return ComposedSchema
   */
  public ComposedSchema anyOf(List<Schema> anyOf) {
    this.anyOf = anyOf;
    return this;
  }

  /**
    * adds the given anyOfItem Schema to this ComposedSchema's list of anyOfItems
    * 
    * @param Schema anyOfItem
    * @return ComposedSchema
    */
  public ComposedSchema addAnyOfItem(Schema anyOfItem) {
    if(this.anyOf == null) {
      this.anyOf = new ArrayList<Schema>();
    }
    this.anyOf.add(anyOfItem);
    return this;
  }

  /**
   * returns the oneOf property from a ComposedSchema instance.
   *
   * @return List&lt;Schema&gt; oneOf
   **/

  public List<Schema> getOneOf() {
    return oneOf;
  }

  /**
   * sets the oneOf property of this instance of ComposedSchema
   * 
   * @param List&lt;Schema&gt; oneOf
   */
  public void setOneOf(List<Schema> oneOf) {
    this.oneOf = oneOf;
  }

  /**
   * sets the oneOf property of this instance of ComposedSchema 
   * and returns this ComposedSchema
   * 
   * @param List&lt;Schema&gt; oneOf
   * @return ComposedSchema
   */
  public ComposedSchema oneOf(List<Schema> oneOf) {
    this.oneOf = oneOf;
    return this;
  }

  /**
    * adds the given oneOfItem Schema to this ComposedSchema's list of oneOfItems
    * 
    * @param Schema oneOfItem
    * @return ComposedSchema
    */
  public ComposedSchema addOneOfItem(Schema oneOfItem) {
    if(this.oneOf == null) {
      this.oneOf = new ArrayList<Schema>();
    }
    this.oneOf.add(oneOfItem);
    return this;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComposedSchema allOfSchema = (ComposedSchema) o;
    return  Objects.equals(this.allOf, allOfSchema.allOf) &&
            Objects.equals(this.anyOf, allOfSchema.anyOf) &&
            Objects.equals(this.oneOf, allOfSchema.oneOf) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allOf, anyOf, oneOf, super.hashCode());
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComposedSchema {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    allOf: ").append(toIndentedString(allOf)).append("\n");
    sb.append("    anyOf: ").append(toIndentedString(anyOf)).append("\n");
    sb.append("    oneOf: ").append(toIndentedString(oneOf)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

