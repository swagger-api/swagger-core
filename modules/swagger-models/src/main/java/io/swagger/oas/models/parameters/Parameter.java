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

package io.swagger.oas.models.parameters;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.swagger.oas.models.examples.Example;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.media.Schema;

/**
 * Parameter
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc2/versions/3.0.md#parameterObject"
 */

public class Parameter {
    private String name = null;
    private String in = null;
    private String description = null;
    private Boolean required = null;
    private Boolean deprecated = null;
    private Boolean allowEmptyValue = null;
    private String $ref = null;

    /**
     * Gets or Sets style
     */
    public enum StyleEnum {
        MATRIX("matrix"),
        LABEL("label"),
        FORM("form"),
        SIMPLE("simple"),
        SPACEDELIMITED("spaceDelimited"),
        PIPEDELIMITED("pipeDelimited"),
        DEEPOBJECT("deepObject");

        private final String value;

        StyleEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private StyleEnum style = null;
    private Boolean explode = null;
    private Boolean allowReserved = null;
    private Schema schema = null;
    private Map<String, Example> examples = null;
    private String example = null;
    private Content content = null;
    private java.util.Map<String, Object> extensions = null;

    /**
     * returns the name property from a Parameter instance.
     *
     * @return String name
     **/

    public String getName() {
        return name;
    }

    /**
     * Sets the name property of a Parameter instance
     * to the parameter.
     *
     * @param name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the name property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param name
     * @return Parameter instance with the modified name property
     */

    public Parameter name(String name) {
        this.name = name;
        return this;
    }

    /**
     * returns the in property from a Parameter instance.
     *
     * @return String in
     **/

    public String getIn() {
        return in;
    }

    /**
     * Sets the in property of a Parameter instance
     * to the parameter.
     * If in property is set to path then also sets
     * required property to true.
     *
     * @param in
     */

    public void setIn(String in) {
        if ("path".equals(in)) {
            this.required = true;
        }
        this.in = in;
    }

    /**
     * Sets the in property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param in
     * @return Parameter instance with the modified in property
     */

    public Parameter in(String in) {
        setIn(in);
        return this;
    }

    /**
     * returns the description property from a Parameter instance.
     *
     * @return String description
     **/

    public String getDescription() {
        return description;
    }

    /**
     * Sets the description property of a Parameter instance
     * to the parameter.
     *
     * @param description
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param description
     * @return Parameter instance with the modified description property
     */

    public Parameter description(String description) {
        this.description = description;
        return this;
    }

    /**
     * returns the required property from a Parameter instance.
     *
     * @return Boolean required
     **/

    public Boolean getRequired() {
        return required;
    }

    /**
     * Sets the required property of a Parameter instance
     * to the parameter.
     *
     * @param required
     */

    public void setRequired(Boolean required) {
        this.required = required;
    }

    /**
     * Sets the required property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param required
     * @return Parameter instance with the modified required property
     */

    public Parameter required(Boolean required) {
        this.required = required;
        return this;
    }

    /**
     * returns the deprecated property from a Parameter instance.
     *
     * @return Boolean deprecated
     **/

    public Boolean getDeprecated() {
        return deprecated;
    }

    /**
     * Sets the deprecated property of a Parameter instance
     * to the parameter.
     *
     * @param deprecated
     */

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    /**
     * Sets the deprecated property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param deprecated
     * @return Parameter instance with the modified deprecated property
     */

    public Parameter deprecated(Boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    /**
     * returns the allowEmptyValue property from a Parameter instance.
     *
     * @return Boolean allowEmptyValue
     **/

    public Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    /**
     * Sets the allowEmptyValue property of a Parameter instance
     * to the parameter.
     *
     * @param allowEmptyValue
     */

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }

    /**
     * Sets the allowEmptyValue property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param allowEmptyValue
     * @return Parameter instance with the modified allowEmptyValue property
     */

    public Parameter allowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
        return this;
    }

    /**
     * returns the style property from a Parameter instance.
     *
     * @return StyleEnum style
     **/

    public StyleEnum getStyle() {
        return style;
    }

    /**
     * Sets the style property of a Parameter instance
     * to the parameter.
     *
     * @param style
     */

    public void setStyle(StyleEnum style) {
        this.style = style;
    }

    /**
     * Sets the style property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param style
     * @return Parameter instance with the modified style property
     */

    public Parameter style(StyleEnum style) {
        this.style = style;
        return this;
    }

    /**
     * returns the explode property from a Parameter instance.
     *
     * @return Boolean explode
     **/

    public Boolean getExplode() {
        return explode;
    }

    /**
     * Sets the explode property of a Parameter instance
     * to the parameter.
     *
     * @param explode
     */

    public void setExplode(Boolean explode) {
        this.explode = explode;
    }

    /**
     * Sets the explode property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param explode
     * @return Parameter instance with the modified explode property
     */

    public Parameter explode(Boolean explode) {
        this.explode = explode;
        return this;
    }

    /**
     * returns the allowReserved property from a Parameter instance.
     *
     * @return Boolean allowReserved
     **/

    public Boolean getAllowReserved() {
        return allowReserved;
    }

    /**
     * Sets the allowReserved property of a Parameter instance
     * to the parameter.
     *
     * @param allowReserved
     */

