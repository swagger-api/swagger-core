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

package io.swagger.oas.models.servers;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

/**
 * ServerVariable
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc0/versions/3.0.md#serverVariableObject"
 */


public class ServerVariable {
  private List<String> _enum = null;
  private String _default = null;
  private String description = null;
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the _enum property from a ServerVariable instance.
   *
   * @return List<String> _enum
   **/

  public List<String> getEnum() {
    return _enum;
  }

  public void setEnum(List<String> _enum) {
    this._enum = _enum;
  }

  public ServerVariable _enum(List<String> _enum) {
    this._enum = _enum;
    return this;
  }

  public ServerVariable addEnumItem(String _enumItem) {
    if(this._enum == null) {
      this._enum = new ArrayList<String>();
    }
    this._enum.add(_enumItem);
    return this;
  }

  /**
   * returns the _default property from a ServerVariable instance.
   *
   * @return String _default
   **/

  public String getDefault() {
    return _default;
  }

  public void setDefault(String _default) {
    this._default = _default;
  }

  public ServerVariable _default(String _default) {
    this._default = _default;
    return this;
  }

  /**
   * returns the description property from a ServerVariable instance.
   *
   * @return String description
   **/

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ServerVariable description(String description) {
    this.description = description;
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
    ServerVariable serverVariable = (ServerVariable) o;
    return Objects.equals(this._enum, serverVariable._enum) &&
        Objects.equals(this._default, serverVariable._default) &&
        Objects.equals(this.description, serverVariable.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_enum, _default, description);
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
    sb.append("class ServerVariable {\n");
    
    sb.append("    _enum: ").append(toIndentedString(_enum)).append("\n");
    sb.append("    _default: ").append(toIndentedString(_default)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

