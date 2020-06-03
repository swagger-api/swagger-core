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

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.models.ExternalDocumentation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Schema
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#schemaObject"
 */

public class Schema<T> {
    protected T _default;

    private String name;
    private String title = null;
    private BigDecimal multipleOf = null;
    private BigDecimal maximum = null;
    private Boolean exclusiveMaximum = null;
    private BigDecimal minimum = null;
    private Boolean exclusiveMinimum = null;
    private Integer maxLength = null;
    private Integer minLength = null;
    private String pattern = null;
    private Integer maxItems = null;
    private Integer minItems = null;
    private Boolean uniqueItems = null;
    private Integer maxProperties = null;
    private Integer minProperties = null;
    private List<String> required = null;
    private String type = null;
    private Schema not = null;
    private Map<String, Schema> properties = null;
    private Object additionalProperties = null;
    private String description = null;
    private String format = null;
    private String $ref = null;
    private Boolean nullable = null;
    private Boolean readOnly = null;
    private Boolean writeOnly = null;
    protected T example = null;
    private ExternalDocumentation externalDocs = null;
    private Boolean deprecated = null;
    private XML xml = null;
    private java.util.Map<String, Object> extensions = null;
    protected List<T> _enum = null;
    private Discriminator discriminator = null;

    public Schema() {
    }

    protected Schema(String type, String format) {
        this.type = type;
        this.format = format;
    }

    /**
     * returns the name property from a from a Schema instance. Ignored in serialization.
     *
     * @return String name
     **/
    @JsonIgnore
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Schema name(String name) {
        this.setName(name);
        return this;
    }

    /**
     * returns the discriminator property from a AllOfSchema instance.
     *
     * @return Discriminator discriminator
     **/

    public Discriminator getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(Discriminator discriminator) {
        this.discriminator = discriminator;
    }

    public Schema discriminator(Discriminator discriminator) {
        this.discriminator = discriminator;
        return this;
    }

    /**
     * returns the title property from a Schema instance.
     *
     * @return String title
     **/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Schema title(String title) {
        this.title = title;
        return this;
    }

    /**
     * returns the _default property from a StringSchema instance.
     *
     * @return String _default
     **/

    public T getDefault() {
        return _default;
    }

    public void setDefault(Object _default) {
        this._default = cast(_default);
    }

    @SuppressWarnings("unchecked")
    protected T cast(Object value) {
        return (T) value;
    }

    public List<T> getEnum() {
        return _enum;
    }

    public void setEnum(List<T> _enum) {
        this._enum = _enum;
    }

    public void addEnumItemObject(T _enumItem) {
        if (this._enum == null) {
            this._enum = new ArrayList<>();
        }
        this._enum.add(cast(_enumItem));
    }

    /**
     * returns the multipleOf property from a Schema instance.
     * <p>
     * minimum: 0
     *
     * @return BigDecimal multipleOf
     **/

    public BigDecimal getMultipleOf() {
        return multipleOf;
    }

    public void setMultipleOf(BigDecimal multipleOf) {
        this.multipleOf = multipleOf;
    }

    public Schema multipleOf(BigDecimal multipleOf) {
        this.multipleOf = multipleOf;
        return this;
    }

    /**
     * returns the maximum property from a Schema instance.
     *
     * @return BigDecimal maximum
     **/

    public BigDecimal getMaximum() {
        return maximum;
    }

    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    public Schema maximum(BigDecimal maximum) {
        this.maximum = maximum;
        return this;
    }

