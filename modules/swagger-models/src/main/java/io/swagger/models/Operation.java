package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.callbacks.Callbacks;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.security.SecurityRequirement;

import java.util.List;
import java.util.Objects;

/**
 * Operation
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class Operation {
  @JsonProperty("tags")
  private List<String> tags = null;

  @JsonProperty("summary")
  private String summary = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("externalDocs")
  private ExternalDocumentation externalDocs = null;

  @JsonProperty("operationId")
  private String operationId = null;

  @JsonProperty("parameters")
  private List<Parameter> parameters = null;

  @JsonProperty("requestBody")
  private RequestBody requestBody = null;

  @JsonProperty("responses")
  private Responses responses = null;

  @JsonProperty("callbacks")
  private Callbacks callbacks = null;

  @JsonProperty("deprecated")
  private Boolean deprecated = null;

  @JsonProperty("security")
  private List<SecurityRequirement> security = null;

  @JsonProperty("servers")
  private List<Server> servers = null;

  public Operation tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public Operation addTagsItem(String tagsItem) {
    this.tags.add(tagsItem);
    return this;
  }

   /**
   * Get tags
   * @return tags
  **/
  @ApiModelProperty(value = "")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Operation summary(String summary) {
    this.summary = summary;
    return this;
  }

   /**
   * Get summary
   * @return summary
  **/
  @ApiModelProperty(value = "")
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public Operation description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Operation externalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
    return this;
  }

   /**
   * Get externalDocs
   * @return externalDocs
  **/
  @ApiModelProperty(value = "")
  public ExternalDocumentation getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
  }

  public Operation operationId(String operationId) {
    this.operationId = operationId;
    return this;
  }

   /**
   * Get operationId
   * @return operationId
  **/
  @ApiModelProperty(value = "")
  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public Operation parameters(List<Parameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  public Operation addParametersItem(Parameter parametersItem) {
    this.parameters.add(parametersItem);
    return this;
  }

   /**
   * Get parameters
   * @return parameters
  **/
  @ApiModelProperty(value = "")
  public List<Parameter> getParameters() {
    return parameters;
  }

  public void setParameters(List<Parameter> parameters) {
    this.parameters = parameters;
  }

  public Operation requestBody(RequestBody requestBody) {
    this.requestBody = requestBody;
    return this;
  }

   /**
   * Get requestBody
   * @return requestBody
  **/
  @ApiModelProperty(value = "")
  public RequestBody getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(RequestBody requestBody) {
    this.requestBody = requestBody;
  }

  public Operation responses(Responses responses) {
    this.responses = responses;
    return this;
  }

   /**
   * Get responses
   * @return responses
  **/
  @ApiModelProperty(required = true, value = "")
  public Responses getResponses() {
    return responses;
  }

  public void setResponses(Responses responses) {
    this.responses = responses;
  }

  public Operation callbacks(Callbacks callbacks) {
    this.callbacks = callbacks;
    return this;
  }

   /**
   * Get callbacks
   * @return callbacks
  **/
  @ApiModelProperty(value = "")
  public Callbacks getCallbacks() {
    return callbacks;
  }

  public void setCallbacks(Callbacks callbacks) {
    this.callbacks = callbacks;
  }

  public Operation deprecated(Boolean deprecated) {
    this.deprecated = deprecated;
    return this;
  }

   /**
   * Get deprecated
   * @return deprecated
  **/
  @ApiModelProperty(value = "")
  public Boolean getDeprecated() {
    return deprecated;
  }

  public void setDeprecated(Boolean deprecated) {
    this.deprecated = deprecated;
  }

  public Operation security(List<SecurityRequirement> security) {
    this.security = security;
    return this;
  }

  public Operation addSecurityItem(SecurityRequirement securityItem) {
    this.security.add(securityItem);
    return this;
  }

   /**
   * Get security
   * @return security
  **/
  @ApiModelProperty(value = "")
  public List<SecurityRequirement> getSecurity() {
    return security;
  }

  public void setSecurity(List<SecurityRequirement> security) {
    this.security = security;
  }

  public Operation servers(List<Server> servers) {
    this.servers = servers;
    return this;
  }

  public Operation addServersItem(Server serversItem) {
    this.servers.add(serversItem);
    return this;
  }

   /**
   * Get servers
   * @return servers
  **/
  @ApiModelProperty(value = "")
  public List<Server> getServers() {
    return servers;
  }

  public void setServers(List<Server> servers) {
    this.servers = servers;
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
        Objects.equals(this.servers, operation.servers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tags, summary, description, externalDocs, operationId, parameters, requestBody, responses, callbacks, deprecated, security, servers);
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

