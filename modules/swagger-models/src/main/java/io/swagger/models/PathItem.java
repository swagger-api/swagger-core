package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.parameters.Parameter;

import java.util.List;
import java.util.Objects;

/**
 * PathItem
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class PathItem {
  @JsonProperty("_$ref")
  private String ref = null;

  @JsonProperty("summary")
  private String summary = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("get")
  private Operation get = null;

  @JsonProperty("put")
  private Operation put = null;

  @JsonProperty("post")
  private Operation post = null;

  @JsonProperty("delete")
  private Operation delete = null;

  @JsonProperty("options")
  private Operation options = null;

  @JsonProperty("head")
  private Operation head = null;

  @JsonProperty("patch")
  private Operation patch = null;

  @JsonProperty("trace")
  private Operation trace = null;

  @JsonProperty("servers")
  private List<Server> servers = null;

  @JsonProperty("parameters")
  private List<Parameter> parameters = null;

  public PathItem ref(String ref) {
    this.ref = ref;
    return this;
  }

   /**
   * Get ref
   * @return ref
  **/
  @ApiModelProperty(value = "")
  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public PathItem summary(String summary) {
    this.summary = summary;
    return this;
  }

   /**
   * Get summary
   * @return summary
  **/
  @ApiModelProperty(value = "")
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public PathItem description(String description) {
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

  public PathItem get(Operation get) {
    this.get = get;
    return this;
  }

   /**
   * Get get
   * @return get
  **/
  @ApiModelProperty(value = "")
  public Operation getGet() {
    return get;
  }

  public void setGet(Operation get) {
    this.get = get;
  }

  public PathItem put(Operation put) {
    this.put = put;
    return this;
  }

   /**
   * Get put
   * @return put
  **/
  @ApiModelProperty(value = "")
  public Operation getPut() {
    return put;
  }

  public void setPut(Operation put) {
    this.put = put;
  }

  public PathItem post(Operation post) {
    this.post = post;
    return this;
  }

   /**
   * Get post
   * @return post
  **/
  @ApiModelProperty(value = "")
  public Operation getPost() {
    return post;
  }

  public void setPost(Operation post) {
    this.post = post;
  }

  public PathItem delete(Operation delete) {
    this.delete = delete;
    return this;
  }

   /**
   * Get delete
   * @return delete
  **/
  @ApiModelProperty(value = "")
  public Operation getDelete() {
    return delete;
  }

  public void setDelete(Operation delete) {
    this.delete = delete;
  }

  public PathItem options(Operation options) {
    this.options = options;
    return this;
  }

   /**
   * Get options
   * @return options
  **/
  @ApiModelProperty(value = "")
  public Operation getOptions() {
    return options;
  }

  public void setOptions(Operation options) {
    this.options = options;
  }

  public PathItem head(Operation head) {
    this.head = head;
    return this;
  }

   /**
   * Get head
   * @return head
  **/
  @ApiModelProperty(value = "")
  public Operation getHead() {
    return head;
  }

  public void setHead(Operation head) {
    this.head = head;
  }

  public PathItem patch(Operation patch) {
    this.patch = patch;
    return this;
  }

   /**
   * Get patch
   * @return patch
  **/
  @ApiModelProperty(value = "")
  public Operation getPatch() {
    return patch;
  }

  public void setPatch(Operation patch) {
    this.patch = patch;
  }

  public PathItem trace(Operation trace) {
    this.trace = trace;
    return this;
  }

   /**
   * Get trace
   * @return trace
  **/
  @ApiModelProperty(value = "")
  public Operation getTrace() {
    return trace;
  }

  public void setTrace(Operation trace) {
    this.trace = trace;
  }

  public PathItem servers(List<Server> servers) {
    this.servers = servers;
    return this;
  }

  public PathItem addServersItem(Server serversItem) {
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

  public PathItem parameters(List<Parameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  public PathItem addParametersItem(Parameter parametersItem) {
    this.parameters.add(parametersItem);
    return this;
  }

   /**
   * Get parameters
   * @return parameters
  **/
  @ApiModelProperty(value = "")
  public List<Parameter> getParameters() {
    return parameters;
  }

  public void setParameters(List<Parameter> parameters) {
    this.parameters = parameters;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PathItem pathItem = (PathItem) o;
    return Objects.equals(this.ref, pathItem.ref) &&
        Objects.equals(this.summary, pathItem.summary) &&
        Objects.equals(this.description, pathItem.description) &&
        Objects.equals(this.get, pathItem.get) &&
        Objects.equals(this.put, pathItem.put) &&
        Objects.equals(this.post, pathItem.post) &&
        Objects.equals(this.delete, pathItem.delete) &&
        Objects.equals(this.options, pathItem.options) &&
        Objects.equals(this.head, pathItem.head) &&
        Objects.equals(this.patch, pathItem.patch) &&
        Objects.equals(this.trace, pathItem.trace) &&
        Objects.equals(this.servers, pathItem.servers) &&
        Objects.equals(this.parameters, pathItem.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ref, summary, description, get, put, post, delete, options, head, patch, trace, servers, parameters);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PathItem {\n");
    
    sb.append("    ref: ").append(toIndentedString(ref)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    get: ").append(toIndentedString(get)).append("\n");
    sb.append("    put: ").append(toIndentedString(put)).append("\n");
    sb.append("    post: ").append(toIndentedString(post)).append("\n");
    sb.append("    delete: ").append(toIndentedString(delete)).append("\n");
    sb.append("    options: ").append(toIndentedString(options)).append("\n");
    sb.append("    head: ").append(toIndentedString(head)).append("\n");
    sb.append("    patch: ").append(toIndentedString(patch)).append("\n");
    sb.append("    trace: ").append(toIndentedString(trace)).append("\n");
    sb.append("    servers: ").append(toIndentedString(servers)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
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

