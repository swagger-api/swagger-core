package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.callbacks.Callback;
import io.swagger.models.links.Link;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.security.SecurityScheme;

import java.util.Map;
import java.util.Objects;

/**
 * Components
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class Components {
  @JsonProperty("schemas")
  private Map<String, Schema> schemas = null;

  @JsonProperty("responses")
  private Map<String, Response> responses = null;

  @JsonProperty("parameters")
  private Map<String, Parameter> parameters = null;

  @JsonProperty("examples")
  private Map<String, Example> examples = null;

  @JsonProperty("requestBodies")
  private Map<String, RequestBody> requestBodies = null;

  @JsonProperty("headers")
  private Map<String, Header> headers = null;

  @JsonProperty("securitySchemes")
  private Map<String, SecurityScheme> securitySchemes = null;

  @JsonProperty("links")
  private Map<String, Link> links = null;

  @JsonProperty("callbacks")
  private Map<String, Callback> callbacks = null;

  public Components schemas(Map<String, Schema> schemas) {
    this.schemas = schemas;
    return this;
  }

  public Components putSchemasItem(String key, Schema schemasItem) {
    this.schemas.put(key, schemasItem);
    return this;
  }

   /**
   * Get schemas
   * @return schemas
  **/
  @ApiModelProperty(value = "")
  public Map<String, Schema> getSchemas() {
    return schemas;
  }

  public void setSchemas(Map<String, Schema> schemas) {
    this.schemas = schemas;
  }

  public Components responses(Map<String, Response> responses) {
    this.responses = responses;
    return this;
  }

  public Components putResponsesItem(String key, Response responsesItem) {
    this.responses.put(key, responsesItem);
    return this;
  }

   /**
   * Get responses
   * @return responses
  **/
  @ApiModelProperty(value = "")
  public Map<String, Response> getResponses() {
    return responses;
  }

  public void setResponses(Map<String, Response> responses) {
    this.responses = responses;
  }

  public Components parameters(Map<String, Parameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  public Components putParametersItem(String key, Parameter parametersItem) {
    this.parameters.put(key, parametersItem);
    return this;
  }

   /**
   * Get parameters
   * @return parameters
  **/
  @ApiModelProperty(value = "")
  public Map<String, Parameter> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Parameter> parameters) {
    this.parameters = parameters;
  }

  public Components examples(Map<String, Example> examples) {
    this.examples = examples;
    return this;
  }

  public Components putExamplesItem(String key, Example examplesItem) {
    this.examples.put(key, examplesItem);
    return this;
  }

   /**
   * Get examples
   * @return examples
  **/
  @ApiModelProperty(value = "")
  public Map<String, Example> getExamples() {
    return examples;
  }

  public void setExamples(Map<String, Example> examples) {
    this.examples = examples;
  }

  public Components requestBodies(Map<String, RequestBody> requestBodies) {
    this.requestBodies = requestBodies;
    return this;
  }

  public Components putRequestBodiesItem(String key, RequestBody requestBodiesItem) {
    this.requestBodies.put(key, requestBodiesItem);
    return this;
  }

   /**
   * Get requestBodies
   * @return requestBodies
  **/
  @ApiModelProperty(value = "")
  public Map<String, RequestBody> getRequestBodies() {
    return requestBodies;
  }

  public void setRequestBodies(Map<String, RequestBody> requestBodies) {
    this.requestBodies = requestBodies;
  }

  public Components headers(Map<String, Header> headers) {
    this.headers = headers;
    return this;
  }

  public Components putHeadersItem(String key, Header headersItem) {
    this.headers.put(key, headersItem);
    return this;
  }

   /**
   * Get headers
   * @return headers
  **/
  @ApiModelProperty(value = "")
  public Map<String, Header> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, Header> headers) {
    this.headers = headers;
  }

  public Components securitySchemes(Map<String, SecurityScheme> securitySchemes) {
    this.securitySchemes = securitySchemes;
    return this;
  }

  public Components putSecuritySchemesItem(String key, SecurityScheme securitySchemesItem) {
    this.securitySchemes.put(key, securitySchemesItem);
    return this;
  }

   /**
   * Get securitySchemes
   * @return securitySchemes
  **/
  @ApiModelProperty(value = "")
  public Map<String, SecurityScheme> getSecuritySchemes() {
    return securitySchemes;
  }

  public void setSecuritySchemes(Map<String, SecurityScheme> securitySchemes) {
    this.securitySchemes = securitySchemes;
  }

  public Components links(Map<String, Link> links) {
    this.links = links;
    return this;
  }

  public Components putLinksItem(String key, Link linksItem) {
    this.links.put(key, linksItem);
    return this;
  }

   /**
   * Get links
   * @return links
  **/
  @ApiModelProperty(value = "")
  public Map<String, Link> getLinks() {
    return links;
  }

  public void setLinks(Map<String, Link> links) {
    this.links = links;
  }

  public Components callbacks(Map<String, Callback> callbacks) {
    this.callbacks = callbacks;
    return this;
  }

  public Components putCallbacksItem(String key, Callback callbacksItem) {
    this.callbacks.put(key, callbacksItem);
    return this;
  }

   /**
   * Get callbacks
   * @return callbacks
  **/
  @ApiModelProperty(value = "")
  public Map<String, Callback> getCallbacks() {
    return callbacks;
  }

  public void setCallbacks(Map<String, Callback> callbacks) {
    this.callbacks = callbacks;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Components components = (Components) o;
    return Objects.equals(this.schemas, components.schemas) &&
        Objects.equals(this.responses, components.responses) &&
        Objects.equals(this.parameters, components.parameters) &&
        Objects.equals(this.examples, components.examples) &&
        Objects.equals(this.requestBodies, components.requestBodies) &&
        Objects.equals(this.headers, components.headers) &&
        Objects.equals(this.securitySchemes, components.securitySchemes) &&
        Objects.equals(this.links, components.links) &&
        Objects.equals(this.callbacks, components.callbacks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(schemas, responses, parameters, examples, requestBodies, headers, securitySchemes, links, callbacks);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Components {\n");
    
    sb.append("    schemas: ").append(toIndentedString(schemas)).append("\n");
    sb.append("    responses: ").append(toIndentedString(responses)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
    sb.append("    examples: ").append(toIndentedString(examples)).append("\n");
    sb.append("    requestBodies: ").append(toIndentedString(requestBodies)).append("\n");
    sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
    sb.append("    securitySchemes: ").append(toIndentedString(securitySchemes)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
    sb.append("    callbacks: ").append(toIndentedString(callbacks)).append("\n");
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

