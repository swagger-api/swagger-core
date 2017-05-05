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

package io.swagger.models.media;

import java.util.Objects;
import io.swagger.models.ExternalDocumentation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DateTimeSchema
 */


public class DateTimeSchema extends Schema {
  private String type = "string";
  private String format = "date-time";
  private String _default = null;

  /**
   * returns the type property from a DateTimeSchema instance.
   *
   * @return String type
   **/

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public DateTimeSchema type(String type) {
    this.type = type;
    return this;
  }

  /**
   * returns the format property from a DateTimeSchema instance.
   *
   * @return String format
   **/

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public DateTimeSchema format(String format) {
    this.format = format;
    return this;
  }

  /**
   * returns the _default property from a DateTimeSchema instance.
   *
   * @return String _default
   **/

  public String getDefault() {
    return _default;
  }

  public void setDefault(String _default) {
    this._default = _default;
  }

  public DateTimeSchema _default(String _default) {
    this._default = _default;
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
    DateTimeSchema dateTimeSchema = (DateTimeSchema) o;
    return Objects.equals(this.type, dateTimeSchema.type) &&
        Objects.equals(this.format, dateTimeSchema.format) &&
        Objects.equals(this._default, dateTimeSchema._default) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, format, _default, super.hashCode());
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DateTimeSchema {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    _default: ").append(toIndentedString(_default)).append("\n");
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

