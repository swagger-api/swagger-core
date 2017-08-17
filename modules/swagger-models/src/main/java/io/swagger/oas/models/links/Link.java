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

package io.swagger.oas.models.links;

import io.swagger.oas.models.headers.Header;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.oas.models.servers.Server;

import java.util.HashMap;
import java.util.Map;

/**
 * Link
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.md#linkObject"
 */


public class Link {
    private String operationRef = null;
    private String operationId = null;
    private Map<String, String> parameters = null;
    private RequestBody requestBody = null;
    private Map<String, Header> headers = null;
    private String description = null;
    private String $ref = null;
    private java.util.Map<String, Object> extensions = null;
    private Server server;

    /**
     * returns the server property from a Link instance.
     *
     * @return Server server
     **/

    public Server getServer() {
        return server;
    }

    /**
     * sets this Link's server property to the given server.
     *
     * @param Server server
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * sets this Link's server property to the given server and
     * returns this instance of Link
     *
     * @param Server server
     * @return Link
     */
    public Link server(Server server) {
        this.setServer(server);
        return this;
    }

    /**
     * returns the operationRef property from a Link instance.
     *
     * @return String operationRef
     **/

    public String getOperationRef() {
        return operationRef;
    }

    /**
     * sets this Link's operationRef property to the given operationRef.
     *
     * @param String operationRef
     */
    public void setOperationRef(String operationRef) {
        this.operationRef = operationRef;
    }

    /**
     * sets this Link's operationRef property to the given operationRef and
     * returns this instance of Link
     *
     * @param String operationRef
     * @return Link
     */
    public Link operationRef(String operationRef) {
        this.operationRef = operationRef;
        return this;
    }

    /**
     * returns the requestBody property from a Link instance.
     *
     * @return RequestBody requestBody
     **/

    public RequestBody getRequestBody() {
        return requestBody;
    }

    /**
     * sets this Link's requestBody property to the given requestBody.
     *
     * @param RequestBody requestBody
     */
    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    /**
     * sets this Link's requestBody property to the given requestBody and
     * returns this instance of Link
     *
     * @param RequestBody requestBody
     * @return Link
     */
    public Link requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    /**
     * returns this Link's requestBody property for this instance of Link.
     *
     * @param String operationId
     */
    public String getOperationId() {
        return operationId;
    }

    /**
     * sets this Link's operationId property to the given operationId.
     *
     * @param String operationId
     */
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * sets this Link's operationId property to the given operationId and
     * returns this instance of Link
     *
     * @param String operationId
     * @return Link
     */
    public Link operationId(String operationId) {
        this.operationId = operationId;
        return this;
    }

    /**
     * returns the parameters property from a Link instance.
     *
     * @return LinkParameters parameters
     **/
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * sets this Link's parameters property to the given parameters.
     *
     * @param LinkParameters parameters
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /**
     * sets this Link's parameter property to the given parameter and
     * returns this instance of Link
     *
     * @param String name
     * @param String parameter
     * @return Link
     */
    public Link parameters(String name, String parameter) {
        if (this.parameters == null) {
            this.parameters = new HashMap<>();
        }
        this.parameters.put(name, parameter);

        return this;
    }

    /**
     * returns the headers property from a Link instance.
     *
     * @return Headers headers
     **/

    public Map<String, Header> getHeaders() {
        return headers;
    }

    /**
     * sets this Link's headers property to the given headers.
     *
     * @param Map&lt;String, Header&gt; headers
     */
    public void setHeaders(Map<String, Header> headers) {
        this.headers = headers;
    }

    /**
     * sets this Link's headers property to the given headers and
     * returns this instance of Link
     *
     * @param Map&lt;String, Header&gt; headers
     * @return Link
     */
    public Link headers(Map<String, Header> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * Adds the given Header to this Link's map of headers, with the given name as its key.
     *
     * @param String name
     * @param Header header
     * @return Link
     */
    public Link addHeaderObject(String name, Header header) {
        if (this.headers == null) {
            headers = new HashMap<>();
        }
        headers.put(name, header);
        return this;
    }

    /**
     * returns the description property from a Link instance.
     *
     * @return String description
     **/

    public String getDescription() {
        return description;
    }

    /**
     * returns the description property from a Link instance.
     *
     * @return String description
     **/
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * sets this Link's description property to the given description and
     * returns this instance of Link
     *
     * @param String description
     * @return Link
     */
    public Link description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Link)) {
            return false;
        }

        Link link = (Link) o;

        if (operationRef != null ? !operationRef.equals(link.operationRef) : link.operationRef != null) {
            return false;
        }
        if (operationId != null ? !operationId.equals(link.operationId) : link.operationId != null) {
            return false;
        }
        if (parameters != null ? !parameters.equals(link.parameters) : link.parameters != null) {
            return false;
        }
        if (headers != null ? !headers.equals(link.headers) : link.headers != null) {
            return false;
        }
        if (description != null ? !description.equals(link.description) : link.description != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(link.extensions) : link.extensions != null) {
            return false;
        }
        return server != null ? server.equals(link.server) : link.server == null;

    }

    @Override
    public int hashCode() {
        int result = operationRef != null ? operationRef.hashCode() : 0;
        result = 31 * result + (operationId != null ? operationId.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        result = 31 * result + (server != null ? server.hashCode() : 0);
        return result;
    }

    /**
     * returns the $ref property from a Link instance.
     *
     * @return String $ref
     **/
    public String get$ref() {
        return $ref;
    }

    /**
     * sets the $ref property for a Link instance.
     *
     * @param String $ref
     **/
    public void set$ref(String $ref) {
        if ($ref != null && ($ref.indexOf(".") == -1 && $ref.indexOf("/") == -1)) {
            $ref = "#/components/links/" + $ref;
        }
        this.$ref = $ref;
    }

    /**
     * sets this Link's $ref property to the given description and
     * returns this instance of Link
     *
     * @param String $ref 
     * @return Link
     */
    public Link $ref(String $ref) {
        this.$ref = $ref;
        return this;
    }

    /**
     * returns the extensions property from a Link instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     */
    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds the given Object to this Link's map of extensions, with the given name as its key.
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
     * sets the extensions property for a Link instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     */
    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Link {\n");

        sb.append("    operationRef: ").append(toIndentedString(operationRef)).append("\n");
        sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
        sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
        sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