    public void setAllowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
    }

    /**
     * Sets the allowReserved property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param allowReserved
     * @return Parameter instance with the modified allowReserved property
     */

    public Parameter allowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
        return this;
    }

    /**
     * returns the schema property from a Parameter instance.
     *
     * @return Schema schema
     **/

    public Schema getSchema() {
        return schema;
    }

    /**
     * Sets the schema property of a Parameter instance
     * to the parameter.
     *
     * @param schema
     */

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    /**
     * Sets the schema property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param schema
     * @return Parameter instance with the modified schema property
     */

    public Parameter schema(Schema schema) {
        this.schema = schema;
        return this;
    }

    /**
     * returns the examples property from a Parameter instance.
     *
     * @return Map&lt;String, Example&gt; examples
     **/

    public Map<String, Example> getExamples() {
        return examples;
    }

    public void setExamples(Map<String, Example> examples) {
        this.examples = examples;
    }

    /**
     * Sets the examples property of a Parameter instance
     * to the parameter.
     *
     * @param examples
     */

    public Parameter examples(Map<String, Example> examples) {
        this.examples = examples;
        return this;
    }

    /**
     * Adds an example item to the examples property of a Parameter instance
     * at the specified key and returns the instance.
     * If examples is null, creates a new HashMap and adds item
     *
     * @param key
     * @param examplesItem
     * @return Parameter instance with the added example item
     */

    public Parameter addExamples(String key, Example examplesItem) {
        if (this.examples == null) {
            this.examples = new HashMap<String, Example>();
        }
        this.examples.put(key, examplesItem);
        return this;
    }

    /**
     * returns the example property from a Parameter instance.
     *
     * @return String example
     **/

    public String getExample() {
        return example;
    }

    /**
     * Sets the example property of a Parameter instance
     * to the parameter.
     *
     * @param example
     */

    public void setExample(String example) {
        this.example = example;
    }

    /**
     * Sets the example property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param example
     * @return Parameter instance with the modified example property
     */

    public Parameter example(String example) {
        this.example = example;
        return this;
    }

    /**
     * returns the content property from a Parameter instance.
     *
     * @return Content content
     **/

    public Content getContent() {
        return content;
    }

    /**
     * Sets the content property of a Parameter instance
     * to the parameter.
     *
     * @param content
     */

    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * Sets the content property of a Parameter instance
     * to the parameter and returns the instance.
     *
     * @param content
     * @return Parameter instance with the modified content property
     */

    public Parameter content(Content content) {
        this.content = content;
        return this;
    }

    /**
     * returns the $ref property from a Parameter instance.
     *
     * @return String $ref
     **/

    public String get$ref() {
        return $ref;
    }

    /**
     * Sets $ref property of a Parameter instance
     * to the parameter.
     *
     * @param $ref
     */

    public void set$ref(String $ref) {
        if ($ref != null && ($ref.indexOf(".") == -1 && $ref.indexOf("/") == -1)) {
            $ref = "#/components/parameters/" + $ref;
        }
        this.$ref = $ref;
    }

    /**
     * Sets $ref property of a Parameter instance
     * to the parameter and return the instance.
     *
     * @param $ref
     * @return Parameter instance with the set $ref property.
     */

    public Parameter $ref(String $ref) {
        this.$ref = $ref;
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
        Parameter parameter = (Parameter) o;
        return Objects.equals(this.name, parameter.name) &&
               Objects.equals(this.in, parameter.in) &&
               Objects.equals(this.description, parameter.description) &&
               Objects.equals(this.required, parameter.required) &&
               Objects.equals(this.deprecated, parameter.deprecated) &&
               Objects.equals(this.allowEmptyValue, parameter.allowEmptyValue) &&
               Objects.equals(this.style, parameter.style) &&
               Objects.equals(this.explode, parameter.explode) &&
               Objects.equals(this.allowReserved, parameter.allowReserved) &&
               Objects.equals(this.schema, parameter.schema) &&
               Objects.equals(this.examples, parameter.examples) &&
               Objects.equals(this.example, parameter.example) &&
               Objects.equals(this.content, parameter.content) &&
               Objects.equals(this.extensions, parameter.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, in, description, required, deprecated, allowEmptyValue, style, explode, allowReserved, schema, examples, example, content, extensions);
    }

    /**
     * Returns extensions property of a Parameter instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     */

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds an object item to extensions map at
     * the specified key.
     * <p>
     * If extensions is null, creates a new HashMap
     * and adds item to it
     *
     * @param String name - map key
     * @param Object value - map value
     */

    public void addExtension(String name, Object value) {
        if (this.extensions == null) {
            this.extensions = new java.util.HashMap<>();
        }
        this.extensions.put(name, value);
    }

    /**
     * Sets extensions property of a Parameter instance
     *
     * @param Map&lt;String, Object&gt; extensions
     */

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Parameter {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    in: ").append(toIndentedString(in)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    required: ").append(toIndentedString(required)).append("\n");
        sb.append("    deprecated: ").append(toIndentedString(deprecated)).append("\n");
        sb.append("    allowEmptyValue: ").append(toIndentedString(allowEmptyValue)).append("\n");
        sb.append("    style: ").append(toIndentedString(style)).append("\n");
        sb.append("    explode: ").append(toIndentedString(explode)).append("\n");
        sb.append("    allowReserved: ").append(toIndentedString(allowReserved)).append("\n");
        sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
        sb.append("    examples: ").append(toIndentedString(examples)).append("\n");
        sb.append("    example: ").append(toIndentedString(example)).append("\n");
        sb.append("    content: ").append(toIndentedString(content)).append("\n");
        sb.append("    $ref: ").append(toIndentedString($ref)).append("\n");
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
