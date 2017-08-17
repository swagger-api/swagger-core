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

package io.swagger.oas.models.servers;

import java.util.Objects;

/**
 * Server
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc2/versions/3.0.md#serverObject"
 */

public class Server {
    private String url = null;
    private String description = null;
    private ServerVariables variables = null;
    private java.util.Map<String, Object> extensions = null;

    /**
     * returns the url property from a Server instance.
     *
     * @return String url
     **/

    public String getUrl() {
        return url;
    }

    /**
     * Sets the url property of a Server instance
     * to the parameter.
     * 
     * @param url
     */

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Sets the url property of a Server instance
     * to the parameter and returns the instance.
     * 
     * @param url
     * @return Server instance with the set url property.
     */

    public Server url(String url) {
        this.url = url;
        return this;
    }

    /**
     * returns the description property from a Server instance.
     *
     * @return String description
     **/

    public String getDescription() {
        return description;
    }

    /**
     * Sets the description property of a Server instance
     * to the parameter.
     * 
     * @param description
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description property of a Server instance
     * to the parameter and returns the instance.
     * 
     * @param description
     * @return Server instance with the set description property.
     */

    public Server description(String description) {
        this.description = description;
        return this;
    }

    /**
     * returns the variables property from a Server instance.
     *
     * @return ServerVariables variables
     **/

    public ServerVariables getVariables() {
        return variables;
    }

    /**
     * Sets the variables property of a Server instance
     * to the parameter.
     * 
     * @param variables
     */

    public void setVariables(ServerVariables variables) {
        this.variables = variables;
    }

    /**
     * Sets the variables property of a Server instance
     * to the parameter and returns the instance.
     * 
     * @param variables
     * @return Server instance with the set variables property.
     */

    public Server variables(ServerVariables variables) {
        this.variables = variables;
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
        Server server = (Server) o;
        return Objects.equals(this.url, server.url) &&
               Objects.equals(this.description, server.description) &&
               Objects.equals(this.variables, server.variables) &&
               Objects.equals(this.extensions, server.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, description, variables, extensions);
    }

    /**
     * Returns extensions property of a Server instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     */

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds an object item to extensions map of a Server instance
     * at the specified key.
     * If extensions is null, then creates a new HashMap and adds the item.
     *
     * @param name
     * @param value
     */

    public void addExtension(String name, Object value) {
        if (this.extensions == null) {
            this.extensions = new java.util.HashMap<>();
        }
        this.extensions.put(name, value);
    }

    /**
     * Sets extensions property of a Server instance
     * to the parameter.
     *
     * @param extensions
     */

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Server {\n");

        sb.append("    url: ").append(toIndentedString(url)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    variables: ").append(toIndentedString(variables)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Converts the given object to string with each line indented by 4 spaces
     * (except the first line).
     * This method adds formatting to the general toString() method.
     *
     * @param o Java object to be represented as String
     * @return Formatted String representation of the object
     */

    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
