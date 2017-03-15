package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.security.SecurityRequirement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * it works
 */
@ApiModel(description = "it works")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class OpenAPI {
  @JsonProperty("openapi")
  private String openapi = null;

  @JsonProperty("info")
  private Info info = null;

  @JsonProperty("servers")
  private List<Server> servers = null;

  @JsonProperty("paths")
  private Paths paths = null;

  @JsonProperty("components")
  private Components components = null;

  @JsonProperty("security")
  private List<SecurityRequirement> security = null;

  @JsonProperty("tags")
  private List<Tag> tags = null;

  @JsonProperty("externalDocs")
  private ExternalDocumentation externalDocs = null;

  public OpenAPI openapi(String openapi) {
    this.openapi = openapi;
    return this;
  }

   /**
   * Get openapi
   * @return openapi
  **/
  @ApiModelProperty(required = true, value = "")
  public String getOpenapi() {
    return openapi;
  }

  public void setOpenapi(String openapi) {
    this.openapi = openapi;
  }

  public OpenAPI info(Info info) {
    this.info = info;
    return this;
  }

   /**
   * Get info
   * @return info
  **/
  @ApiModelProperty(required = true, value = "")
  public Info getInfo() {
    return info;
  }

  public void setInfo(Info info) {
    this.info = info;
  }

  public OpenAPI servers(List<Server> servers) {
    this.servers = servers;
    return this;
  }

  public OpenAPI addServersItem(Server serversItem) {
    this.servers.add(serversItem);
    return this;
  }

   /**
   * Get servers
   * @return servers
  **/
  @ApiModelProperty(value = "")
  public List<Server> getServers() {
    return servers;
  }

  public void setServers(List<Server> servers) {
    this.servers = servers;
  }

  public OpenAPI paths(Paths paths) {
    this.paths = paths;
    return this;
  }

   /**
   * Get paths
   * @return paths
  **/
  @ApiModelProperty(required = true, value = "")
  public Paths getPaths() {
    return paths;
  }

  public void setPaths(Paths paths) {
    this.paths = paths;
  }

  public OpenAPI components(Components components) {
    this.components = components;
    return this;
  }

   /**
   * Get components
   * @return components
  **/
  @ApiModelProperty(value = "")
  public Components getComponents() {
    return components;
  }

  public void setComponents(Components components) {
    this.components = components;
  }

  public OpenAPI security(List<SecurityRequirement> security) {
    this.security = security;
    return this;
  }

  public OpenAPI addSecurityItem(SecurityRequirement securityItem) {
    this.security.add(securityItem);
    return this;
  }

   /**
   * Get security
   * @return security
  **/
  @ApiModelProperty(value = "")
  public List<SecurityRequirement> getSecurity() {
    return security;
  }

  public void setSecurity(List<SecurityRequirement> security) {
    this.security = security;
  }

  public OpenAPI tags(List<Tag> tags) {
    this.tags = tags;
    return this;
  }

  public OpenAPI addTagsItem(Tag tagsItem) {
    if(this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

   /**
   * Get tags
   * @return tags
  **/
  @ApiModelProperty(value = "")
  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public OpenAPI externalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
    return this;
  }

   /**
   * Get externalDocs
   * @return externalDocs
  **/
  @ApiModelProperty(value = "")
  public ExternalDocumentation getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
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
        Objects.equals(this.servers, openAPI.servers) &&
        Objects.equals(this.paths, openAPI.paths) &&
        Objects.equals(this.components, openAPI.components) &&
        Objects.equals(this.security, openAPI.security) &&
        Objects.equals(this.tags, openAPI.tags) &&
        Objects.equals(this.externalDocs, openAPI.externalDocs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(openapi, info, servers, paths, components, security, tags, externalDocs);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OpenAPI {\n");
    
    sb.append("    openapi: ").append(toIndentedString(openapi)).append("\n");
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
    sb.append("    servers: ").append(toIndentedString(servers)).append("\n");
    sb.append("    paths: ").append(toIndentedString(paths)).append("\n");
    sb.append("    components: ").append(toIndentedString(components)).append("\n");
    sb.append("    security: ").append(toIndentedString(security)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    externalDocs: ").append(toIndentedString(externalDocs)).append("\n");
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

