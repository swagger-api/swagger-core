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

package io.swagger.oas.models.info;

import java.util.Objects;

/**
 * License
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.md#licenseObject"
 */


public class License {
  private String name = null;
  private String url = null;
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the name property from a License instance.
   *
   * @return String name
   **/

  public String getName() {
    return name;
  }

  /**
   * sets this License's name property to the given name.
   *
   * @param String name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * sets this License's name property to the given name and
   * returns this instance of License
   *
   * @param String name
   * @return License
   */
  public License name(String name) {
    this.name = name;
    return this;
  }

  /**
   * returns the url property from a License instance.
   *
   * @return String url
   **/

  public String getUrl() {
    return url;
  }

  /**
   * sets this License's url property to the given url.
   *
   * @param String url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * sets this License's url property to the given url and
   * returns this instance of License
   *
   * @param String url
   * @return License
   */
  public License url(String url) {
    this.url = url;
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
    License license = (License) o;
    return Objects.equals(this.name, license.name) &&
        Objects.equals(this.url, license.url) &&
        Objects.equals(this.extensions, license.extensions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, url, extensions);
  }

  /**
   * returns the extensions property from a License instance.
   *
   * @return Map&lt;String, Object&gt; extensions
   */
  public java.util.Map<String, Object> getExtensions() {
    return extensions;
  }

  /**
   * Adds the given Object to this License's map of extensions, with the given key as its key.
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
   * sets the extensions property for a License instance.
   *
   * @return Map&lt;String, Object&gt; extensions
   */
  public void setExtensions(java.util.Map<String, Object> extensions) {
    this.extensions = extensions;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class License {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
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

