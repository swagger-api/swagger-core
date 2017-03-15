package io.swagger.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * RequestBody
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class RequestBody {
  @JsonProperty("description")
  private String description = null;

  @JsonProperty("content")
  private Content content = null;

  @JsonProperty("required")
  private Boolean required = null;

  public RequestBody description(String description) {
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

  public RequestBody content(Content content) {
    this.content = content;
    return this;
  }

   /**
   * Get content
   * @return content
  **/
  @ApiModelProperty(value = "")
  public Content getContent() {
    return content;
  }

  public void setContent(Content content) {
    this.content = content;
  }

  public RequestBody required(Boolean required) {
    this.required = required;
    return this;
  }

   /**
   * Get required
   * @return required
  **/
  @ApiModelProperty(value = "")
  public Boolean getRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestBody requestBody = (RequestBody) o;
    return Objects.equals(this.description, requestBody.description) &&
        Objects.equals(this.content, requestBody.content) &&
        Objects.equals(this.required, requestBody.required);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, content, required);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestBody {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
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

