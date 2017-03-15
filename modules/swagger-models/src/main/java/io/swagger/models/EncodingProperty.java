package io.swagger.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModelProperty;

/**
 * EncodingProperty
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class EncodingProperty {
  @JsonProperty("contentType")
  private String contentType = null;

  @JsonProperty("headers")
  private Object headers = null;

  /**
   * Gets or Sets style
   */
  public enum StyleEnum {
    FORM("form"),
    
    SPACEDELIMITED("spaceDelimited"),
    
    PIPEDELIMITED("pipeDelimited"),
    
    DEEPOBJECT("deepObject");

    private String value;

    StyleEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StyleEnum fromValue(String text) {
      for (StyleEnum b : StyleEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("style")
  private StyleEnum style = null;

  @JsonProperty("explode")
  private Boolean explode = null;

  public EncodingProperty contentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

   /**
   * Get contentType
   * @return contentType
  **/
  @ApiModelProperty(value = "")
  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public EncodingProperty headers(Object headers) {
    this.headers = headers;
    return this;
  }

   /**
   * Get headers
   * @return headers
  **/
  @ApiModelProperty(value = "")
  public Object getHeaders() {
    return headers;
  }

  public void setHeaders(Object headers) {
    this.headers = headers;
  }

  public EncodingProperty style(StyleEnum style) {
    this.style = style;
    return this;
  }

   /**
   * Get style
   * @return style
  **/
  @ApiModelProperty(value = "")
  public StyleEnum getStyle() {
    return style;
  }

  public void setStyle(StyleEnum style) {
    this.style = style;
  }

  public EncodingProperty explode(Boolean explode) {
    this.explode = explode;
    return this;
  }

   /**
   * Get explode
   * @return explode
  **/
  @ApiModelProperty(value = "")
  public Boolean getExplode() {
    return explode;
  }

  public void setExplode(Boolean explode) {
    this.explode = explode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EncodingProperty encodingProperty = (EncodingProperty) o;
    return Objects.equals(this.contentType, encodingProperty.contentType) &&
        Objects.equals(this.headers, encodingProperty.headers) &&
        Objects.equals(this.style, encodingProperty.style) &&
        Objects.equals(this.explode, encodingProperty.explode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contentType, headers, style, explode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EncodingProperty {\n");
    
    sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
    sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
    sb.append("    style: ").append(toIndentedString(style)).append("\n");
    sb.append("    explode: ").append(toIndentedString(explode)).append("\n");
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

