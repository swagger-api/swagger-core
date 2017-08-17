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

package io.swagger.oas.models;

import io.swagger.oas.models.callbacks.Callback;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.oas.models.responses.ApiResponses;
import io.swagger.oas.models.security.SecurityRequirement;
import io.swagger.oas.models.servers.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Operation
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.md#operationObject"
 */


public class Operation {
  private List<String> tags = null;
  private String summary = null;
  private String description = null;
  private ExternalDocumentation externalDocs = null;
  private String operationId = null;
  private List<Parameter> parameters = null;
  private RequestBody requestBody = null;
  private ApiResponses responses = null;
  private Map<String, Callback> callbacks = null;
  private Boolean deprecated = null;
  private List<SecurityRequirement> security = null;
  private List<Server> servers = null;
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the tags property from a Operation instance.
   *
   * @return List&lt;String&gt; tags
   **/

  public List<String> getTags() {
    return tags;
  }

  /**
   * sets this Operation's tags property to the given Tags.
   *
   * @param List&lt;String&gt;tags
   */
  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  /**
   * sets this Operation's tags property to the given tags and
   * returns this instance of Operation
   *
   * @param List&lt;String&gt;tags
   * @return Operation
   */
  public Operation tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  /**
   * Adds the given tagsItem to this Operation's list of tags, with the given key as its key.
   *
   * @param String key
   * @param String tagsItem
   * @return Operation
   */
  public Operation addTagsItem(String tagsItem) {
    if(this.tags == null) {
      this.tags = new ArrayList<String>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * returns the summary property from a Operation instance.
   *
   * @return String summary
   **/

  public String getSummary() {
    return summary;
  }

  /**
   * sets this Operation's summary property to the given summary.
   *
   * @param String summary
   */
  public void setSummary(String summary) {
    this.summary = summary;
  }

  /**
   * sets this Operation's summary property to the given summary and
   * returns this instance of Operation
   *
   * @param String summary
   * @return Operation
   */
  public Operation summary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * returns the description property from a Operation instance.
   *
   * @return String description
   **/
  public String getDescription() {
    return description;
  }

  /**
   * sets this Operation's description property to the given description.
   *
   * @param String description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * sets this Operation's description property to the given description and
   * returns this instance of Operation
   *
   * @param String description
   * @return Operation
   */
  public Operation description(String description) {
    this.description = description;
    return this;
  }

  /**
   * returns the externalDocs property from a Operation instance.
   *
   * @return ExternalDocumentation externalDocs
   **/

  public ExternalDocumentation getExternalDocs() {
    return externalDocs;
  }

  /**
   * sets this Operation's externalDocs property to the given externalDocs.
   *
   * @param ExternalDocumentation externalDocs
   */
  public void setExternalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
  }

  /**
   * sets this Operation's externalDocs property to the given externalDocs and
   * returns this instance of Operation
   *
   * @param ExternalDocumentation externalDocs
   * @return Operation
   */
  public Operation externalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
    return this;
  }

  /**
   * returns the operationId property from a Operation instance.
   *
   * @return String operationId
   **/

  public String getOperationId() {
    return operationId;
  }

  /**
   * sets this Operation's operationId property to the given operationId.
   *
   * @param String operationId
   */
  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  /**
   * sets this Operation's operationId property to the given operationId and
   * returns this instance of Operation
   *
   * @param String operationId
   * @return Operation
   */
  public Operation operationId(String operationId) {
    this.operationId = operationId;
    return this;
  }

  /**
   * returns the parameters property from a Operation instance.
   *
   * @return List&lt;Parameter&gt; parameters
   **/

  public List<Parameter> getParameters() {
    return parameters;
  }

 /**
   * sets this Operation's parameters property to the given parameters.
   *
   * @param List&lt;Parameter&gt;parameters
   */
  public void setParameters(List<Parameter> parameters) {
    this.parameters = parameters;
  }

