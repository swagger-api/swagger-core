package io.swagger.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * ServerVariable
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class ServerVariable {
  @JsonProperty("enum")
  private String _enum = null;

  @JsonProperty("default")
  private String _default = null;

  @JsonProperty("description")
  private String description = null;

  public ServerVariable _enum(String _enum) {
    this._enum = _enum;
    return this;
  }

   /**
   * Get _enum
   * @return _enum
  **/
  @ApiModelProperty(value = "")
  public String getEnum() {
    return _enum;
  }

  public void setEnum(String _enum) {
    this._enum = _enum;
  }

  public ServerVariable _default(String _default) {
    this._default = _default;
    return this;
  }

   /**
   * Get _default
   * @return _default
  **/
  @ApiModelProperty(required = true, value = "")
  public String getDefault() {
    return _default;
  }

  public void setDefault(String _default) {
    this._default = _default;
  }

  public ServerVariable description(String description) {
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
    ServerVariable serverVariable = (ServerVariable) o;
    return Objects.equals(this._enum, serverVariable._enum) &&
        Objects.equals(this._default, serverVariable._default) &&
        Objects.equals(this.description, serverVariable.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_enum, _default, description);
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

