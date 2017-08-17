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
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.md#infoObject"
 */


public class Info {
  private String title = null;
  private String description = null;
  private String termsOfService = null;
  private Contact contact = null;
  private License license = null;
  private String version = null;
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the title property from a Info instance.
   *
   * @return String title
   **/

  public String getTitle() {
    return title;
  }

  /**
   * sets this Info's title property to the given title.
   *
   * @param String title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * sets this Info's title property to the given title and
   * returns this instance of Info
   *
   * @param String title
   * @return Info
   */
  public Info title(String title) {
    this.title = title;
    return this;
  }

  /**
   * returns the description property from a Info instance.
   *
   * @return String description
   **/

  public String getDescription() {
    return description;
  }

  /**
   * sets this Info's description property to the given description.
   *
   * @param String description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * sets this Info's description property to the given description and
   * returns this instance of Info
   *
   * @param String description
   * @return Info
   */
  public Info description(String description) {
    this.description = description;
    return this;
  }

  /**
   * returns the termsOfService property from a Info instance.
   *
   * @return String termsOfService
   **/

  public String getTermsOfService() {
    return termsOfService;
  }

  /**
   * sets this Info's termsOfService property to the given termsOfService.
   *
   * @param String termsOfService
   */
  public void setTermsOfService(String termsOfService) {
    this.termsOfService = termsOfService;
  }

  /**
   * sets this Info's termsOfService property to the given termsOfService and
   * returns this instance of Info
   *
   * @param String termsOfService
   * @return Info
   */
  public Info termsOfService(String termsOfService) {
    this.termsOfService = termsOfService;
    return this;
  }

  /**
   * returns the contact property from a Info instance.
   *
   * @return Contact contact
   **/

  public Contact getContact() {
    return contact;
  }

  /**
   * sets this Info's contact property to the given contact.
   *
   * @param Contact contact
   */
  public void setContact(Contact contact) {
    this.contact = contact;
  }

  /**
   * sets this Info's contact property to the given contact and
   * returns this instance of Info
   *
   * @param Contact contact
   * @return Info
   */
  public Info contact(Contact contact) {
    this.contact = contact;
    return this;
  }

  /**
   * returns the license property from a Info instance.
   *
   * @return License license
   **/

  public License getLicense() {
    return license;
  }

  /**
   * sets this Info's license property to the given license.
   *
   * @param License license
   */
  public void setLicense(License license) {
    this.license = license;
  }

  /**
   * sets this Info's license property to the given license and
   * returns this instance of Info
   *
   * @param License license
   * @return Info
   */
  public Info license(License license) {
    this.license = license;
    return this;
  }

  /**
   * returns the version property from a Info instance.
   *
   * @return String version
   **/

  public String getVersion() {
    return version;
  }

  /**
   * sets this Info's version property to the given version.
   *
   * @param String version
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * sets this Info's version property to the given version and
   * returns this instance of Info
   *
   * @param String version
   * @return Info
   */
  public Info version(String version) {
    this.version = version;
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
    Info info = (Info) o;
    return Objects.equals(this.title, info.title) &&
        Objects.equals(this.description, info.description) &&
        Objects.equals(this.termsOfService, info.termsOfService) &&
        Objects.equals(this.contact, info.contact) &&
        Objects.equals(this.license, info.license) &&
        Objects.equals(this.version, info.version) &&
        Objects.equals(this.extensions, info.extensions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, termsOfService, contact, license, version, extensions);
  }

  /**
   * returns the extensions property from a Info instance.
   *
   * @return Map&lt;String, Object&gt; extensions
   */
  public java.util.Map<String, Object> getExtensions() {
    return extensions;
  }

  /**
   * Adds the given Object to this Info's map of extensions, with the given key as its key.
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
    * sets the extensions property for a Info instance.
    *
    * @return Map&lt;String, Object&gt; extensions
    */
  public void setExtensions(java.util.Map<String, Object> extensions) {
    this.extensions = extensions;
  }
 
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Info {\n");
    
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    termsOfService: ").append(toIndentedString(termsOfService)).append("\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    license: ").append(toIndentedString(license)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

