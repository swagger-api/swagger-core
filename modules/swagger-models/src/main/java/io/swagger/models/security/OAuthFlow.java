package io.swagger.models.security;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * OAuthFlow
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class OAuthFlow {
  @JsonProperty("authorizationUrl")
  private String authorizationUrl = null;

  @JsonProperty("tokenUrl")
  private String tokenUrl = null;

  @JsonProperty("refreshUrl")
  private String refreshUrl = null;

  @JsonProperty("scopes")
  private Scopes scopes = null;

  public OAuthFlow authorizationUrl(String authorizationUrl) {
    this.authorizationUrl = authorizationUrl;
    return this;
  }

   /**
   * Get authorizationUrl
   * @return authorizationUrl
  **/
  @ApiModelProperty(value = "")
  public String getAuthorizationUrl() {
    return authorizationUrl;
  }

  public void setAuthorizationUrl(String authorizationUrl) {
    this.authorizationUrl = authorizationUrl;
  }

  public OAuthFlow tokenUrl(String tokenUrl) {
    this.tokenUrl = tokenUrl;
    return this;
  }

   /**
   * Get tokenUrl
   * @return tokenUrl
  **/
  @ApiModelProperty(value = "")
  public String getTokenUrl() {
    return tokenUrl;
  }

  public void setTokenUrl(String tokenUrl) {
    this.tokenUrl = tokenUrl;
  }

  public OAuthFlow refreshUrl(String refreshUrl) {
    this.refreshUrl = refreshUrl;
    return this;
  }

   /**
   * Get refreshUrl
   * @return refreshUrl
  **/
  @ApiModelProperty(value = "")
  public String getRefreshUrl() {
    return refreshUrl;
  }

  public void setRefreshUrl(String refreshUrl) {
    this.refreshUrl = refreshUrl;
  }

  public OAuthFlow scopes(Scopes scopes) {
    this.scopes = scopes;
    return this;
  }

   /**
   * Get scopes
   * @return scopes
  **/
  @ApiModelProperty(required = true, value = "")
  public Scopes getScopes() {
    return scopes;
  }

  public void setScopes(Scopes scopes) {
    this.scopes = scopes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OAuthFlow oauthFlow = (OAuthFlow) o;
    return Objects.equals(this.authorizationUrl, oauthFlow.authorizationUrl) &&
        Objects.equals(this.tokenUrl, oauthFlow.tokenUrl) &&
        Objects.equals(this.refreshUrl, oauthFlow.refreshUrl) &&
        Objects.equals(this.scopes, oauthFlow.scopes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorizationUrl, tokenUrl, refreshUrl, scopes);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OAuthFlow {\n");
    
    sb.append("    authorizationUrl: ").append(toIndentedString(authorizationUrl)).append("\n");
    sb.append("    tokenUrl: ").append(toIndentedString(tokenUrl)).append("\n");
    sb.append("    refreshUrl: ").append(toIndentedString(refreshUrl)).append("\n");
    sb.append("    scopes: ").append(toIndentedString(scopes)).append("\n");
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

