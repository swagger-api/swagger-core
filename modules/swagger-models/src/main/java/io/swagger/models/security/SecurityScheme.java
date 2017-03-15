package io.swagger.models.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * SecurityScheme
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class SecurityScheme {
  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    APIKEY("apiKey"),
    
    HTTP("http"),
    
    OAUTH2("oauth2"),
    
    OPENIDCONNECT("openIdConnect");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("name")
  private String name = null;

  /**
   * Gets or Sets in
   */
  public enum InEnum {
    HEADER("header"),
    
    QUERY("query");

    private String value;

    InEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static InEnum fromValue(String text) {
      for (InEnum b : InEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("in")
  private InEnum in = null;

  @JsonProperty("scheme")
  private String scheme = null;

  @JsonProperty("bearerFormat")
  private String bearerFormat = null;

  @JsonProperty("flow")
  private OAuthFlows flow = null;

  @JsonProperty("openIdConnectUrl")
  private String openIdConnectUrl = null;

  public SecurityScheme type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(required = true, value = "")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public SecurityScheme description(String description) {
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

  public SecurityScheme name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(required = true, value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SecurityScheme in(InEnum in) {
    this.in = in;
    return this;
  }

   /**
   * Get in
   * @return in
  **/
  @ApiModelProperty(value = "")
  public InEnum getIn() {
    return in;
  }

  public void setIn(InEnum in) {
    this.in = in;
  }

  public SecurityScheme scheme(String scheme) {
    this.scheme = scheme;
    return this;
  }

   /**
   * Get scheme
   * @return scheme
  **/
  @ApiModelProperty(value = "")
  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public SecurityScheme bearerFormat(String bearerFormat) {
    this.bearerFormat = bearerFormat;
    return this;
  }

   /**
   * Get bearerFormat
   * @return bearerFormat
  **/
  @ApiModelProperty(value = "")
  public String getBearerFormat() {
    return bearerFormat;
  }

  public void setBearerFormat(String bearerFormat) {
    this.bearerFormat = bearerFormat;
  }

  public SecurityScheme flow(OAuthFlows flow) {
    this.flow = flow;
    return this;
  }

   /**
   * Get flow
   * @return flow
  **/
  @ApiModelProperty(value = "")
  public OAuthFlows getFlow() {
    return flow;
  }

  public void setFlow(OAuthFlows flow) {
    this.flow = flow;
  }

  public SecurityScheme openIdConnectUrl(String openIdConnectUrl) {
    this.openIdConnectUrl = openIdConnectUrl;
    return this;
  }

   /**
   * Get openIdConnectUrl
   * @return openIdConnectUrl
  **/
  @ApiModelProperty(value = "")
  public String getOpenIdConnectUrl() {
    return openIdConnectUrl;
  }

  public void setOpenIdConnectUrl(String openIdConnectUrl) {
    this.openIdConnectUrl = openIdConnectUrl;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SecurityScheme securityScheme = (SecurityScheme) o;
    return Objects.equals(this.type, securityScheme.type) &&
        Objects.equals(this.description, securityScheme.description) &&
        Objects.equals(this.name, securityScheme.name) &&
        Objects.equals(this.in, securityScheme.in) &&
        Objects.equals(this.scheme, securityScheme.scheme) &&
        Objects.equals(this.bearerFormat, securityScheme.bearerFormat) &&
        Objects.equals(this.flow, securityScheme.flow) &&
        Objects.equals(this.openIdConnectUrl, securityScheme.openIdConnectUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, description, name, in, scheme, bearerFormat, flow, openIdConnectUrl);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SecurityScheme {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    in: ").append(toIndentedString(in)).append("\n");
    sb.append("    scheme: ").append(toIndentedString(scheme)).append("\n");
    sb.append("    bearerFormat: ").append(toIndentedString(bearerFormat)).append("\n");
    sb.append("    flow: ").append(toIndentedString(flow)).append("\n");
    sb.append("    openIdConnectUrl: ").append(toIndentedString(openIdConnectUrl)).append("\n");
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

