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

import java.util.Objects;
import io.swagger.oas.models.ExternalDocumentation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * IntegerSchema
 */


public class IntegerSchema extends Schema {
  private String type = "integer";
  private String format = "int32";
  private Integer _default = null;
  private List<Integer> _enum = null;

  /**
   * returns the type property from a IntegerSchema instance.
   *
   * @return String type
   **/

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public IntegerSchema type(String type) {
    this.type = type;
    return this;
  }

  /**
   * returns the format property from a IntegerSchema instance.
   *
   * @return String format
   **/

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public IntegerSchema format(String format) {
    this.format = format;
    return this;
  }

  /**
   * returns the _default property from a IntegerSchema instance.
   *
   * @return Integer _default
   **/

  public Integer getDefault() {
    return _default;
  }

  public void setDefault(Integer _default) {
    this._default = _default;
  }

  public IntegerSchema _default(Integer _default) {
    this._default = _default;
    return this;
  }

  /**
   * returns the _enum property from a IntegerSchema instance.
   *
   * @return List<Integer> _enum
   **/

  public List<Integer> getEnum() {
    return _enum;
  }

  public void setEnum(List<Integer> _enum) {
    this._enum = _enum;
  }

  public IntegerSchema _enum(List<Integer> _enum) {
    this._enum = _enum;
    return this;
  }

  public IntegerSchema addEnumItem(Integer _enumItem) {
    if(this._enum == null) {
      this._enum = new ArrayList<Integer>();
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
    IntegerSchema integerSchema = (IntegerSchema) o;
    return Objects.equals(this.type, integerSchema.type) &&
        Objects.equals(this.format, integerSchema.format) &&
        Objects.equals(this._default, integerSchema._default) &&
        Objects.equals(this._enum, integerSchema._enum) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, format, _default, _enum, super.hashCode());
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntegerSchema {\n");
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

