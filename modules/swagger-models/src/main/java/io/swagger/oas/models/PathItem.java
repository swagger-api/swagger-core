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
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;

/**
 * PathItem
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc0/versions/3.0.md#pathItemObject"
 */


public class PathItem {
  private String ref = null;
  private String summary = null;
  private String description = null;
  private Operation get = null;
  private Operation put = null;
  private Operation post = null;
  private Operation delete = null;
  private Operation options = null;
  private Operation head = null;
  private Operation patch = null;
  private Operation trace = null;
  private List<Server> servers = null;
  private List<Parameter> parameters = null;
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the ref property from a PathItem instance.
   *
   * @return String ref
   **/

  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public PathItem ref(String ref) {
    this.ref = ref;
    return this;
  }

  /**
   * returns the summary property from a PathItem instance.
   *
   * @return String summary
   **/

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public PathItem summary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * returns the description property from a PathItem instance.
   *
   * @return String description
   **/

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public PathItem description(String description) {
    this.description = description;
    return this;
  }

  /**
   * returns the get property from a PathItem instance.
   *
   * @return Operation get
   **/

  public Operation getGet() {
    return get;
  }

  public void setGet(Operation get) {
    this.get = get;
  }

  public PathItem get(Operation get) {
    this.get = get;
    return this;
  }

  /**
   * returns the put property from a PathItem instance.
   *
   * @return Operation put
   **/

  public Operation getPut() {
    return put;
  }

  public void setPut(Operation put) {
    this.put = put;
  }

  public PathItem put(Operation put) {
    this.put = put;
    return this;
  }

  /**
   * returns the post property from a PathItem instance.
   *
   * @return Operation post
   **/

  public Operation getPost() {
    return post;
  }

  public void setPost(Operation post) {
    this.post = post;
  }

  public PathItem post(Operation post) {
    this.post = post;
    return this;
  }

  /**
   * returns the delete property from a PathItem instance.
   *
   * @return Operation delete
   **/

  public Operation getDelete() {
    return delete;
  }

  public void setDelete(Operation delete) {
    this.delete = delete;
  }

  public PathItem delete(Operation delete) {
    this.delete = delete;
    return this;
  }

  /**
   * returns the options property from a PathItem instance.
   *
   * @return Operation options
   **/

  public Operation getOptions() {
    return options;
  }

  public void setOptions(Operation options) {
    this.options = options;
  }

  public PathItem options(Operation options) {
    this.options = options;
    return this;
  }

  /**
   * returns the head property from a PathItem instance.
   *
   * @return Operation head
   **/

  public Operation getHead() {
    return head;
  }

  public void setHead(Operation head) {
    this.head = head;
  }

  public PathItem head(Operation head) {
    this.head = head;
    return this;
  }

  /**
   * returns the patch property from a PathItem instance.
   *
   * @return Operation patch
   **/

  public Operation getPatch() {
    return patch;
  }

  public void setPatch(Operation patch) {
    this.patch = patch;
  }

  public PathItem patch(Operation patch) {
    this.patch = patch;
    return this;
  }

  /**
   * returns the trace property from a PathItem instance.
   *
   * @return Operation trace
   **/

  public Operation getTrace() {
    return trace;
  }

  public void setTrace(Operation trace) {
    this.trace = trace;
  }

  public PathItem trace(Operation trace) {
    this.trace = trace;
    return this;
  }

  /**
   * returns the servers property from a PathItem instance.
   *
   * @return List<Server> servers
   **/

  public List<Server> getServers() {
    return servers;
  }

  public void setServers(List<Server> servers) {
    this.servers = servers;
  }

  public PathItem servers(List<Server> servers) {
    this.servers = servers;
    return this;
  }

  public PathItem addServersItem(Server serversItem) {
    if(this.servers == null) {
      this.servers = new ArrayList<Server>();
    }
    this.servers.add(serversItem);
    return this;
  }

  /**
   * returns the parameters property from a PathItem instance.
   *
   * @return List<Parameter> parameters
   **/

  public List<Parameter> getParameters() {
    return parameters;
  }

  public void setParameters(List<Parameter> parameters) {
    this.parameters = parameters;
  }

  public PathItem parameters(List<Parameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  public PathItem addParametersItem(Parameter parametersItem) {
    if(this.parameters == null) {
      this.parameters = new ArrayList<Parameter>();
    }
    this.parameters.add(parametersItem);
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

