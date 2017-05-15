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

package io.swagger.oas.models.responses;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * ApiResponses
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc0/versions/3.0.md#responsesObject"
 */


public class ApiResponses extends LinkedHashMap<String, ApiResponse> {
  private ApiResponse _default = null;
  private java.util.Map<String, Object> extensions = null;

  public ApiResponses addApiResponse(String name, ApiResponse item) {
    this.put(name, item);
    return this;
  }
  /**
   * returns the _default property from a ApiResponses instance.
   *
   * @return ApiResponse _default
   **/

  public ApiResponse getDefault() {
    return _default;
  }

  public void setDefault(ApiResponse _default) {
    this._default = _default;
  }

  public ApiResponses _default(ApiResponse _default) {
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
    ApiResponses apiResponses = (ApiResponses) o;
    return Objects.equals(this._default, apiResponses._default) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_default, super.hashCode());
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
    sb.append("class ApiResponses {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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

