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

package io.swagger.oas.models.links;

import java.util.Objects;

/**
 * LinkParameter
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.md#linkOParametersbject"
 */


public class LinkParameter {
  private String value;

  public LinkParameter() { }
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the value property from a LinkParameter instance.
   *
   * @return String value
   **/
  public String getValue() {
    return value;
  }

  /**
   * sets this LinkParameter's value property to the given value.
   *
   * @param String value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * sets this LinkParameter's value property to the given value and
   * returns this instance of LinkParameter
   *
   * @param String value
   * @return LinkParameter
   */
  public LinkParameter value(String value) {
    this.value = value;
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
    LinkParameter linkParameters = (LinkParameter) o;
    return Objects.equals(this.value, linkParameters.value) &&
           Objects.equals(this.extensions, linkParameters.extensions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, extensions);
  }

  /**
   * returns the extensions property from a LinkParameter instance.
   *
   * @return Map&lt;String, Object&gt; extensions
   */
  public java.util.Map<String, Object> getExtensions() {
    return extensions;
  }

  /**
   * Adds the given Object to this LinkParameter's map of extensions, with the given key as its key.
   *
   * @param String key
   * @param Object value
   */
  public void addExtension(String name, Object value) {
    if(this.extensions == null) {
      this.extensions = new java.util.HashMap<>();
    }
    this.extensions.put(name, value);
  }

  /**
   * sets the extensions property for a LinkParameter instance.
   *
   * @return Map&lt;String, Object&gt; extensions
   */
  public void setExtensions(java.util.Map<String, Object> extensions) {
    this.extensions = extensions;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LinkParameter {\n");
    
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

