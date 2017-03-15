package io.swagger.models.security;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * OAuthFlows
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class OAuthFlows {
  @JsonProperty("implicit")
  private OAuthFlow implicit = null;

  @JsonProperty("password")
  private OAuthFlow password = null;

  @JsonProperty("clientCredentials")
  private OAuthFlow clientCredentials = null;

  @JsonProperty("authorizationCode")
  private OAuthFlow authorizationCode = null;

  public OAuthFlows implicit(OAuthFlow implicit) {
    this.implicit = implicit;
    return this;
  }

   /**
   * Get implicit
   * @return implicit
  **/
  @ApiModelProperty(value = "")
  public OAuthFlow getImplicit() {
    return implicit;
  }

  public void setImplicit(OAuthFlow implicit) {
    this.implicit = implicit;
  }

  public OAuthFlows password(OAuthFlow password) {
    this.password = password;
    return this;
  }

   /**
   * Get password
   * @return password
  **/
  @ApiModelProperty(value = "")
  public OAuthFlow getPassword() {
    return password;
  }

  public void setPassword(OAuthFlow password) {
    this.password = password;
  }

  public OAuthFlows clientCredentials(OAuthFlow clientCredentials) {
    this.clientCredentials = clientCredentials;
    return this;
  }

   /**
   * Get clientCredentials
   * @return clientCredentials
  **/
  @ApiModelProperty(value = "")
  public OAuthFlow getClientCredentials() {
    return clientCredentials;
  }

  public void setClientCredentials(OAuthFlow clientCredentials) {
    this.clientCredentials = clientCredentials;
  }

  public OAuthFlows authorizationCode(OAuthFlow authorizationCode) {
    this.authorizationCode = authorizationCode;
    return this;
  }

   /**
   * Get authorizationCode
   * @return authorizationCode
  **/
  @ApiModelProperty(value = "")
  public OAuthFlow getAuthorizationCode() {
    return authorizationCode;
  }

  public void setAuthorizationCode(OAuthFlow authorizationCode) {
    this.authorizationCode = authorizationCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OAuthFlows oauthFlows = (OAuthFlows) o;
    return Objects.equals(this.implicit, oauthFlows.implicit) &&
        Objects.equals(this.password, oauthFlows.password) &&
        Objects.equals(this.clientCredentials, oauthFlows.clientCredentials) &&
        Objects.equals(this.authorizationCode, oauthFlows.authorizationCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implicit, password, clientCredentials, authorizationCode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OAuthFlows {\n");
    
    sb.append("    implicit: ").append(toIndentedString(implicit)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    clientCredentials: ").append(toIndentedString(clientCredentials)).append("\n");
    sb.append("    authorizationCode: ").append(toIndentedString(authorizationCode)).append("\n");
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

