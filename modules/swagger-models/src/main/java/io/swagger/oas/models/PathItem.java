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


import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.servers.Server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * PathItem
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.md#pathItemObject"
 */


public class PathItem {
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
    private String $ref = null;
    private java.util.Map<String, Object> extensions = null;

    /**
     * returns the summary property from a PathItem instance.
     *
     * @return String summary
     **/

    public String getSummary() {
        return summary;
    }

    /**
     * sets this PathItem's summary property to the given summary.
     *
     * @param String summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
    * sets this PathItem's summary property to the given summary and
    * returns this instance of PathItem
    *
    * @param String summary
    * @return PathItem
    */
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

    /**
     * sets this PathItem's description property to the given description.
     *
     * @param String description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * sets this PathItem's description property to the given description and
    * returns this instance of PathItem
    *
    * @param String description
    * @return PathItem
    */
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

    /**
     * sets this PathItem's get property to the given get.
     *
     * @param Operation get
     */
    public void setGet(Operation get) {
        this.get = get;
    }

    /**
    * sets this PathItem's get property to the given get and
    * returns this instance of PathItem
    *
    * @param Operation get
    * @return PathItem
    */
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

    /**
     * sets this PathItem's put property to the given put.
     *
     * @param Operation put
     */
    public void setPut(Operation put) {
        this.put = put;
    }

    /**
    * sets this PathItem's put property to the given put and
    * returns this instance of PathItem
    *
    * @param Operation put
    * @return PathItem
    */
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

    /**
     * sets this PathItem's post property to the given post.
     *
     * @param Operation post
     */
    public void setPost(Operation post) {
        this.post = post;
    }

    /**
    * sets this PathItem's post property to the given post and
    * returns this instance of PathItem
    *
    * @param Operation post
    * @return PathItem
    */
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

    /**
     * sets this PathItem's delete property to the given delete.
     *
     * @param Operation delete
     */
    public void setDelete(Operation delete) {
        this.delete = delete;
    }

    /**
    * sets this PathItem's delete property to the given delete and
    * returns this instance of PathItem
    *
    * @param Operation delete
    * @return PathItem
    */
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

    /**
     * sets this PathItem's options property to the given options.
     *
     * @param Operation options
     */
    public void setOptions(Operation options) {
        this.options = options;
    }

    /**
    * sets this PathItem's options property to the given options and
    * returns this instance of PathItem
    *
    * @param Operation options
    * @return PathItem
    */
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

    /**
     * sets this PathItem's head property to the given head.
     *
     * @param Operation head
     */
    public void setHead(Operation head) {
        this.head = head;
    }

    /**
    * sets this PathItem's head property to the given head and
    * returns this instance of PathItem
    *
    * @param Operation head
    * @return PathItem
    */
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

    /**
     * sets this PathItem's patch property to the given patch.
     *
     * @param Operation patch
     */
    public void setPatch(Operation patch) {
        this.patch = patch;
    }

    /**
    * sets this PathItem's patch property to the given patch and
    * returns this instance of PathItem
    *
    * @param Operation patch
    * @return PathItem
    */
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

    /**
     * sets this PathItem's trace property to the given trace.
     *
     * @param Operation trace
     */
    public void setTrace(Operation trace) {
        this.trace = trace;
    }

    /**
    * sets this PathItem's trace property to the given trace and
    * returns this instance of PathItem
    *
    * @param Operation trace
    * @return PathItem
    */
    public PathItem trace(Operation trace) {
        this.trace = trace;
        return this;
    }

    /**
     * Returns a list of all the operation for this path.
     * 
     * @return List&lt;Operation&gt; allOperations
     */
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

    /**
     * All of the possible types of methods for this path
     */
    public enum HttpMethod {
        POST,
        GET,
        PUT,
        PATCH,
        DELETE,
        HEAD,
        OPTIONS,
        TRACE
    }

    /**
     * Returns a map with all the operations for this path, where the HttpMethods are keys.
     * 
     * @return Map&lt;HttpMethod, Operation&gt; result
     */
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

    /**
     * returns the servers property from a PathItem instance.
     *
     * @return List&lt;Server&gt; servers
     **/

    public List<Server> getServers() {
        return servers;
    }

    /**
     * sets this PathItem's servers property to the given servers.
     *
     * @param List&lt;Server&gt; servers
     */
    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    /**
    * sets this PathItem's patch servers to the given servers and
    * returns this instance of PathItem
    *
    * @param List&lt;Server&gt; servers
    * @return PathItem
    */
    public PathItem servers(List<Server> servers) {
        this.servers = servers;
        return this;
    }

