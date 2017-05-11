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

package io.swagger.oas.models.links;

import java.util.Objects;
import io.swagger.oas.models.headers.Headers;

/**
 * Link
 *
 * @link https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc0/versions/3.0.md#linkObject
 */


public class Link {
  private String operationRef = null;
  private String operationId = null;
  private LinkParameters parameters = null;
  private Headers headers = null;
  private String description = null;
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the operationRef property from a Link instance.
   *
   * @return String operationRef
   **/

  public String getOperationRef() {
    return operationRef;
  }

  public void setOperationRef(String operationRef) {
    this.operationRef = operationRef;
  }

  public Link operationRef(String operationRef) {
    this.operationRef = operationRef;
    return this;
  }

  /**
   * returns the operationId property from a Link instance.
   *
   * @return String operationId
   **/

  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public Link operationId(String operationId) {
    this.operationId = operationId;
    return this;
  }

  /**
   * returns the parameters property from a Link instance.
   *
   * @return LinkParameters parameters
   **/

  public LinkParameters getParameters() {
    return parameters;
  }

  public void setParameters(LinkParameters parameters) {
    this.parameters = parameters;
  }

  public Link parameters(LinkParameters parameters) {
    this.parameters = parameters;
    return this;
  }

  /**
   * returns the headers property from a Link instance.
   *
   * @return Headers headers
   **/

  public Headers getHeaders() {
    return headers;
  }

  public void setHeaders(Headers headers) {
    this.headers = headers;
  }

  public Link headers(Headers headers) {
    this.headers = headers;
    return this;
  }

  /**
   * returns the description property from a Link instance.
   *
   * @return String description
   **/

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Link description(String description) {
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
    Link link = (Link) o;
    return Objects.equals(this.operationRef, link.operationRef) &&
        Objects.equals(this.operationId, link.operationId) &&
        Objects.equals(this.parameters, link.parameters) &&
        Objects.equals(this.headers, link.headers) &&
        Objects.equals(this.description, link.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operationRef, operationId, parameters, headers, description);
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
    sb.append("class Link {\n");
    
    sb.append("    operationRef: ").append(toIndentedString(operationRef)).append("\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
    sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
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

