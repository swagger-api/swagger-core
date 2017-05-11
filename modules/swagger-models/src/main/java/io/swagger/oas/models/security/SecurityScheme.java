/**
 * Copyright 2017 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.oas.models.security;

import java.util.Objects;

/**
 * SecurityScheme
 *
 * @link https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc0/versions/3.0.md#securitySchemeObject
 */


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
  }

  private TypeEnum type = null;
  private String description = null;
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
  }

  private InEnum in = null;
  private String scheme = null;
  private String bearerFormat = null;
  private OAuthFlows flows = null;
  private String openIdConnectUrl = null;
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the type property from a SecurityScheme instance.
   *
   * @return TypeEnum type
   **/

  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public SecurityScheme type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * returns the description property from a SecurityScheme instance.
   *
   * @return String description
   **/

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public SecurityScheme description(String description) {
    this.description = description;
    return this;
  }

  /**
   * returns the name property from a SecurityScheme instance.
   *
   * @return String name
   **/

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SecurityScheme name(String name) {
    this.name = name;
    return this;
  }

  /**
   * returns the in property from a SecurityScheme instance.
   *
   * @return InEnum in
   **/

  public InEnum getIn() {
    return in;
  }

  public void setIn(InEnum in) {
    this.in = in;
  }

  public SecurityScheme in(InEnum in) {
    this.in = in;
    return this;
  }

  /**
   * returns the scheme property from a SecurityScheme instance.
   *
   * @return String scheme
   **/

  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public SecurityScheme scheme(String scheme) {
    this.scheme = scheme;
    return this;
  }

  /**
   * returns the bearerFormat property from a SecurityScheme instance.
   *
   * @return String bearerFormat
   **/

  public String getBearerFormat() {
    return bearerFormat;
  }

  public void setBearerFormat(String bearerFormat) {
    this.bearerFormat = bearerFormat;
  }

  public SecurityScheme bearerFormat(String bearerFormat) {
    this.bearerFormat = bearerFormat;
    return this;
  }

  /**
   * returns the flows property from a SecurityScheme instance.
   *
   * @return OAuthFlows flows
   **/

  public OAuthFlows getFlows() {
    return flows;
  }

  public void setFlows(OAuthFlows flows) {
    this.flows = flows;
  }

  public SecurityScheme flows(OAuthFlows flows) {
    this.flows = flows;
    return this;
  }

  /**
   * returns the openIdConnectUrl property from a SecurityScheme instance.
   *
   * @return String openIdConnectUrl
   **/

  public String getOpenIdConnectUrl() {
    return openIdConnectUrl;
  }

  public void setOpenIdConnectUrl(String openIdConnectUrl) {
    this.openIdConnectUrl = openIdConnectUrl;
  }

  public SecurityScheme openIdConnectUrl(String openIdConnectUrl) {
    this.openIdConnectUrl = openIdConnectUrl;
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
    SecurityScheme securityScheme = (SecurityScheme) o;
    return Objects.equals(this.type, securityScheme.type) &&
        Objects.equals(this.description, securityScheme.description) &&
        Objects.equals(this.name, securityScheme.name) &&
        Objects.equals(this.in, securityScheme.in) &&
        Objects.equals(this.scheme, securityScheme.scheme) &&
        Objects.equals(this.bearerFormat, securityScheme.bearerFormat) &&
        Objects.equals(this.flows, securityScheme.flows) &&
        Objects.equals(this.openIdConnectUrl, securityScheme.openIdConnectUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, description, name, in, scheme, bearerFormat, flows, openIdConnectUrl);
  }


  public java.util.Map<String, Object> getExtensions() {
    return extensions;
  }

  public void addExtension(String name, Object value) {
    if(this.extensions == null) {
      this.extensions = new java.util.HashMap<>();
    }
    this.extensions.put(name, value);
  }

  public void setExtensions(java.util.Map<String, Object> extensions) {
    this.extensions = extensions;
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
    sb.append("    flows: ").append(toIndentedString(flows)).append("\n");
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