    /**
     * Adds the given serversItem to this PathItem's list of serversItem, with the given key as its key.
     *
     * @param String key
     * @param Server serversItem
     * @return PathItem
    */
    public PathItem addServersItem(Server serversItem) {
        if (this.servers == null) {
            this.servers = new ArrayList<Server>();
        }
        this.servers.add(serversItem);
        return this;
    }

    /**
     * returns the parameters property from a PathItem instance.
     *
     * @return List&lt;Parameter&gt; parameters
     **/

    public List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * sets this PathItem's parameters property to the given parameters.
     *
     * @param List&lt;Parameter&gt; servers
     */
    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    /**
     * sets this PathItem's patch parameters to the given parameters and
     * returns this instance of PathItem
     *
     * @param List&lt;Parameter&gt; servers
     * @return PathItem
     */
    public PathItem parameters(List<Parameter> parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * Adds the given parametersItem to this PathItem's list of parametersItem, with the given key as its key.
     *
     * @param String key
     * @param Parameter parametersItem
     * @return PathItem
    */
    public PathItem addParametersItem(Parameter parametersItem) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<Parameter>();
        }
        this.parameters.add(parametersItem);
        return this;
    }

    /**
     * returns the extensions property from a PathItem instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     **/
    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds the given extension to this PathItem's list of extension, with the given key as its key.
     *
     * @param String key
     * @param Object value
    */
    public void addExtension(String name, Object value) {
        if (this.extensions == null) {
            this.extensions = new java.util.HashMap<>();
        }
        this.extensions.put(name, value);
    }

    /**
     * sets this PathItem's patch extensions to the given extensions
     *
     * @param Map&lt;String, Object&gt; extensions
     **/
    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    /**
     * returns the ref property from a PathItem instance.
     *
     * @return String ref
     **/
    public String get$ref() {
        return $ref;
    }

    /**
     * sets this PathItem's $ref property to the given $ref.
     *
     * @param String $ref
     */
    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    /**
     * sets this PathItem's $ref parameters to the given $ref and
     * returns this instance of PathItem
     *
     * @param List&lt;String&gt; $ref
     * @return PathItem
     */
    public PathItem $ref(String $ref) {
        this.$ref = $ref;
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

        if (summary != null ? !summary.equals(pathItem.summary) : pathItem.summary != null) {
            return false;
        }
        if (description != null ? !description.equals(pathItem.description) : pathItem.description != null) {
            return false;
        }
        if (get != null ? !get.equals(pathItem.get) : pathItem.get != null) {
            return false;
        }
        if (put != null ? !put.equals(pathItem.put) : pathItem.put != null) {
            return false;
        }
        if (post != null ? !post.equals(pathItem.post) : pathItem.post != null) {
            return false;
        }
        if (delete != null ? !delete.equals(pathItem.delete) : pathItem.delete != null) {
            return false;
        }
        if (options != null ? !options.equals(pathItem.options) : pathItem.options != null) {
            return false;
        }
        if (head != null ? !head.equals(pathItem.head) : pathItem.head != null) {
            return false;
        }
        if (patch != null ? !patch.equals(pathItem.patch) : pathItem.patch != null) {
            return false;
        }
        if (trace != null ? !trace.equals(pathItem.trace) : pathItem.trace != null) {
            return false;
        }
        if (servers != null ? !servers.equals(pathItem.servers) : pathItem.servers != null) {
            return false;
        }
        if (parameters != null ? !parameters.equals(pathItem.parameters) : pathItem.parameters != null) {
            return false;
        }
        if ($ref != null ? !$ref.equals(pathItem.$ref) : pathItem.$ref != null) {
            return false;
        }
        return extensions != null ? extensions.equals(pathItem.extensions) : pathItem.extensions == null;

    }

    @Override
    public int hashCode() {
        int result = summary != null ? summary.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (get != null ? get.hashCode() : 0);
        result = 31 * result + (put != null ? put.hashCode() : 0);
        result = 31 * result + (post != null ? post.hashCode() : 0);
        result = 31 * result + (delete != null ? delete.hashCode() : 0);
        result = 31 * result + (options != null ? options.hashCode() : 0);
        result = 31 * result + (head != null ? head.hashCode() : 0);
        result = 31 * result + (patch != null ? patch.hashCode() : 0);
        result = 31 * result + (trace != null ? trace.hashCode() : 0);
        result = 31 * result + (servers != null ? servers.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + ($ref != null ? $ref.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }
   
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PathItem {\n");
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
        sb.append("    $ref: ").append(toIndentedString($ref)).append("\n");
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

