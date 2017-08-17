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
 * BinarySchema
 */


public class BinarySchema extends Schema<byte[]> {
  private String type = "string";
  private String format = "binary";

  /**
   * returns the type property from a BinarySchema instance.
   *
   * @return String type
   **/

  public String getType() {
    return type;
  }

  /**
   * sets this BinarySchema's type property to the given type.
   *
   * @param String type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * sets this BinarySchema's type property to the given type and
   * returns this instance of BinarySchema
   *
   * @param String type
   * @return BinarySchema
   */
  public BinarySchema type(String type) {
    this.type = type;
    return this;
  }

  /**
   * returns the format property from a BinarySchema instance.
   *
   * @return String format
   **/

  public String getFormat() {
    return format;
  }

  /**
   * sets this BinarySchema's format property to the given format.
   *
   * @param String format
   */
  public void setFormat(String format) {
    this.format = format;
  }

  /**
   * sets this BinarySchema's format property to the given format and
   * returns this instance of BinarySchema
   *
   * @param String format
   * @return BinarySchema
   */
  public BinarySchema format(String format) {
    this.format = format;
    return this;
  }

  /**
   * sets the _default property of this BinarySchema to the given _default value.
   * 
   * @param byte[] _default
   * @return BinarySchema
   */
  public BinarySchema _default(byte[] _default) {
    super.setDefault(_default);
    return this;
  }

  @Override
  protected byte[] cast(Object value) {
    if(value != null) {
      try {
        if(value instanceof byte[]) {
          return (byte[]) value;
        }
      }
      catch (Exception e) {
      }
    }
    return null;
  }

  /**
   * sets the _enum property of this BinarySchema to the given _enum value.
   * 
   * @param List&lt;byte[]&gt; _enum
   * @return BinarySchema
   */
  public BinarySchema _enum(List<byte[]> _enum) {
    this._enum = _enum;
    return this;
  }

  /**
   * Adds the given _enumItem to this BinarySchema's List of _enumItems.
   *
   * @param byte[] _enumItem
   */
  public BinarySchema addEnumItem(byte[] _enumItem) {
    if(this._enum == null) {
      this._enum = new ArrayList<byte[]>();
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
    BinarySchema binarySchema = (BinarySchema) o;
    return Objects.equals(this.type, binarySchema.type) &&
        Objects.equals(this.format, binarySchema.format) &&
        Objects.equals(this._default, binarySchema._default) &&
        Objects.equals(this._enum, binarySchema._enum) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, format, _default, _enum, super.hashCode());
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BinarySchema {\n");
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

