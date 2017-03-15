package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Objects;

/**
 * Responses
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class Responses extends HashMap<String, Response> {
  @JsonProperty("default")
  private Response _default = null;

  public Responses _default(Response _default) {
    this._default = _default;
    return this;
  }

   /**
   * Get _default
   * @return _default
  **/
  @ApiModelProperty(value = "")
  public Response getDefault() {
    return _default;
  }

  public void setDefault(Response _default) {
    this._default = _default;
  }

  public Responses addResponse(String key, Response response) {
    this.put(key, response);
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
    Responses responses = (Responses) o;
    return Objects.equals(this._default, responses._default) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_default, super.hashCode());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Responses {\n");
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

