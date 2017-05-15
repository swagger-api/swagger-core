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

package io.swagger.oas.models;

import java.util.Objects;
import io.swagger.oas.models.info.Info;
import io.swagger.oas.models.security.SecurityRequirement;
import io.swagger.oas.models.servers.Server;
import io.swagger.oas.models.tags.Tag;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenAPI
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc0/versions/3.0.md#oasObject"
 */


public class OpenAPI {
  private String openapi = "3.0.0-rc1";
  private Info info = null;
  private ExternalDocumentation externalDocs = null;
  private List<Server> servers = null;
  private List<SecurityRequirement> security = null;
  private List<Tag> tags = null;
  private Paths paths = null;
  private Components components = null;
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the openapi property from a OpenAPI instance.
   *
   * @return String openapi
   **/

  public String getOpenapi() {
    return openapi;
  }

  public void setOpenapi(String openapi) {
    this.openapi = openapi;
  }

  public OpenAPI openapi(String openapi) {
    this.openapi = openapi;
    return this;
  }

  /**
   * returns the info property from a OpenAPI instance.
   *
   * @return Info info
   **/

  public Info getInfo() {
    return info;
  }

  public void setInfo(Info info) {
    this.info = info;
  }

  public OpenAPI info(Info info) {
    this.info = info;
    return this;
  }

  /**
   * returns the externalDocs property from a OpenAPI instance.
   *
   * @return ExternalDocumentation externalDocs
   **/

  public ExternalDocumentation getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
  }

  public OpenAPI externalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
    return this;
  }

  /**
   * Servers defined in the API
   *
   * @return List<Server> servers
   **/

  public List<Server> getServers() {
    return servers;
  }

  public void setServers(List<Server> servers) {
    this.servers = servers;
  }

  public OpenAPI servers(List<Server> servers) {
    this.servers = servers;
    return this;
  }

  public OpenAPI addServersItem(Server serversItem) {
    if(this.servers == null) {
      this.servers = new ArrayList<Server>();
    }
    this.servers.add(serversItem);
    return this;
  }

  /**
   * returns the security property from a OpenAPI instance.
   *
   * @return List<SecurityRequirement> security
   **/

  public List<SecurityRequirement> getSecurity() {
    return security;
  }

  public void setSecurity(List<SecurityRequirement> security) {
    this.security = security;
  }

  public OpenAPI security(List<SecurityRequirement> security) {
    this.security = security;
    return this;
  }

  public OpenAPI addSecurityItem(SecurityRequirement securityItem) {
    if(this.security == null) {
      this.security = new ArrayList<SecurityRequirement>();
    }
    this.security.add(securityItem);
    return this;
  }

  /**
   * returns the tags property from a OpenAPI instance.
   *
   * @return List<Tag> tags
   **/

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public OpenAPI tags(List<Tag> tags) {
    this.tags = tags;
    return this;
  }

  public OpenAPI addTagsItem(Tag tagsItem) {
    if(this.tags == null) {
      this.tags = new ArrayList<Tag>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * returns the paths property from a OpenAPI instance.
   *
   * @return Paths paths
   **/

  public Paths getPaths() {
    return paths;
  }

  public void setPaths(Paths paths) {
    this.paths = paths;
  }

  public OpenAPI paths(Paths paths) {
    this.paths = paths;
    return this;
  }

  /**
   * returns the components property from a OpenAPI instance.
   *
   * @return Components components
   **/

  public Components getComponents() {
    return components;
  }

  public void setComponents(Components components) {
    this.components = components;
  }

  public OpenAPI components(Components components) {
    this.components = components;
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
    OpenAPI openAPI = (OpenAPI) o;
    return Objects.equals(this.openapi, openAPI.openapi) &&
        Objects.equals(this.info, openAPI.info) &&
        Objects.equals(this.externalDocs, openAPI.externalDocs) &&
        Objects.equals(this.servers, openAPI.servers) &&
        Objects.equals(this.security, openAPI.security) &&
        Objects.equals(this.tags, openAPI.tags) &&
        Objects.equals(this.paths, openAPI.paths) &&
        Objects.equals(this.components, openAPI.components);
  }

  @Override
  public int hashCode() {
    return Objects.hash(openapi, info, externalDocs, servers, security, tags, paths, components);
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
    sb.append("class OpenAPI {\n");
    
    sb.append("    openapi: ").append(toIndentedString(openapi)).append("\n");
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
    sb.append("    externalDocs: ").append(toIndentedString(externalDocs)).append("\n");
    sb.append("    servers: ").append(toIndentedString(servers)).append("\n");
    sb.append("    security: ").append(toIndentedString(security)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    paths: ").append(toIndentedString(paths)).append("\n");
    sb.append("    components: ").append(toIndentedString(components)).append("\n");
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

