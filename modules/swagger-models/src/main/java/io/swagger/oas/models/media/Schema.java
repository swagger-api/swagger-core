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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.oas.models.ExternalDocumentation;

/**
 * Schema
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc2/versions/3.0.md#schemaObject"
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
    private Schema additionalProperties = null;
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

    /**
     * returns the name property from a from a Schema instance. Ignored in serialization.
     *
     * @return String name
     **/
    @JsonIgnore
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name property of a Schema instance
     * to the parameter.
     *
     * @param name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the name property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param name
     * @return Schema instance with the modified name property
     */

    public Schema name(String name) {
        this.setName(name);
        return this;
    }

    /**
     * returns the discriminator property from a Schema instance.
     *
     * @return Discriminator discriminator
     **/

    public Discriminator getDiscriminator() {
        return discriminator;
    }

    /**
     * Sets discriminator property of a Schema instance
     * to the parameter.
     *
     * @param discriminator
     */

    public void setDiscriminator(Discriminator discriminator) {
        this.discriminator = discriminator;
    }

    /**
     * Sets discriminator property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param discriminator
     * @return Schema instance with the modified discriminator.
     */

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

    /**
     * Sets the title property of a Schema instance
     * to the parameter.
     *
     * @param title
     */

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the title property of a Schema instance
     * to the parameter and returns the modified instance.
     *
     * @param title
     * @return Schema instance with the modified title.
     */

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

    /**
     * Set _default property of a Schema instance
     * to the parameter.
     *
     * @param _default
     */

    public void setDefault(Object _default) {
        this._default = cast(_default);
    }

    /**
     * Casts a Java object to a generic type T.
     *
     * @param value
     * @return T value
     */

    protected T cast(Object value) {
        return (T) value;
    }

    /**
     * Returns _enum property for a Schema instance.
     *
     * @return List&lt;T&gt; _enum
     */

    public List<T> getEnum() {
        return _enum;
    }

    /**
     * Sets _enum property of a Schema instance
     * to the parameter.
     *
     * @param _enum
     */

    public void setEnum(List<T> _enum) {
        this._enum = _enum;
    }

    /**
     * Adds a generic type T item to _enum of a Schema instance.
     *
     * @param _enumItem
     */

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

    /**
     * Sets multipleOf property of a Schema instance
     * to the parameter.
     *
     * @param multipleOf
     */

    public void setMultipleOf(BigDecimal multipleOf) {
        this.multipleOf = multipleOf;
    }

    /**
     * Sets multipleOf property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param multipleOf
     * @return Schema instance with the modified multipleOf property
     */

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

    /**
     * Sets maximum property of a Schema instance
     * to the parameter.
     *
     * @param maximum
     */

    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    /**
     * Sets maximum property of a Schema instance to the parameter
     * and returns the instance.
     *
     * @param maximum
     * @return Schema instance with the modified maximum property
     */

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

    /**
     * Sets exclusiveMaximum property of a Schema instance
     * to the parameter.
     *
     * @param exclusiveMaximum
     */

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    /**
     * Sets exclusiveMaximum property of a Schema instance to the parameter
     * and returns the instance.
     *
     * @param exclusiveMaximum
     * @return Schema instance with modified exclusiveMaximum property.
     */

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

    /**
     * Sets minimum property of a Schema instance
     * to the parameter.
     *
     * @param minimum
     */

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    /**
     * Sets minimum property of a Schema instance
     * to the parameter and returns the instance
     *
     * @param minimum
     * @return Schema instance with the modified minimum property.
     */

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

    /**
     * Sets exclusiveMinimum property of a Schema instance
     * to the parameter.
     *
     * @param exclusiveMinimum
     */

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    /**
     * Sets exclusiveMinimum property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param exclusiveMinimum
     * @return Schema instance with the modified exclusiveMinimum property.
     */

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

    /**
     * Sets maxLength property of a Schema instance
     * to the parameter.
     *
     * @param maxLength
     */

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Sets maxLength property of a Schema instance
     * to the parameter and returns the instance
     *
     * @param maxLength
     * @return Schema instance with the modified maxLength property.
     */

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

    /**
     * Sets minLength property of a Schema instance
     * to the parameter.
     *
     * @param minLength
     */

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    /**
     * Sets minLength property of a Schema instance
     * to the parameter and returns the instance
     *
     * @param minLength
     * @return Schema instance with the modified minLength property.
     */

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

    /**
     * Sets pattern property of a Schema instance
     * to the parameter.
     *
     * @param pattern
     */

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Sets pattern property of a Schema instance
     * to the parameter and returns the instance
     *
     * @param pattern
     * @return Schema instance with the modified pattern property.
     */

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

    /**
     * Sets maxItems property of a Schema instance
     * to the parameter.
     *
     * @param maxItems
     */

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    /**
     * Sets maxItems property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param maxItems
     * @return Schema instance with the modified maxItems property.
     */

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

    /**
     * Sets minItems property of Schema instance
     * to the parameter.
     *
     * @param minItems
     */

    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    /**
     * Sets minItems property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param minItems
     * @return Schema instance with the modified minItems property.
     */

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

    /**
     * Sets uniqueItems property of a Schema instance
     * to the parameter.
     *
     * @param uniqueItems
     */

    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

    /**
     * Sets uniqueItems property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param uniqueItems
     * @return Schema instance with the modified uniqueItems property.
     */

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

    /**
     * Sets maxProperties property of a Schema instance
     * to the parameter.
     *
     * @param maxProperties
     */

    public void setMaxProperties(Integer maxProperties) {
        this.maxProperties = maxProperties;
    }

    /**
     * Sets maxProperties property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param maxProperties
     * @return Schema instance with the modified maxProperty property.
     */

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

    /**
     * Sets minProperties property of a Schema instance
     * to the parameter.
     *
     * @param minProperties
     */

    public void setMinProperties(Integer minProperties) {
        this.minProperties = minProperties;
    }

    /**
     * Sets minProperties property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param minProperties
     * @return Schema instance with the modified minProperty property.
     */

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

    /**
     * Sets required property of a Schema instance if
     * it is null or does not contain the List items
     * passed in as method arguments.
     *
     * @param required
     */

    public void setRequired(List<String> required) {
        List<String> list = new ArrayList<>();
        if (required != null) {
            for (String req : required) {
                if (this.properties == null) {
                    list.add(req);
                } else if (this.properties.containsKey(req)) {
                    list.add(req);
                }
            }
        }
        Collections.sort(list);
        if (list.size() == 0) {
            list = null;
        }
        this.required = list;
    }

    /**
     * Sets required List property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param required
     * @return Schema instance with the set required property.
     */

    public Schema required(List<String> required) {
        this.required = required;
        return this;
    }

    /**
     * Adds an item to required List of a Schema instance.
     * Creates new ArrayList if instance's required property is null.
     *
     * @param requiredItem
     * @return Schema instance with added required item.
     */

    public Schema addRequiredItem(String requiredItem) {
        if (this.required == null) {
            this.required = new ArrayList<String>();
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

    /**
     * Sets the type property of a Schema instance
     * to the parameter.
     *
     * @param type
     */

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the type property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param type
     * @return Schema instance with the modified type property.
     */

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

    /**
     * Sets the not property of a Schema instance
     * to the parameter.
     *
     * @param not
     */

    public void setNot(Schema not) {
        this.not = not;
    }

    /**
     * Sets the not property of a Schema instance
     * to the parameter and
     * returns the instance.
     *
     * @param not
     * @return Schema with the modified not property.
     */

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

    /**
     * Sets properties property of a Schema instance
     * to the parameter.
     *
     * @param properties
     */

    public void setProperties(Map<String, Schema> properties) {
        this.properties = properties;
    }

    /**
     * Sets properties property of a Schema instance
     * to the parameter and returns the modified instance.
     *
     * @param properties
     * @return Schema instance with the set properties property.
     */

    public Schema properties(Map<String, Schema> properties) {
        this.properties = properties;
        return this;
    }

    /**
     * Adds a Schema property item at specified key to properties
     * property of a Schema instance and returns the instance.
     *
     * @param key
     * @param propertiesItem
     * @return Schema instance with added property item.
     */

    public Schema addProperties(String key, Schema propertiesItem) {
        if (this.properties == null) {
            this.properties = new LinkedHashMap<String, Schema>();
        }
        this.properties.put(key, propertiesItem);
        return this;
    }

    /**
     * returns the additionalProperties property from a Schema instance.
     *
     * @return Schema additionalProperties
     **/

    public Schema getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * Sets additionalProperties property of a Schema instance
     * to the parameter.
     *
     * @param additionalProperties
     */

    public void setAdditionalProperties(Schema additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    /**
     * Sets additionalProperties property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param additionalProperties
     * @return Schema instance with the set additionalProperties property
     */

    public Schema additionalProperties(Schema additionalProperties) {
        this.additionalProperties = additionalProperties;
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

    /**
     * Sets description property of a Schema instance
     * to the parameter.
     *
     * @param description
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets description property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param description
     * @return Schema instance with the set description property
     */

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

    /**
     * Sets format property of a Schema instance
     * to the parameter.
     *
     * @param format
     */

    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Sets format property of a Schema instance
     * to the parameter and returns the instance.
     *
     * @param format
     * @return Schema instance with the set format property.
     */

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

    /**
     * Set $ref property of a Schema instance
     * to the parameter.
     *
     * @param $ref
     */

    public void set$ref(String $ref) {
        if ($ref != null && ($ref.indexOf(".") == -1 && $ref.indexOf("/") == -1)) {
            $ref = "#/components/schemas/" + $ref;
        }
        this.$ref = $ref;
    }

    /**
     * Set $ref property of a Schema instance
     * to the parameter and return the instance.
     *
     * @param $ref
     * @return Schema instance with the set $ref property.
     */

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

    /**
     * Sets nullable property of a Schema instance
     * to the parameter.
     *
     * @param nullable
     */

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * Sets nullable property of a Schema instance
     * to the parameter and return the instance.
     *
     * @param nullable
     * @return Schema instance with the set nullable property.
     */

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

    /**
     * Sets readOnly property of a Schema instance
     * to the parameter.
     *
     * @param readOnly
     */

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Sets readOnly property of a Schema instance
     * to the parameter and return the instance.
     *
     * @param readOnly
     * @return Schema instance with the set readOnly property.
     */

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

    /**
     * Sets writeOnly property of a Schema instance
     * to the parameter.
     *
     * @param writeOnly
     */

    public void setWriteOnly(Boolean writeOnly) {
        this.writeOnly = writeOnly;
    }

    /**
     * Sets writeOnly property of a Schema instance
     * to the parameter and return the instance.
     *
     * @param writeOnly
     * @return Schema instance with the set writeOnly property.
     */

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

    /**
     * Sets example property of a Schema instance
     * to the parameter.
     *
     * @param example
     */

    public void setExample(Object example) {
        this.example = cast(example);
    }

    /**
     * Sets example property of a Schema instance
     * to the parameter and return the instance.
     *
     * @param example
     * @return Schema instance with the set example property.
     */

    public Schema example(Object example) {
        this.example = cast(example);
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

    /**
     * Sets externalDocs property of a Schema instance
     * to the parameter.
     *
     * @param externalDocs
     */

    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }

    /**
     * Sets externalDocs property of a Schema instance
     * to the parameter and
     * return the instance.
     *
     * @param externalDocs
     * @return Schema instance with the set externalDocs property
     */

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

    /**
     * Sets deprecated property of a Schema instance
     * to the parameter.
     *
     * @param deprecated
     */

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    /**
     * Sets deprecated property of a Schema instance
     * to the parameter and
     * return the instance.
     *
     * @param deprecated
     * @return Schema instance with the set deprecated property
     */

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

    /**
     * Sets xml property of a Schema instance
     * to the parameter.
     *
     * @param xml
     */

    public void setXml(XML xml) {
        this.xml = xml;
    }

    /**
     * Sets xml property of a Schema instance
     * to the parameter and
     * return the instance.
     *
     * @param xml
     * @return Schema instance with the set xml property
     */

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
               Objects.equals(this._enum, schema._enum) &&
               Objects.equals(this._default, schema._default);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, multipleOf, maximum, exclusiveMaximum, minimum, exclusiveMinimum, maxLength, minLength, pattern, maxItems,
                            minItems, uniqueItems, maxProperties, minProperties, required, type, not, properties, additionalProperties, description, format, $ref,
                            nullable, readOnly, writeOnly, example, externalDocs, deprecated, xml, extensions, _enum, _default);
    }

    /**
     * Returns extensions property of a Schema instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     */

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds an object item to extensions map at
     * the specified key.
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
     * Sets extensions property of a Schema instance
     *
     * @param extensions
     */

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Schema {\n");

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
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    not: ").append(toIndentedString(not)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
        sb.append("    additionalProperties: ").append(toIndentedString(additionalProperties)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
        sb.append("    $ref: ").append(toIndentedString($ref)).append("\n");
        sb.append("    nullable: ").append(toIndentedString(nullable)).append("\n");
        sb.append("    readOnly: ").append(toIndentedString(readOnly)).append("\n");
        sb.append("    writeOnly: ").append(toIndentedString(writeOnly)).append("\n");
        sb.append("    example: ").append(toIndentedString(example)).append("\n");
        sb.append("    externalDocs: ").append(toIndentedString(externalDocs)).append("\n");
        sb.append("    deprecated: ").append(toIndentedString(deprecated)).append("\n");
        sb.append("    xml: ").append(toIndentedString(xml)).append("\n");
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