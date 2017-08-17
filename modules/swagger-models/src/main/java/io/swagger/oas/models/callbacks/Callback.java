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

package io.swagger.oas.models.callbacks;

import java.util.Objects;
import io.swagger.oas.models.PathItem;
import java.util.LinkedHashMap;

/**
 * Callback
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.md#callbackObject"
 */


public class Callback extends LinkedHashMap<String, PathItem> {
  public Callback() { }
  private java.util.Map<String, Object> extensions = null;

  /**
   * Adds the given PathItem to this Callbacks's list of PathItems, with the given key as its key.
   *
   * @param String name
   * @param PathItem item
   */
  public Callback addPathItem(String name, PathItem item) {
    this.put(name, item);
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
    Callback callback = (Callback) o;
    return Objects.equals(this.extensions, callback.extensions) &&
      super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(extensions, super.hashCode());
  }

  /**
   * returns the extensions property from a Callback instance.
   *
   * @return Map&lt;String, Object&gt; extensions
   **/
  public java.util.Map<String, Object> getExtensions() {
    return extensions;
  }

  /**
   * Adds the given Object to this Callback's map of extensions, with the given key as its key.
   *
   * @param String key
   * @param Object value
   * @return Components
   */
  public void addExtension(String name, Object value) {
    if(this.extensions == null) {
      this.extensions = new java.util.HashMap<>();
    }
    this.extensions.put(name, value);
  }

  /**
   * sets this Components' extensions property to the given map of extensions.
   *
   * @param Map&lt;String, Object&gt;extensions
   */
  public void setExtensions(java.util.Map<String, Object> extensions) {
    this.extensions = extensions;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Callback {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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

