/**
 * Copyright 2017 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.swagger.v3.oas.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;

/**
 * PathItem
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#pathItemObject"
 */

public class PathItem {
  public enum HttpMethod {
    DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE
  }

  private String $ref = null;
  private Operation delete = null;
  private String description = null;
  private java.util.Map<String, Object> extensions = null;
  private Operation get = null;
  private Operation head = null;
  private Operation options = null;
  private List<Parameter> parameters = null;
  private Operation patch = null;
  private Operation post = null;
  private Operation put = null;
  private List<Server> servers = null;
  private String summary = null;

  private Operation trace = null;

  public PathItem $ref(String $ref) {
    set$ref($ref);
    return this;
  }

  public void addExtension(String name, Object value) {
    if (name == null || name.isEmpty() || !name.startsWith("x-")) {
      return;
    }
    if (this.extensions == null) {
      this.extensions = new java.util.LinkedHashMap<>();
    }
    this.extensions.put(name, value);
  }

  public PathItem addParametersItem(Parameter parametersItem) {
    if (this.parameters == null) {
      this.parameters = new ArrayList<>();
    }
    this.parameters.add(parametersItem);
    return this;
  }

  public PathItem addServersItem(Server serversItem) {
    if (this.servers == null) {
      this.servers = new ArrayList<>();
    }
    this.servers.add(serversItem);
    return this;
  }

  public PathItem delete(Operation delete) {
    setDelete(delete);
    return this;
  }

