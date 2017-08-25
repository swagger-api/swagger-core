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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.swagger.oas.models.examples.Example;

/**
 * MediaType
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc2/versions/3.0.md#mediaTypeObject"
 */

public class MediaType {
    private Schema schema = null;
    private Map<String, Example> examples = null;
    private String example = null;
    private Map<String, Encoding> encoding = null;
    private java.util.Map<String, Object> extensions = null;

    /**
     * returns the schema property from a MediaType instance.
     *
     * @return Schema schema
     **/

    public Schema getSchema() {
        return schema;
    }

    /**
     * Sets schema field of a MediaType instance to the
     * parameter.
     *
     * @param schema
     */

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    /**
     * Sets schema property of a MediaType instance to the
     * parameter and returns the instance.
     *
     * @param schema
     * @return MediaType instance with the set schema property
     */

    public MediaType schema(Schema schema) {
        this.schema = schema;
        return this;
    }

    /**
     * returns the examples map of String to Example from a MediaType instance.
     *
     * @return Map&lt;String, Example&gt; examples
     **/

    public Map<String, Example> getExamples() {
        return examples;
    }

    /**
     * Sets examples field of a MediaType instance to the
     * parameter.
     *
     * @param examples
     */

    public void setExamples(Map<String, Example> examples) {
        this.examples = examples;
    }

    /**
     * Sets examples field of a MediaType instance to the
     * parameter and returns the instance.
     *
     * @param examples
     * @return MediaType instance with the set examples property
     */

    public MediaType examples(Map<String, Example> examples) {
        this.examples = examples;
        return this;
    }

    /**
     * Adds an example item to examples map of a MediaType instance
     * and returns the instance.
     * <p>
     * If the examples property is null, creates a new HashMap
     * and adds the item to it.
     *
     * @param key
     * @param examplesItem
     * @return MediaType instance with the set example item
     */

    public MediaType addExamples(String key, Example examplesItem) {
        if (this.examples == null) {
            this.examples = new HashMap<String, Example>();
        }
        this.examples.put(key, examplesItem);
        return this;
    }

    /**
     * returns the example property from a MediaType instance.
     *
     * @return String example
     **/

    public String getExample() {
        return example;
    }

    /**
     * Sets example property of a MediaType instance to the
     * parameter.
     *
     * @param example
     */

    public void setExample(String example) {
        this.example = example;
    }

    /**
     * Sets example property of a MediaType instance to the
     * parameter and returns the instance.
     *
     * @param example
     * @return MediaType instance with the set example property
     */

    public MediaType example(String example) {
        this.example = example;
        return this;
    }

    /**
     * returns the encoding property from a MediaType instance.
     *
     * @return Encoding encoding
     **/

    public Map<String, Encoding> getEncoding() {
        return encoding;
    }

    /**
     * Sets encoding property of a MediaType instance to the
     * parameter.
     *
     * @param encoding
     */

    public void setEncoding(Map<String, Encoding> encoding) {
        this.encoding = encoding;
    }

    /**
     * Sets encoding property of a MediaType instance to the
     * parameter and returns the instance.
     *
     * @param encoding
     * @return MediaType instance with the set encoding property
     */

    public MediaType encoding(Map<String, Encoding> encoding) {
        this.encoding = encoding;
        return this;
    }

    /**
     * Adds an Encoding item to encoding map of a MediaType instance
     * and returns the instance.
     * <p>
     * If the encoding property is null, creates a new HashMap
     * and adds the item to it.
     *
     * @param String key
     * @param Encoding encodingItem
     * @return MediaType instance with the added encoding item
     */

    public MediaType addEncoding(String key, Encoding encodingItem) {
        if (this.encoding == null) {
            this.encoding = new HashMap<String, Encoding>();
        }
        this.encoding.put(key, encodingItem);
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
        MediaType mediaType = (MediaType) o;
        return Objects.equals(this.schema, mediaType.schema) &&
               Objects.equals(this.examples, mediaType.examples) &&
               Objects.equals(this.example, mediaType.example) &&
               Objects.equals(this.encoding, mediaType.encoding) &&
               Objects.equals(this.extensions, mediaType.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, examples, example, encoding, extensions);
    }

    /**
     * Returns extensions property of a MediaType instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     */

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds an object item to extensions map for
     * the specified name key.
     * <p>
     * If the extensions property is null, creates a new HashMap
     * and adds the item to it.
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
     * Sets extensions property of a MediaType instance
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
        sb.append("class MediaType {\n");

        sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
        sb.append("    examples: ").append(toIndentedString(examples)).append("\n");
        sb.append("    example: ").append(toIndentedString(example)).append("\n");
        sb.append("    encoding: ").append(toIndentedString(encoding)).append("\n");
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
