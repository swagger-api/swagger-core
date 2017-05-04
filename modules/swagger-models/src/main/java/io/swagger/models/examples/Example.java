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

package io.swagger.models.examples;

import java.util.Objects;

/**
 * Example
 */


public class Example {
  private String summary = null;
  private String description = null;
  private String value = null;
  private String externalValue = null;
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the summary property from a Example instance.
   *
   * @return String summary
   **/

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public Example summary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * returns the description property from a Example instance.
   *
   * @return String description
   **/

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Example description(String description) {
    this.description = description;
    return this;
  }

  /**
   * returns the value property from a Example instance.
   *
   * @return String value
   **/

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Example value(String value) {
    this.value = value;
    return this;
  }

  /**
   * returns the externalValue property from a Example instance.
   *
   * @return String externalValue
   **/

  public String getExternalValue() {
    return externalValue;
  }

  public void setExternalValue(String externalValue) {
    this.externalValue = externalValue;
  }

  public Example externalValue(String externalValue) {
    this.externalValue = externalValue;
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
    Example example = (Example) o;
    return Objects.equals(this.summary, example.summary) &&
        Objects.equals(this.description, example.description) &&
        Objects.equals(this.value, example.value) &&
        Objects.equals(this.externalValue, example.externalValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(summary, description, value, externalValue);
  }


  public java.util.Map<String, Object> getExtensions() {
    return extensions;
  }

  public void addExtension(String name, Object value) {
    if(this.extensions == null) {
      this.extensions = new java.util.HashMap<>();
    }
    this.extensions.put(name, value);
  }

  public void setExtensions(java.util.Map<String, Object> extensions) {
    this.extensions = extensions;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Example {\n");
    
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    externalValue: ").append(toIndentedString(externalValue)).append("\n");
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