  public PathItem description(String description) {
    this.description = description;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PathItem)) {
      return false;
    }

    PathItem pathItem = (PathItem) o;

    if (this.summary != null ? !this.summary.equals(pathItem.summary) : pathItem.summary != null) {
      return false;
    }
    if (this.description != null ? !this.description.equals(pathItem.description)
        : pathItem.description != null) {
      return false;
    }
    if (this.get != null ? !this.get.equals(pathItem.get) : pathItem.get != null) {
      return false;
    }
    if (this.put != null ? !this.put.equals(pathItem.put) : pathItem.put != null) {
      return false;
    }
    if (this.post != null ? !this.post.equals(pathItem.post) : pathItem.post != null) {
      return false;
    }
    if (this.delete != null ? !this.delete.equals(pathItem.delete) : pathItem.delete != null) {
      return false;
    }
    if (this.options != null ? !this.options.equals(pathItem.options) : pathItem.options != null) {
      return false;
    }
    if (this.head != null ? !this.head.equals(pathItem.head) : pathItem.head != null) {
      return false;
    }
    if (this.patch != null ? !this.patch.equals(pathItem.patch) : pathItem.patch != null) {
      return false;
    }
    if (this.trace != null ? !this.trace.equals(pathItem.trace) : pathItem.trace != null) {
      return false;
    }
    if (this.servers != null ? !this.servers.equals(pathItem.servers) : pathItem.servers != null) {
      return false;
    }
    if (this.parameters != null ? !this.parameters.equals(pathItem.parameters)
        : pathItem.parameters != null) {
      return false;
    }
    if (this.$ref != null ? !this.$ref.equals(pathItem.$ref) : pathItem.$ref != null) {
      return false;
    }
    return this.extensions != null ? this.extensions.equals(pathItem.extensions)
        : pathItem.extensions == null;

  }

  public PathItem extensions(java.util.Map<String, Object> extensions) {
    this.extensions = extensions;
    return this;
  }

  public PathItem get(Operation get) {
    setGet(get);
    return this;
  }

  /**
   * returns the ref property from a PathItem instance.
   *
   * @return String ref
   **/
  public String get$ref() {
    return this.$ref;
  }

  /**
   * returns the delete property from a PathItem instance.
   *
   * @return Operation delete
   **/

  public Operation getDelete() {
    return this.delete;
  }

  /**
   * returns the description property from a PathItem instance.
   *
   * @return String description
   **/

  public String getDescription() {
    return this.description;
  }

  public java.util.Map<String, Object> getExtensions() {
    return this.extensions;
  }

  /**
   * returns the get property from a PathItem instance.
   *
   * @return Operation get
   **/

  public Operation getGet() {
    return this.get;
  }

  /**
   * returns the head property from a PathItem instance.
   *
   * @return Operation head
   **/

  public Operation getHead() {
    return this.head;
  }

  /**
   * returns the options property from a PathItem instance.
   *
   * @return Operation options
   **/

  public Operation getOptions() {
    return this.options;
  }

  /**
   * returns the parameters property from a PathItem instance.
   *
   * @return List&lt;Parameter&gt; parameters
   **/

  public List<Parameter> getParameters() {
    return this.parameters;
  }

  /**
   * returns the patch property from a PathItem instance.
   *
   * @return Operation patch
   **/

  public Operation getPatch() {
    return this.patch;
  }

  /**
   * returns the post property from a PathItem instance.
   *
   * @return Operation post
   **/

  public Operation getPost() {
    return this.post;
  }

  /**
   * returns the put property from a PathItem instance.
   *
   * @return Operation put
   **/

  public Operation getPut() {
    return this.put;
  }

  /**
   * returns the servers property from a PathItem instance.
   *
   * @return List&lt;Server&gt; servers
   **/

  public List<Server> getServers() {
    return this.servers;
  }

  /**
   * returns the summary property from a PathItem instance.
   *
   * @return String summary
   **/

  public String getSummary() {
    return this.summary;
  }

  /**
   * returns the trace property from a PathItem instance.
   *
   * @return Operation trace
   **/

  public Operation getTrace() {
    return this.trace;
  }

  @Override
  public int hashCode() {
    int result = this.summary != null ? this.summary.hashCode() : 0;
    result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
    result = 31 * result + (this.get != null ? this.get.hashCode() : 0);
    result = 31 * result + (this.put != null ? this.put.hashCode() : 0);
    result = 31 * result + (this.post != null ? this.post.hashCode() : 0);
    result = 31 * result + (this.delete != null ? this.delete.hashCode() : 0);
    result = 31 * result + (this.options != null ? this.options.hashCode() : 0);
    result = 31 * result + (this.head != null ? this.head.hashCode() : 0);
    result = 31 * result + (this.patch != null ? this.patch.hashCode() : 0);
    result = 31 * result + (this.trace != null ? this.trace.hashCode() : 0);
    result = 31 * result + (this.servers != null ? this.servers.hashCode() : 0);
    result = 31 * result + (this.parameters != null ? this.parameters.hashCode() : 0);
    result = 31 * result + (this.$ref != null ? this.$ref.hashCode() : 0);
    result = 31 * result + (this.extensions != null ? this.extensions.hashCode() : 0);
    return result;
  }

  public PathItem head(Operation head) {
    setHead(head);
    return this;
  }

  public void operation(HttpMethod method, Operation operation) {
    switch (method) {
      case PATCH:
        this.patch = operation;
        break;
      case POST:
        this.post = operation;
        break;
      case PUT:
        this.put = operation;
        break;
      case GET:
        this.get = operation;
        break;
      case OPTIONS:
        this.options = operation;
        break;
      case TRACE:
        this.trace = operation;
        break;
      case HEAD:
        this.head = operation;
        break;
      case DELETE:
        this.delete = operation;
        break;
      default:
    }
  }

  public PathItem options(Operation options) {
    setOptions(options);
    return this;
  }

  public PathItem parameters(List<Parameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  public PathItem patch(Operation patch) {
    setPatch(patch);
    return this;
  }

  public PathItem post(Operation post) {
    setPost(post);
    return this;
  }

  public PathItem put(Operation put) {
    setPut(put);
    return this;
  }

  public List<Operation> readOperations() {
    List<Operation> allOperations = new ArrayList<>();
    if (this.get != null) {
      allOperations.add(this.get);
    }
    if (this.put != null) {
      allOperations.add(this.put);
    }
    if (this.head != null) {
      allOperations.add(this.head);
    }
    if (this.post != null) {
      allOperations.add(this.post);
    }
    if (this.delete != null) {
      allOperations.add(this.delete);
    }
    if (this.patch != null) {
      allOperations.add(this.patch);
    }
    if (this.options != null) {
      allOperations.add(this.options);
    }
    if (this.trace != null) {
      allOperations.add(this.trace);
    }

    return allOperations;
  }

  public Map<HttpMethod, Operation> readOperationsMap() {
    Map<HttpMethod, Operation> result = new LinkedHashMap<>();

    if (this.get != null) {
      result.put(HttpMethod.GET, this.get);
    }
    if (this.put != null) {
      result.put(HttpMethod.PUT, this.put);
    }
    if (this.post != null) {
      result.put(HttpMethod.POST, this.post);
    }
    if (this.delete != null) {
      result.put(HttpMethod.DELETE, this.delete);
    }
    if (this.patch != null) {
      result.put(HttpMethod.PATCH, this.patch);
    }
    if (this.head != null) {
      result.put(HttpMethod.HEAD, this.head);
    }
    if (this.options != null) {
      result.put(HttpMethod.OPTIONS, this.options);
    }
    if (this.trace != null) {
      result.put(HttpMethod.TRACE, this.trace);
    }

    return result;
  }

  public PathItem servers(List<Server> servers) {
    this.servers = servers;
    return this;
  }

  public void set$ref(String $ref) {
    this.$ref = $ref;
  }

  public void setDelete(Operation delete) {
    preSet(this.delete, delete);
    this.delete = delete;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setExtensions(java.util.Map<String, Object> extensions) {
    this.extensions = extensions;
  }

  public void setGet(Operation get) {
    this.get= preSet(this.get, get);
  }

  public void setHead(Operation head) {
    this.head = preSet(this.head, head);
  }

  public void setOptions(Operation options) {
    this.options = preSet(this.options, options);
  }

  public void setParameters(List<Parameter> parameters) {
    this.parameters = parameters;
  }

  public void setPatch(Operation patch) {
    this.patch = preSet(this.patch, patch);
  }

  /**
   * If operations are the same but
   * @param operation
   * @param newOperation
   */
  private Operation preSet(Operation operation, Operation newOperation) {
    String newOperationId = newOperation.getOperationId();
    System.out.println(newOperationId);
    if (operation != null) {
      System.out.println(operation.getOperationId());
      //check if the operation is already defined in order reuse the same operationId
      //changing operationId just to check if the operation is already the same as a
      newOperation.setOperationId(operation.getOperationId());
      if (!newOperation.equals(operation)) {
        newOperation.setOperationId(newOperationId);
        return newOperation;
      } else {
        System.out.println("same method!");

        String operationId=(newOperation.getOperationId().compareTo(newOperationId)>0)? newOperationId:newOperation.getOperationId();

        operation.setOperationId(operationId);
        return operation;
      }
    }
    return newOperation;
  }

  public void setPost(Operation post) {
    this.post = preSet(this.post, post);
  }

  public void setPut(Operation put) {
    this.put = preSet(this.put, put);
  }

  public void setServers(List<Server> servers) {
    this.servers = servers;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public void setTrace(Operation trace) {
    this.trace = preSet(this.trace, trace);
  }

  public PathItem summary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PathItem {\n");
    sb.append("    summary: ").append(toIndentedString(this.summary)).append("\n");
    sb.append("    description: ").append(toIndentedString(this.description)).append("\n");
    sb.append("    get: ").append(toIndentedString(this.get)).append("\n");
    sb.append("    put: ").append(toIndentedString(this.put)).append("\n");
    sb.append("    post: ").append(toIndentedString(this.post)).append("\n");
    sb.append("    delete: ").append(toIndentedString(this.delete)).append("\n");
    sb.append("    options: ").append(toIndentedString(this.options)).append("\n");
    sb.append("    head: ").append(toIndentedString(this.head)).append("\n");
    sb.append("    patch: ").append(toIndentedString(this.patch)).append("\n");
    sb.append("    trace: ").append(toIndentedString(this.trace)).append("\n");
    sb.append("    servers: ").append(toIndentedString(this.servers)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(this.parameters)).append("\n");
    sb.append("    $ref: ").append(toIndentedString(this.$ref)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  public PathItem trace(Operation trace) {
    setTrace(trace);
    return this;
  }

}

