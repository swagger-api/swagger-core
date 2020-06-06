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

package io.swagger.v3.oas.models.media;

import io.swagger.v3.oas.models.examples.Example;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * MediaType
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#mediaTypeObject"
 */

public class MediaType {
    private Schema schema = null;
    private Map<String, Example> examples = null;
    private Object example = null;
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

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public MediaType schema(Schema schema) {
        this.schema = schema;
        return this;
    }

    /**
     * returns the examples property from a MediaType instance.
     *
     * @return Map&lt;String, Example&gt; examples
     **/

    public Map<String, Example> getExamples() {
        return examples;
    }

    public void setExamples(Map<String, Example> examples) {
        this.examples = examples;
    }

    public MediaType examples(Map<String, Example> examples) {
        this.examples = examples;
        return this;
    }

    public MediaType addExamples(String key, Example examplesItem) {
        if (this.examples == null) {
            this.examples = new LinkedHashMap<>();
        }
        this.examples.put(key, examplesItem);
        return this;
    }

    /**
     * returns the example property from a MediaType instance.
     *
     * @return String example
     **/

    public Object getExample() {
        return example;
    }

    public void setExample(Object example) {
        this.example = example;
    }

    public MediaType example(Object example) {
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

    public void setEncoding(Map<String, Encoding> encoding) {
        this.encoding = encoding;
    }

    public MediaType encoding(Map<String, Encoding> encoding) {
        this.encoding = encoding;
        return this;
    }

    public MediaType addEncoding(String key, Encoding encodingItem) {
        if (this.encoding == null) {
            this.encoding = new LinkedHashMap<>();
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

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
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

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public MediaType extensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
        return this;
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
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