    /**
     * returns the exclusiveMaximum property from a Schema instance.
     *
     * @return Boolean exclusiveMaximum
     **/

    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public Schema exclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
        return this;
    }

    /**
     * returns the minimum property from a Schema instance.
     *
     * @return BigDecimal minimum
     **/

    public BigDecimal getMinimum() {
        return minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public Schema minimum(BigDecimal minimum) {
        this.minimum = minimum;
        return this;
    }

    /**
     * returns the exclusiveMinimum property from a Schema instance.
     *
     * @return Boolean exclusiveMinimum
     **/

    public Boolean getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Schema exclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
        return this;
    }

    /**
     * returns the maxLength property from a Schema instance.
     * <p>
     * minimum: 0
     *
     * @return Integer maxLength
     **/

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Schema maxLength(Integer maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    /**
     * returns the minLength property from a Schema instance.
     * <p>
     * minimum: 0
     *
     * @return Integer minLength
     **/

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Schema minLength(Integer minLength) {
        this.minLength = minLength;
        return this;
    }

    /**
     * returns the pattern property from a Schema instance.
     *
     * @return String pattern
     **/

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Schema pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     * returns the maxItems property from a Schema instance.
     * <p>
     * minimum: 0
     *
     * @return Integer maxItems
     **/

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Schema maxItems(Integer maxItems) {
        this.maxItems = maxItems;
        return this;
    }

    /**
     * returns the minItems property from a Schema instance.
     * <p>
     * minimum: 0
     *
     * @return Integer minItems
     **/

    public Integer getMinItems() {
        return minItems;
    }

    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    public Schema minItems(Integer minItems) {
        this.minItems = minItems;
        return this;
    }

    /**
     * returns the uniqueItems property from a Schema instance.
     *
     * @return Boolean uniqueItems
     **/

    public Boolean getUniqueItems() {
        return uniqueItems;
    }

    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

    public Schema uniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
        return this;
    }

    /**
     * returns the maxProperties property from a Schema instance.
     * <p>
     * minimum: 0
     *
     * @return Integer maxProperties
     **/

    public Integer getMaxProperties() {
        return maxProperties;
    }

    public void setMaxProperties(Integer maxProperties) {
        this.maxProperties = maxProperties;
    }

    public Schema maxProperties(Integer maxProperties) {
        this.maxProperties = maxProperties;
        return this;
    }

    /**
     * returns the minProperties property from a Schema instance.
     * <p>
     * minimum: 0
     *
     * @return Integer minProperties
     **/

    public Integer getMinProperties() {
        return minProperties;
    }

    public void setMinProperties(Integer minProperties) {
        this.minProperties = minProperties;
    }

    public Schema minProperties(Integer minProperties) {
        this.minProperties = minProperties;
        return this;
    }

    /**
     * returns the required property from a Schema instance.
     *
     * @return List&lt;String&gt; required
     **/

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        List<String> list = new ArrayList<>();
        if (required != null) {
            for (String req : required) {
                if (this.properties == null || this.properties.containsKey(req)) {
                    list.add(req);
                }
            }
        }
        Collections.sort(list);
        if (list.isEmpty()) {
            list = null;
        }
        this.required = list;
    }

    public Schema required(List<String> required) {
        this.required = required;
        return this;
    }

    public Schema addRequiredItem(String requiredItem) {
        if (this.required == null) {
            this.required = new ArrayList<>();
        }
        this.required.add(requiredItem);
        Collections.sort(required);
        return this;
    }

    /**
     * returns the type property from a Schema instance.
     *
     * @return String type
     **/

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Schema type(String type) {
        this.type = type;
        return this;
    }

    /**
     * returns the not property from a Schema instance.
     *
     * @return Schema not
     **/

    public Schema getNot() {
        return not;
    }

    public void setNot(Schema not) {
        this.not = not;
    }

    public Schema not(Schema not) {
        this.not = not;
        return this;
    }

    /**
     * returns the properties property from a Schema instance.
     *
     * @return Map&lt;String, Schema&gt; properties
     **/

    public Map<String, Schema> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Schema> properties) {
        this.properties = properties;
    }

    public Schema properties(Map<String, Schema> properties) {
        this.properties = properties;
        return this;
    }

    public Schema addProperties(String key, Schema propertiesItem) {
        if (this.properties == null) {
            this.properties = new LinkedHashMap<>();
        }
        this.properties.put(key, propertiesItem);
        return this;
    }

    /**
     * returns the additionalProperties property from a Schema instance. Can be either a Boolean or a Schema
     *
     * @return Object additionalProperties
     **/

    public Object getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Object additionalProperties) {
        if (additionalProperties != null && !(additionalProperties instanceof Boolean) && !(additionalProperties instanceof Schema)) {
            throw new IllegalArgumentException("additionalProperties must be either a Boolean or a Schema instance");
        }
        this.additionalProperties = additionalProperties;
    }

    public Schema additionalProperties(Object additionalProperties) {
        setAdditionalProperties(additionalProperties);
        return this;
    }

    /**
     * returns the description property from a Schema instance.
     *
     * @return String description
     **/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Schema description(String description) {
        this.description = description;
        return this;
    }

    /**
     * returns the format property from a Schema instance.
     *
     * @return String format
     **/

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Schema format(String format) {
        this.format = format;
        return this;
    }

    /**
     * returns the $ref property from a Schema instance.
     *
     * @return String $ref
     **/
    public String get$ref() {
        return $ref;
    }

    public void set$ref(String $ref) {
        if ($ref != null && ($ref.indexOf('.') == -1 && $ref.indexOf('/') == -1)) {
            $ref = "#/components/schemas/" + $ref;
        }
        this.$ref = $ref;
    }

    public Schema $ref(String $ref) {

        set$ref($ref);
        return this;
    }

    /**
     * returns the nullable property from a Schema instance.
     *
     * @return Boolean nullable
     **/

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public Schema nullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    /**
     * returns the readOnly property from a Schema instance.
     *
     * @return Boolean readOnly
     **/

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Schema readOnly(Boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    /**
     * returns the writeOnly property from a Schema instance.
     *
     * @return Boolean writeOnly
     **/

    public Boolean getWriteOnly() {
        return writeOnly;
    }

    public void setWriteOnly(Boolean writeOnly) {
        this.writeOnly = writeOnly;
    }

    public Schema writeOnly(Boolean writeOnly) {
        this.writeOnly = writeOnly;
        return this;
    }

    /**
     * returns the example property from a Schema instance.
     *
     * @return String example
     **/

    public Object getExample() {
        return example;
    }

    public void setExample(Object example) {
        this.example = cast(example);
    }

    public Schema example(Object example) {
        setExample(example);
        return this;
    }

    /**
     * returns the externalDocs property from a Schema instance.
     *
     * @return ExternalDocumentation externalDocs
     **/

    public ExternalDocumentation getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }

    public Schema externalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
        return this;
    }

    /**
     * returns the deprecated property from a Schema instance.
     *
     * @return Boolean deprecated
     **/

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public Schema deprecated(Boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    /**
     * returns the xml property from a Schema instance.
     *
     * @return XML xml
     **/

    public XML getXml() {
        return xml;
    }

    public void setXml(XML xml) {
        this.xml = xml;
    }

    public Schema xml(XML xml) {
        this.xml = xml;
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
        Schema schema = (Schema) o;
        return Objects.equals(this.title, schema.title) &&
                Objects.equals(this.multipleOf, schema.multipleOf) &&
                Objects.equals(this.maximum, schema.maximum) &&
                Objects.equals(this.exclusiveMaximum, schema.exclusiveMaximum) &&
                Objects.equals(this.minimum, schema.minimum) &&
                Objects.equals(this.exclusiveMinimum, schema.exclusiveMinimum) &&
                Objects.equals(this.maxLength, schema.maxLength) &&
                Objects.equals(this.minLength, schema.minLength) &&
                Objects.equals(this.pattern, schema.pattern) &&
                Objects.equals(this.maxItems, schema.maxItems) &&
                Objects.equals(this.minItems, schema.minItems) &&
                Objects.equals(this.uniqueItems, schema.uniqueItems) &&
                Objects.equals(this.maxProperties, schema.maxProperties) &&
                Objects.equals(this.minProperties, schema.minProperties) &&
                Objects.equals(this.required, schema.required) &&
                Objects.equals(this.type, schema.type) &&
                Objects.equals(this.not, schema.not) &&
                Objects.equals(this.properties, schema.properties) &&
                Objects.equals(this.additionalProperties, schema.additionalProperties) &&
                Objects.equals(this.description, schema.description) &&
                Objects.equals(this.format, schema.format) &&
                Objects.equals(this.$ref, schema.$ref) &&
                Objects.equals(this.nullable, schema.nullable) &&
                Objects.equals(this.readOnly, schema.readOnly) &&
                Objects.equals(this.writeOnly, schema.writeOnly) &&
                Objects.equals(this.example, schema.example) &&
                Objects.equals(this.externalDocs, schema.externalDocs) &&
                Objects.equals(this.deprecated, schema.deprecated) &&
                Objects.equals(this.xml, schema.xml) &&
                Objects.equals(this.extensions, schema.extensions) &&
                Objects.equals(this.discriminator, schema.discriminator) &&
                Objects.equals(this._enum, schema._enum) &&
                Objects.equals(this._default, schema._default);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, multipleOf, maximum, exclusiveMaximum, minimum, exclusiveMinimum, maxLength, minLength, pattern, maxItems,
                minItems, uniqueItems, maxProperties, minProperties, required, type, not, properties, additionalProperties, description, format, $ref,
                nullable, readOnly, writeOnly, example, externalDocs, deprecated, xml, extensions, discriminator, _enum, _default);
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

    public Schema extensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Schema {\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
        sb.append("    $ref: ").append(toIndentedString($ref)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    multipleOf: ").append(toIndentedString(multipleOf)).append("\n");
        sb.append("    maximum: ").append(toIndentedString(maximum)).append("\n");
        sb.append("    exclusiveMaximum: ").append(toIndentedString(exclusiveMaximum)).append("\n");
        sb.append("    minimum: ").append(toIndentedString(minimum)).append("\n");
        sb.append("    exclusiveMinimum: ").append(toIndentedString(exclusiveMinimum)).append("\n");
        sb.append("    maxLength: ").append(toIndentedString(maxLength)).append("\n");
        sb.append("    minLength: ").append(toIndentedString(minLength)).append("\n");
        sb.append("    pattern: ").append(toIndentedString(pattern)).append("\n");
        sb.append("    maxItems: ").append(toIndentedString(maxItems)).append("\n");
        sb.append("    minItems: ").append(toIndentedString(minItems)).append("\n");
        sb.append("    uniqueItems: ").append(toIndentedString(uniqueItems)).append("\n");
        sb.append("    maxProperties: ").append(toIndentedString(maxProperties)).append("\n");
        sb.append("    minProperties: ").append(toIndentedString(minProperties)).append("\n");
        sb.append("    required: ").append(toIndentedString(required)).append("\n");
        sb.append("    not: ").append(toIndentedString(not)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
        sb.append("    additionalProperties: ").append(toIndentedString(additionalProperties)).append("\n");
        sb.append("    nullable: ").append(toIndentedString(nullable)).append("\n");
        sb.append("    readOnly: ").append(toIndentedString(readOnly)).append("\n");
        sb.append("    writeOnly: ").append(toIndentedString(writeOnly)).append("\n");
        sb.append("    example: ").append(toIndentedString(example)).append("\n");
        sb.append("    externalDocs: ").append(toIndentedString(externalDocs)).append("\n");
        sb.append("    deprecated: ").append(toIndentedString(deprecated)).append("\n");
        sb.append("    discriminator: ").append(toIndentedString(discriminator)).append("\n");
        sb.append("    xml: ").append(toIndentedString(xml)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    protected String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

