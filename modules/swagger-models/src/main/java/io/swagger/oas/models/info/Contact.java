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
 * Contact
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.md#contactObject"
 */


public class Contact {
  private String name = null;
  private String url = null;
  private String email = null;
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the name property from a Contact instance.
   *
   * @return String name
   **/

  public String getName() {
    return name;
  }

  /**
   * sets this Contact's name property to the given name.
   *
   * @param String name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * sets this Contact's name property to the given name and
   * returns this instance of Contact
   *
   * @param String name
   * @return Contact
   */
  public Contact name(String name) {
    this.name = name;
    return this;
  }

  /**
   * returns the url property from a Contact instance.
   *
   * @return String url
   **/

  public String getUrl() {
    return url;
  }

  /**
   * sets this Contact's url property to the given url.
   *
   * @param String url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * sets this Contact's url property to the given url and
   * returns this instance of Contact
   *
   * @param String url
   * @return Contact
   */
  public Contact url(String url) {
    this.url = url;
    return this;
  }

  /**
   * returns the email property from a Contact instance.
   *
   * @return String email
   **/

  public String getEmail() {
    return email;
  }

  /**
   * sets this Contact's email property to the given email.
   *
   * @param String email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * sets this Contact's email property to the given email and
   * returns this instance of Contact
   *
   * @param String email
   * @return Contact
   */
  public Contact email(String email) {
    this.email = email;
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
    Contact contact = (Contact) o;
    return Objects.equals(this.name, contact.name) &&
        Objects.equals(this.url, contact.url) &&
        Objects.equals(this.email, contact.email) &&
        Objects.equals(this.extensions, contact.extensions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, url, email, extensions);
  }

  /**
   * returns the extensions property from a Contact instance.
   *
   * @return Map&lt;String, Object&gt; extensions
   */
  public java.util.Map<String, Object> getExtensions() {
    return extensions;
  }

  /**
   * Adds the given Object to this Contact's map of extensions, with the given key as its key.
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
    * sets the extensions property for a Contact instance.
    *
    * @return Map&lt;String, Object&gt; extensions
    */
  public void setExtensions(java.util.Map<String, Object> extensions) {
    this.extensions = extensions;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Contact {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
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

