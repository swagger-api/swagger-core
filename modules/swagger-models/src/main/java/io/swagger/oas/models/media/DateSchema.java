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
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * DateSchema
 */


public class DateSchema extends Schema<Date> {
  private String type = "string";
  private String format = "date";

  /**
   * returns the type property from a DateSchema instance.
   *
   * @return String type
   **/

  public String getType() {
    return type;
  }

  /**
   * sets this DateSchema's type property to the given type.
   *
   * @param String type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * sets this DateSchema's type property to the given type and
   * returns this instance of DateSchema
   *
   * @param String type
   * @return DateSchema
   */
  public DateSchema type(String type) {
    this.type = type;
    return this;
  }

  /**
   * returns the format property from a DateSchema instance.
   *
   * @return String format
   **/

  public String getFormat() {
    return format;
  }

  /**
   * sets this DateSchema's format property to the given format.
   *
   * @param String format
   */
  public void setFormat(String format) {
    this.format = format;
  }

  /**
   * sets this DateSchema's format property to the given format and
   * returns this instance of DateSchema
   *
   * @param String format
   * @return DateSchema
   */
  public DateSchema format(String format) {
    this.format = format;
    return this;
  }

  /**
   * sets the _default property of this DateSchema to the given _default value.
   * 
   * @param byte[] _default
   * @return DateSchema
   */
  public DateSchema _default(Date _default) {
    super.setDefault(_default);
    return this;
  }

  @Override
  protected Date cast(Object value) {
    if(value != null) {
      try {
        if(value instanceof Date) {
          return (Date) value;
        }
      }
      catch (Exception e) {
      }
    }
    return null;
  }

  /**
   * sets the _enum property of this DateSchema to the given _enum value.
   * 
   * @param List&lt;byte[]&gt; _enum
   * @return DateSchema
   */
  public DateSchema _enum(List<Date> _enum) {
    this._enum = _enum;
    return this;
  }

  /**
   * Adds the given _enumItem to this DateSchema's List of _enumItems.
   *
   * @param byte[] _enumItem
   */
  public DateSchema addEnumItem(Date _enumItem) {
    if(this._enum == null) {
      this._enum = new ArrayList<Date>();
    }
    this._enum.add(_enumItem);
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
    DateSchema dateSchema = (DateSchema) o;
    return Objects.equals(this.type, dateSchema.type) &&
        Objects.equals(this.format, dateSchema.format) &&
        Objects.equals(this._default, dateSchema._default) &&
        Objects.equals(this._enum, dateSchema._enum) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, format, _default, _enum, super.hashCode());
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DateSchema {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    _default: ").append(toIndentedString(_default)).append("\n");
    sb.append("    _enum: ").append(toIndentedString(_enum)).append("\n");
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

