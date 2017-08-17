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

package io.swagger.oas.models.media;

import java.util.Objects;

/**
 * XML
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc2/versions/3.0.md#xmlObject"
 */

public class XML {
    private String name = null;
    private String namespace = null;
    private String prefix = null;
    private Boolean attribute = null;
    private Boolean wrapped = null;
    private java.util.Map<String, Object> extensions = null;

    /**
     * returns the name property from a XML instance.
     *
     * @return String name
     **/

    public String getName() {
        return name;
    }

    /**
     * Sets the name property of an XML instance
     * to the parameter.
     *
     * @param name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the name property of an XML instance
     * to the parameter and returns the instance.
     *
     * @param name
     * @return XML instance with the set name property
     */

    public XML name(String name) {
        this.name = name;
        return this;
    }

    /**
     * returns the namespace property from a XML instance.
     *
     * @return String namespace
     **/

    public String getNamespace() {
        return namespace;
    }

    /**
     * Sets the namespace property of an XML instance
     * to the parameter.
     *
     * @param namespace
     */

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Sets the namespace property of an XML instance
     * to the parameter and returns the instance.
     *
     * @param namespace
     * @return XML instance with the set namespace property
     */

    public XML namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    /**
     * returns the prefix property from a XML instance.
     *
     * @return String prefix
     **/

    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the prefix property of an XML instance
     * to the parameter.
     *
     * @param prefix
     */

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Sets the prefix property of an XML instance
     * to the parameter and returns the instance.
     *
     * @param prefix
     * @return XML instance with the set prefix property
     */

    public XML prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * returns the attribute property from a XML instance.
     *
     * @return Boolean attribute
     **/

    public Boolean getAttribute() {
        return attribute;
    }

    /**
     * Sets the attribute property of an XML instance
     * to the parameter.
     *
     * @param attribute
     */

    public void setAttribute(Boolean attribute) {
        this.attribute = attribute;
    }

    /**
     * Sets the attribute property of an XML instance
     * to the parameter and returns the instance.
     *
     * @param attribute
     * @return XML instance with the set attribute property
     */

    public XML attribute(Boolean attribute) {
        this.attribute = attribute;
        return this;
    }

    /**
     * returns the wrapped property from a XML instance.
     *
     * @return Boolean wrapped
     **/

    public Boolean getWrapped() {
        return wrapped;
    }

    /**
     * Sets the wrapped property of an XML instance
     * to the parameter.
     *
     * @param wrapped
     */

    public void setWrapped(Boolean wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Sets the wrapped property of an XML instance
     * to the parameter and returns the instance.
     *
     * @param wrapped
     * @return XML instance with the set wrapped property
     */

    public XML wrapped(Boolean wrapped) {
        this.wrapped = wrapped;
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
        XML XML = (XML) o;
        return Objects.equals(this.name, XML.name) &&
               Objects.equals(this.namespace, XML.namespace) &&
               Objects.equals(this.prefix, XML.prefix) &&
               Objects.equals(this.attribute, XML.attribute) &&
               Objects.equals(this.wrapped, XML.wrapped) &&
               Objects.equals(this.extensions, XML.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, namespace, prefix, attribute, wrapped, extensions);
    }

    /**
     * Returns the extensions property of an XML instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     */

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds an item to the extensions property of an XML instance.
     * If extensions is null, creates a HashMap and adds item to it.
     *
     * @param name - map key
     * @param value - map value
     */

    public void addExtension(String name, Object value) {
        if (this.extensions == null) {
            this.extensions = new java.util.HashMap<>();
        }
        this.extensions.put(name, value);
    }

    /**
     * Sets the extensions property of an XML instance
     * to the parameter.
     *
     * @return Map&lt;String, Object&gt; extensions
     */

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class XML {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    namespace: ").append(toIndentedString(namespace)).append("\n");
        sb.append("    prefix: ").append(toIndentedString(prefix)).append("\n");
        sb.append("    attribute: ").append(toIndentedString(attribute)).append("\n");
        sb.append("    wrapped: ").append(toIndentedString(wrapped)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
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
