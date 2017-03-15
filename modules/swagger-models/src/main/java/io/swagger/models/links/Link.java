package io.swagger.models.links;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.Headers;

/**
 * Link
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class Link {
  @JsonProperty("href")
  private String href = null;

  @JsonProperty("operationId")
  private String operationId = null;

  @JsonProperty("parameters")
  private LinkParameters parameters = null;

  @JsonProperty("headers")
  private Headers headers = null;

  @JsonProperty("description")
  private String description = null;

  public Link href(String href) {
    this.href = href;
    return this;
  }

   /**
   * Get href
   * @return href
  **/
  @ApiModelProperty(value = "")
  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public Link operationId(String operationId) {
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

  public Link parameters(LinkParameters parameters) {
    this.parameters = parameters;
    return this;
  }

   /**
   * Get parameters
   * @return parameters
  **/
  @ApiModelProperty(value = "")
  public LinkParameters getParameters() {
    return parameters;
  }

  public void setParameters(LinkParameters parameters) {
    this.parameters = parameters;
  }

  public Link headers(Headers headers) {
    this.headers = headers;
    return this;
  }

   /**
   * Get headers
   * @return headers
  **/
  @ApiModelProperty(value = "")
  public Headers getHeaders() {
    return headers;
  }

  public void setHeaders(Headers headers) {
    this.headers = headers;
  }

  public Link description(String description) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Link link = (Link) o;
    return Objects.equals(this.href, link.href) &&
        Objects.equals(this.operationId, link.operationId) &&
        Objects.equals(this.parameters, link.parameters) &&
        Objects.equals(this.headers, link.headers) &&
        Objects.equals(this.description, link.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(href, operationId, parameters, headers, description);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Link {\n");
    
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
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