  /**
   * sets this Operation's parameters property to the given parameters and
   * returns this instance of Operation
   *
   * @param List&lt;Parameter&gt;parameters
   * @return Operation
   */
  public Operation parameters(List<Parameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  /**
   * Adds the given parametersItem to this Operation's list of parameters, with the given key as its key.
   *
   * @param String key
   * @param Parameter parametersItem
   * @return Operation
   */
  public Operation addParametersItem(Parameter parametersItem) {
    if(this.parameters == null) {
      this.parameters = new ArrayList<Parameter>();
    }
    this.parameters.add(parametersItem);
    return this;
  }

  /**
   * returns the requestBody property from a Operation instance.
   *
   * @return RequestBody requestBody
   **/

  public RequestBody getRequestBody() {
    return requestBody;
  }

  /**
   * sets this Operation's requestBody property to the given requestBody.
   *
   * @param RequestBody requestBody
   */
  public void setRequestBody(RequestBody requestBody) {
    this.requestBody = requestBody;
  }

  /**
   * sets this Operation's requestBody property to the given requestBody and
   * returns this instance of Operation
   *
   * @param RequestBody requestBody
   * @return Operation
   */
  public Operation requestBody(RequestBody requestBody) {
    this.requestBody = requestBody;
    return this;
  }

  /**
   * returns the responses property from a Operation instance.
   *
   * @return ApiResponses responses
   **/

  public ApiResponses getResponses() {
    return responses;
  }

  /**
   * sets this Operation's responses property to the given responses.
   *
   * @param ApiResponses responses
   */
  public void setResponses(ApiResponses responses) {
    this.responses = responses;
  }

  /**
   * sets this Operation's responses property to the given responses and
   * returns this instance of Operation
   *
   * @param ApiResponses responses
   * @return Operation
   */
  public Operation responses(ApiResponses responses) {
    this.responses = responses;
    return this;
  }

  /**
   * returns the callbacks property from a Operation instance.
   *
   * @return Callbacks callbacks
   **/

  public Map<String, Callback> getCallbacks() {
    return callbacks;
  }

  /**
   * sets this Operation's callbacks property to the given callbacks.
   *
   * @param Map&lt;String, Callback&gt; callbacks
   */
  public void setCallbacks(Map<String, Callback> callbacks) {
    this.callbacks = callbacks;
  }

  /**
   * sets this Operation's callbacks property to the given callbacks and
   * returns this instance of Operation
   *
   * @param Map&lt;String, Callback&gt; callbacks
   * @return Operation
   */
  public Operation callbacks(Map<String, Callback> callbacks) {
    this.callbacks = callbacks;
    return this;
  }

  /**
   * returns the deprecated property from a Operation instance.
   *
   * @return Boolean deprecated
   **/

  public Boolean getDeprecated() {
    return deprecated;
  }

  /**
   * sets this Operation's deprecated property to the given deprecated.
   *
   * @param Boolean deprecated
   */
  public void setDeprecated(Boolean deprecated) {
    this.deprecated = deprecated;
  }

  /**
   * sets this Operation's deprecated property to the given deprecated and
   * returns this instance of Operation
   *
   * @param Boolean deprecated
   * @return Operation
   */
  public Operation deprecated(Boolean deprecated) {
    this.deprecated = deprecated;
    return this;
  }

  /**
   * returns the security property from a Operation instance.
   *
   * @return List&lt;SecurityRequirement&gt; security
   **/

  public List<SecurityRequirement> getSecurity() {
    return security;
  }

  /**
   * sets this Operation's security property to the given security.
   *
   * @param List&lt;SecurityRequirement&gt; security
   */
  public void setSecurity(List<SecurityRequirement> security) {
    this.security = security;
  }

  /**
   * sets this Operation's security property to the given security and
   * returns this instance of Operation
   *
   * @param List&lt;SecurityRequirement&gt;security
   * @return Operation
   */
  public Operation security(List<SecurityRequirement> security) {
    this.security = security;
    return this;
  }

  /**
   * Adds the given securityItem to this Operation's list of securityItems, with the given key as its key.
   *
   * @param String key
   * @param SecurityRequirement securityItem
   * @return Operation
   */
  public Operation addSecurityItem(SecurityRequirement securityItem) {
    if(this.security == null) {
      this.security = new ArrayList<SecurityRequirement>();
    }
    this.security.add(securityItem);
    return this;
  }

  /**
   * returns the servers property from a Operation instance.
   *
   * @return List&lt;Server&gt; servers
   **/

  public List<Server> getServers() {
    return servers;
  }

  /**
   * sets this Operation's servers property to the given servers.
   *
   * @param List&lt;Server&gt; servers
   */
  public void setServers(List<Server> servers) {
    this.servers = servers;
  }

  /**
   * sets this Operation's servers property to the given servers and
   * returns this instance of Operation
   *
   * @param List&lt;Server&gt;servers
   * @return Operation
   */
  public Operation servers(List<Server> servers) {
    this.servers = servers;
    return this;
  }

  /**
   * Adds the given serversItem to this Operation's list of serversItem, with the given key as its key.
   *
   * @param String key
   * @param Server serversItem
   * @return Operation
   */
  public Operation addServersItem(Server serversItem) {
    if(this.servers == null) {
      this.servers = new ArrayList<Server>();
    }
    this.servers.add(serversItem);
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
    Operation operation = (Operation) o;
    return Objects.equals(this.tags, operation.tags) &&
        Objects.equals(this.summary, operation.summary) &&
        Objects.equals(this.description, operation.description) &&
        Objects.equals(this.externalDocs, operation.externalDocs) &&
        Objects.equals(this.operationId, operation.operationId) &&
        Objects.equals(this.parameters, operation.parameters) &&
        Objects.equals(this.requestBody, operation.requestBody) &&
        Objects.equals(this.responses, operation.responses) &&
        Objects.equals(this.callbacks, operation.callbacks) &&
        Objects.equals(this.deprecated, operation.deprecated) &&
        Objects.equals(this.security, operation.security) &&
        Objects.equals(this.servers, operation.servers) &&
        Objects.equals(this.extensions, operation.extensions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tags, summary, description, externalDocs, operationId, parameters, requestBody, responses, callbacks, deprecated, security, servers, extensions);
  }

  /**
   * returns the extensions property from a Operation instance.
   *
   * @return Map&lt;String, Object&gt; extensions
   **/
  public java.util.Map<String, Object> getExtensions() {
    return extensions;
  }

  /**
   * Adds the given Object to this Operation's map of extensions, with the given key as its key.
   *
   * @param String key
   * @param Object value
   * @return Operation
   */
  public void addExtension(String name, Object value) {
    if(this.extensions == null) {
      this.extensions = new java.util.HashMap<>();
    }
    this.extensions.put(name, value);
  }

  /**
   * sets this Operation's extensions property to the given map of extensions.
   *
   * @param Map&lt;String, Object&gt;extensions
   */
  public void setExtensions(java.util.Map<String, Object> extensions) {
    this.extensions = extensions;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Operation {\n");
    
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    externalDocs: ").append(toIndentedString(externalDocs)).append("\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
    sb.append("    requestBody: ").append(toIndentedString(requestBody)).append("\n");
    sb.append("    responses: ").append(toIndentedString(responses)).append("\n");
    sb.append("    callbacks: ").append(toIndentedString(callbacks)).append("\n");
    sb.append("    deprecated: ").append(toIndentedString(deprecated)).append("\n");
    sb.append("    security: ").append(toIndentedString(security)).append("\n");
    sb.append("    servers: ").append(toIndentedString(servers)).append("\n");
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

