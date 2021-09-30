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
import io.swagger.v3.oas.annotations.OpenAPI30;
import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.SpecVersion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
    @OpenAPI30
    private Boolean exclusiveMaximum = null;
    private BigDecimal minimum = null;
    @OpenAPI30
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
    @OpenAPI30
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

    private boolean exampleSetFlag;

    private List<Schema> allOf = null;
    private List<Schema> anyOf = null;
    private List<Schema> oneOf = null;

    private Schema<?> items = null;

    protected T _const;

    private SpecVersion specVersion = SpecVersion.V30;

    @JsonIgnore
    public SpecVersion getSpecVersion() {
        return this.specVersion;
    }

    public void setSpecVersion(SpecVersion specVersion) {
        this.specVersion = specVersion;
    }

    public Schema specVersion(SpecVersion specVersion) {
        this.setSpecVersion(specVersion);
        return this;
    }


    /*
    @OpenAPI31 fields and accessors
    */


    @OpenAPI31
    private Set<String> types;

    @OpenAPI31
    private Map<String, Schema> patternProperties = null;
    @OpenAPI31
    private BigDecimal exclusiveMaximumValue = null;
    @OpenAPI31
    private BigDecimal exclusiveMinimumValue = null;


    @OpenAPI31
    private Schema contains = null;
    @OpenAPI31
    private String $id;
    @OpenAPI31
    private String $schema;
    @OpenAPI31
    private String $anchor;
    @OpenAPI31
    private String jsonSchemaDialect;

    @OpenAPI31
    public Schema getContains() {
        return contains;
    }

    @OpenAPI31
    public void setContains(Schema contains) {
        this.contains = contains;
    }

    @OpenAPI31
    public String get$id() {
        return $id;
    }

    @OpenAPI31
    public void set$id(String $id) {
        this.$id = $id;
    }

    @OpenAPI31
    public String get$schema() {
        return $schema;
    }

    @OpenAPI31
    public void set$schema(String $schema) {
        this.$schema = $schema;
    }

    @OpenAPI31
    public String get$anchor() {
        return $anchor;
    }

    @OpenAPI31
    public void set$anchor(String $anchor) {
        this.$anchor = $anchor;
    }

    /**
     * returns the exclusiveMaximumValue property from a Schema instance for OpenAPI 3.1.x
     *
     * @since 2.1.8
     * @return BigDecimal exclusiveMaximumValue
     *
     **/
    @OpenAPI31
    public BigDecimal getExclusiveMaximumValue() {
        return exclusiveMaximumValue;
    }

    @OpenAPI31
    public void setExclusiveMaximumValue(BigDecimal exclusiveMaximumValue) {
        this.exclusiveMaximumValue = exclusiveMaximumValue;
    }

    @OpenAPI31
    public Schema exclusiveMaximumValue(BigDecimal exclusiveMaximumValue) {
        this.exclusiveMaximumValue = exclusiveMaximumValue;
        return this;
    }

    /**
     * returns the exclusiveMinimumValue property from a Schema instance for OpenAPI 3.1.x
     *
     * @since 2.1.8
     * @return BigDecimal exclusiveMinimumValue
     *
     **/
    @OpenAPI31
    public BigDecimal getExclusiveMinimumValue() {
        return exclusiveMinimumValue;
    }
    @OpenAPI31
    public void setExclusiveMinimumValue(BigDecimal exclusiveMinimumValue) {
        this.exclusiveMinimumValue = exclusiveMinimumValue;
    }
    @OpenAPI31
    public Schema exclusiveMinimumValue(BigDecimal exclusiveMinimumValue) {
        this.exclusiveMinimumValue = exclusiveMinimumValue;
        return this;
    }

    /**
     * returns the patternProperties property from a Schema instance.
     *
     * @since 2.1.8
     * @return Map&lt;String, Schema&gt; patternProperties
     **/

    @OpenAPI31
    public Map<String, Schema> getPatternProperties() {
        return patternProperties;
    }

    public void setPatternProperties(Map<String, Schema> patternProperties) {
        this.patternProperties = patternProperties;
    }

    @OpenAPI31
    public Schema patternProperties(Map<String, Schema> patternProperties) {
        this.patternProperties = patternProperties;
        return this;
    }

    @OpenAPI31
    public Schema addPatternProperty(String key, Schema patternPropertiesItem) {
        if (this.patternProperties == null) {
            this.patternProperties = new LinkedHashMap<>();
        }
        this.patternProperties.put(key, patternPropertiesItem);
        return this;
    }


    @OpenAPI31
    public Schema contains(Schema contains) {
        this.contains = contains;
        return this;
    }

    @OpenAPI31
    public Schema $id(String $id) {
        this.$id = $id;
        return this;
    }

    @OpenAPI31
    public Set<String> getTypes() {
        return types;
    }

    @OpenAPI31
    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @OpenAPI31
    public boolean addType(String type) {
        if (types == null) {
            types = new LinkedHashSet<>();
        }
        return types.add(type);
    }

    @OpenAPI31
    public Schema $schema(String $schema) {
        this.$schema = $schema;
        return this;
    }

    @OpenAPI31
    public Schema $anchor(String $anchor) {
        this.$anchor = $anchor;
        return this;
    }

    @OpenAPI31
    public Schema types(Set<String> types) {
        this.types = types;
        return this;
    }

    /*
    INTERNAL MEMBERS @OpenAPI31
     */

    @OpenAPI31
    protected Map<String, Object> jsonSchema = null;

    @OpenAPI31
    public Map<String, Object> getJsonSchema() {
        return jsonSchema;
    }

    @OpenAPI31
    public void setJsonSchema(Map<String, Object> jsonSchema) {
        this.jsonSchema = jsonSchema;
    }

    @OpenAPI31
    public Schema jsonSchema(Map<String, Object> jsonSchema) {
        this.jsonSchema = jsonSchema;
        return this;
    }

    @OpenAPI31
    protected transient Object jsonSchemaImpl = null;

    @OpenAPI31
    public Object getJsonSchemaImpl() {
        return jsonSchemaImpl;
    }

    @OpenAPI31
    public void setJsonSchemaImpl(Object jsonSchemaImpl) {
        this.jsonSchemaImpl = jsonSchemaImpl;
    }

    @OpenAPI31
    public Schema jsonSchemaImpl(Object jsonSchemaImpl) {
        setJsonSchemaImpl(jsonSchemaImpl);
        return this;
    }

    @OpenAPI31
    public String getJsonSchemaDialect() {
        return jsonSchemaDialect;
    }

    @OpenAPI31
    public void setJsonSchemaDialect(String jsonSchemaDialect) {
        this.jsonSchemaDialect = jsonSchemaDialect;
    }

    @OpenAPI31
    public Schema jsonSchemaDialect(String jsonSchemaDialect) {
        this.jsonSchemaDialect = jsonSchemaDialect;
        return this;
    }

        /*
    CONSTRUCTORS
     */


    public Schema() {
    }

    protected Schema(String type, String format) {
        this.type = type;
        this.format = format;
    }

    public Schema(SpecVersion specVersion) {
        this.specVersion = specVersion;
    }

    protected Schema(String type, String format, SpecVersion specVersion) {
        this.type = type;
        this.format = format;
        this.specVersion = specVersion;
    }

    /*
    ACCESSORS
     */

    /**
     * returns the allOf property from a ComposedSchema instance.
     *
     * @return List&lt;Schema&gt; allOf
     **/

    public List<Schema> getAllOf() {
        return allOf;
    }

    public void setAllOf(List<Schema> allOf) {
        this.allOf = allOf;
    }

    public Schema allOf(List<Schema> allOf) {
        this.allOf = allOf;
        return this;
    }

    public Schema addAllOfItem(Schema allOfItem) {
        if (this.allOf == null) {
            this.allOf = new ArrayList<>();
        }
        this.allOf.add(allOfItem);
        return this;
    }

    /**
     * returns the anyOf property from a ComposedSchema instance.
     *
     * @return List&lt;Schema&gt; anyOf
     **/

    public List<Schema> getAnyOf() {
        return anyOf;
    }

    public void setAnyOf(List<Schema> anyOf) {
        this.anyOf = anyOf;
    }

    public Schema anyOf(List<Schema> anyOf) {
        this.anyOf = anyOf;
        return this;
    }

    public Schema addAnyOfItem(Schema anyOfItem) {
        if (this.anyOf == null) {
            this.anyOf = new ArrayList<>();
        }
        this.anyOf.add(anyOfItem);
        return this;
    }

    /**
     * returns the oneOf property from a ComposedSchema instance.
     *
     * @return List&lt;Schema&gt; oneOf
     **/

    public List<Schema> getOneOf() {
        return oneOf;
    }

    public void setOneOf(List<Schema> oneOf) {
        this.oneOf = oneOf;
    }

    public Schema oneOf(List<Schema> oneOf) {
        this.oneOf = oneOf;
        return this;
    }

    public Schema addOneOfItem(Schema oneOfItem) {
        if (this.oneOf == null) {
            this.oneOf = new ArrayList<>();
        }
        this.oneOf.add(oneOfItem);
        return this;
    }


    /**
     * returns the items property from a ArraySchema instance.
     *
     * @return Schema items
     **/

    public Schema<?> getItems() {
        return items;
    }

    public void setItems(Schema<?> items) {
        this.items = items;
    }

    public Schema items(Schema<?> items) {
        this.items = items;
        return this;
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
     * returns the exclusiveMaximum property from a Schema instance for OpenAPI 3.0.x
     *
     * @return Boolean exclusiveMaximum
     **/
    @OpenAPI30
    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    @OpenAPI30
    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    @OpenAPI30
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
     * returns the exclusiveMinimum property from a Schema instance for OpenAPI 3.0.x
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
            $ref = Components.COMPONENTS_SCHEMAS_REF + $ref;
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
    @OpenAPI30
    public Boolean getNullable() {
        return nullable;
    }

    @OpenAPI30
    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    @OpenAPI30
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
        if (!(example != null && this.example == null)) {
            exampleSetFlag = true;
        }
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

    /**
     * returns true if example setter has been invoked
     * Used to flag explicit setting to null of example (vs missing field) while deserializing from json/yaml string
     *
     * @return boolean exampleSetFlag
     **/

    public boolean getExampleSetFlag() {
        return exampleSetFlag;
    }

    public void setExampleSetFlag(boolean exampleSetFlag) {
        this.exampleSetFlag = exampleSetFlag;
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
                Objects.equals(this.exclusiveMaximumValue, schema.exclusiveMaximumValue) &&
                Objects.equals(this.minimum, schema.minimum) &&
                Objects.equals(this.exclusiveMinimum, schema.exclusiveMinimum) &&
                Objects.equals(this.exclusiveMinimumValue, schema.exclusiveMinimumValue) &&
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
                Objects.equals(this.contains, schema.contains) &&
                Objects.equals(this.patternProperties, schema.patternProperties) &&
                Objects.equals(this.$id, schema.$id) &&
                Objects.equals(this.$anchor, schema.$anchor) &&
                Objects.equals(this.$schema, schema.$schema) &&
                Objects.equals(this.types, schema.types) &&
                Objects.equals(this.allOf, schema.allOf) &&
                Objects.equals(this.anyOf, schema.anyOf) &&
                Objects.equals(this.oneOf, schema.oneOf) &&
                Objects.equals(this._const, schema._const) &&
                Objects.equals(this._default, schema._default) &&
                Objects.equals(this.jsonSchemaDialect, schema.jsonSchemaDialect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, multipleOf, maximum, exclusiveMaximum, exclusiveMaximumValue, minimum,
                exclusiveMinimum, exclusiveMinimumValue, maxLength, minLength, pattern, maxItems, minItems, uniqueItems,
                maxProperties, minProperties, required, type, not, properties, additionalProperties, description,
                format, $ref, nullable, readOnly, writeOnly, example, externalDocs, deprecated, xml, extensions,
                discriminator, _enum, _default, patternProperties, $id, $anchor, $schema, types, allOf, anyOf, oneOf, _const,
                jsonSchemaDialect);
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

    @OpenAPI31
    public void addExtension31(String name, Object value) {
        if (name != null && (name.startsWith("x-oas-") || name.startsWith("x-oai-"))) {
            return;
        }
        addExtension(name, value);
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
        Object typeStr = specVersion == SpecVersion.V30 ? type : types;
        sb.append("    type: ").append(toIndentedString(typeStr)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
        sb.append("    $ref: ").append(toIndentedString($ref)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    multipleOf: ").append(toIndentedString(multipleOf)).append("\n");
        sb.append("    maximum: ").append(toIndentedString(maximum)).append("\n");
        Object exclusiveMaximumStr = specVersion == SpecVersion.V30 ? exclusiveMaximum : exclusiveMaximumValue;
        sb.append("    exclusiveMaximum: ").append(toIndentedString(exclusiveMaximumStr)).append("\n");
        sb.append("    minimum: ").append(toIndentedString(minimum)).append("\n");
        Object exclusiveMinimumStr = specVersion == SpecVersion.V30 ? exclusiveMinimum : exclusiveMinimumValue;
        sb.append("    exclusiveMinimum: ").append(toIndentedString(exclusiveMinimumStr)).append("\n");
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
        if (specVersion == SpecVersion.V31) {
            sb.append("    patternProperties: ").append(toIndentedString(patternProperties)).append("\n");
            sb.append("    contains: ").append(toIndentedString(contains)).append("\n");
            sb.append("    $id: ").append(toIndentedString($id)).append("\n");
            sb.append("    $anchor: ").append(toIndentedString($anchor)).append("\n");
            sb.append("    $schema: ").append(toIndentedString($schema)).append("\n");
            sb.append("    const: ").append(toIndentedString(_const)).append("\n");
            sb.append("    jsonSchemaDialect: ").append(toIndentedString(jsonSchemaDialect)).append("\n");
        }
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

    public Schema _default(T _default) {
        this._default = _default;
        return this;
    }

    public Schema _enum(List<T> _enum) {
        this._enum = _enum;
        return this;
    }

    public Schema exampleSetFlag(boolean exampleSetFlag) {
        this.exampleSetFlag = exampleSetFlag;
        return this;
    }

    public T getConst() {
        return _const;
    }

    public void setConst(Object _const) {
        this._const = cast(_const);
    }

    public Schema _const(Object _const) {
        this._const = cast(_const);
        return this;
    }


}

