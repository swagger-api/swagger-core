package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Schema
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class Schema {
  @JsonProperty("title")
  private String title = null;

  @JsonProperty("multipleOf")
  private BigDecimal multipleOf = null;

  @JsonProperty("maximum")
  private BigDecimal maximum = null;

  @JsonProperty("exclusiveMaximum")
  private Boolean exclusiveMaximum = null;

  @JsonProperty("minimum")
  private BigDecimal minimum = null;

  @JsonProperty("exclusiveMinimum")
  private Boolean exclusiveMinimum = null;

  @JsonProperty("maxLength")
  private Integer maxLength = null;

  @JsonProperty("minLength")
  private Integer minLength = null;

  @JsonProperty("pattern")
  private String pattern = null;

  @JsonProperty("maxItems")
  private Integer maxItems = null;

  @JsonProperty("minItems")
  private Integer minItems = null;

  @JsonProperty("uniqueItems")
  private Boolean uniqueItems = null;

  @JsonProperty("maxProperties")
  private Integer maxProperties = null;

  @JsonProperty("minProperties")
  private Integer minProperties = null;

  @JsonProperty("required")
  private List<String> required = null;

  @JsonProperty("enum")
  private List<Object> _enum = null;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    ARRAY("array"),
    
    BOOLEAN("boolean"),
    
    INTEGER("integer"),
    
    NUMBER("number"),
    
    OBJECT("object"),
    
    STRING("string");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("_allOf")
  private List<Schema> allOf = null;

  @JsonProperty("oneOf")
  private List<Schema> oneOf = null;

  @JsonProperty("anyOf")
  private List<Schema> anyOf = null;

  @JsonProperty("not")
  private Schema not = null;

  @JsonProperty("items")
  private Schema items = null;

  @JsonProperty("properties")
  private Map<String, Schema> properties = null;

  @JsonProperty("additionalProperties")
  private Schema additionalProperties = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("format")
  private String format = null;

  @JsonProperty("_$ref")
  private String ref = null;

  @JsonProperty("nulable")
  private Boolean nulable = null;

  @JsonProperty("discriminator")
  private String discriminator = null;

  @JsonProperty("readOnly")
  private Boolean readOnly = null;

  @JsonProperty("writeOnly")
  private Boolean writeOnly = null;

  @JsonProperty("examples")
  private List<Example> examples = null;

  @JsonProperty("example")
  private Example example = null;

  @JsonProperty("externalDocs")
  private ExternalDocumentation externalDocs = null;

  @JsonProperty("deprecated")
  private Boolean deprecated = null;

  @JsonProperty("xml")
  private XML xml = null;

  public Schema title(String title) {
    this.title = title;
    return this;
  }

   /**
   * Get title
   * @return title
  **/
  @ApiModelProperty(value = "")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Schema multipleOf(BigDecimal multipleOf) {
    this.multipleOf = multipleOf;
    return this;
  }

   /**
   * Get multipleOf
   * minimum: 0
   * @return multipleOf
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getMultipleOf() {
    return multipleOf;
  }

  public void setMultipleOf(BigDecimal multipleOf) {
    this.multipleOf = multipleOf;
  }

  public Schema maximum(BigDecimal maximum) {
    this.maximum = maximum;
    return this;
  }

   /**
   * Get maximum
   * @return maximum
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getMaximum() {
    return maximum;
  }

  public void setMaximum(BigDecimal maximum) {
    this.maximum = maximum;
  }

  public Schema exclusiveMaximum(Boolean exclusiveMaximum) {
    this.exclusiveMaximum = exclusiveMaximum;
    return this;
  }

   /**
   * Get exclusiveMaximum
   * @return exclusiveMaximum
  **/
  @ApiModelProperty(value = "")
  public Boolean getExclusiveMaximum() {
    return exclusiveMaximum;
  }

  public void setExclusiveMaximum(Boolean exclusiveMaximum) {
    this.exclusiveMaximum = exclusiveMaximum;
  }

  public Schema minimum(BigDecimal minimum) {
    this.minimum = minimum;
    return this;
  }

   /**
   * Get minimum
   * @return minimum
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getMinimum() {
    return minimum;
  }

  public void setMinimum(BigDecimal minimum) {
    this.minimum = minimum;
  }

  public Schema exclusiveMinimum(Boolean exclusiveMinimum) {
    this.exclusiveMinimum = exclusiveMinimum;
    return this;
  }

   /**
   * Get exclusiveMinimum
   * @return exclusiveMinimum
  **/
  @ApiModelProperty(value = "")
  public Boolean getExclusiveMinimum() {
    return exclusiveMinimum;
  }

  public void setExclusiveMinimum(Boolean exclusiveMinimum) {
    this.exclusiveMinimum = exclusiveMinimum;
  }

  public Schema maxLength(Integer maxLength) {
    this.maxLength = maxLength;
    return this;
  }

   /**
   * Get maxLength
   * minimum: 0
   * @return maxLength
  **/
  @ApiModelProperty(value = "")
  public Integer getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  public Schema minLength(Integer minLength) {
    this.minLength = minLength;
    return this;
  }

   /**
   * Get minLength
   * minimum: 0
   * @return minLength
  **/
  @ApiModelProperty(value = "")
  public Integer getMinLength() {
    return minLength;
  }

  public void setMinLength(Integer minLength) {
    this.minLength = minLength;
  }

  public Schema pattern(String pattern) {
    this.pattern = pattern;
    return this;
  }

   /**
   * Get pattern
   * @return pattern
  **/
  @ApiModelProperty(value = "")
  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public Schema maxItems(Integer maxItems) {
    this.maxItems = maxItems;
    return this;
  }

   /**
   * Get maxItems
   * minimum: 0
   * @return maxItems
  **/
  @ApiModelProperty(value = "")
  public Integer getMaxItems() {
    return maxItems;
  }

  public void setMaxItems(Integer maxItems) {
    this.maxItems = maxItems;
  }

  public Schema minItems(Integer minItems) {
    this.minItems = minItems;
    return this;
  }

   /**
   * Get minItems
   * minimum: 0
   * @return minItems
  **/
  @ApiModelProperty(value = "")
  public Integer getMinItems() {
    return minItems;
  }

  public void setMinItems(Integer minItems) {
    this.minItems = minItems;
  }

  public Schema uniqueItems(Boolean uniqueItems) {
    this.uniqueItems = uniqueItems;
    return this;
  }

   /**
   * Get uniqueItems
   * @return uniqueItems
  **/
  @ApiModelProperty(value = "")
  public Boolean getUniqueItems() {
    return uniqueItems;
  }

  public void setUniqueItems(Boolean uniqueItems) {
    this.uniqueItems = uniqueItems;
  }

  public Schema maxProperties(Integer maxProperties) {
    this.maxProperties = maxProperties;
    return this;
  }

   /**
   * Get maxProperties
   * minimum: 0
   * @return maxProperties
  **/
  @ApiModelProperty(value = "")
  public Integer getMaxProperties() {
    return maxProperties;
  }

  public void setMaxProperties(Integer maxProperties) {
    this.maxProperties = maxProperties;
  }

  public Schema minProperties(Integer minProperties) {
    this.minProperties = minProperties;
    return this;
  }

   /**
   * Get minProperties
   * minimum: 0
   * @return minProperties
  **/
  @ApiModelProperty(value = "")
  public Integer getMinProperties() {
    return minProperties;
  }

  public void setMinProperties(Integer minProperties) {
    this.minProperties = minProperties;
  }

  public Schema required(List<String> required) {
    this.required = required;
    return this;
  }

  public Schema addRequiredItem(String requiredItem) {
    this.required.add(requiredItem);
    return this;
  }

   /**
   * Get required
   * @return required
  **/
  @ApiModelProperty(value = "")
  public List<String> getRequired() {
    return required;
  }

  public void setRequired(List<String> required) {
    this.required = required;
  }

  public Schema _enum(List<Object> _enum) {
    this._enum = _enum;
    return this;
  }

  public Schema addEnumItem(Object _enumItem) {
    this._enum.add(_enumItem);
    return this;
  }

   /**
   * testing
   * @return _enum
  **/
  @ApiModelProperty(value = "testing")
  public List<Object> getEnum() {
    return _enum;
  }

  public void setEnum(List<Object> _enum) {
    this._enum = _enum;
  }

  public Schema type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public Schema allOf(List<Schema> allOf) {
    this.allOf = allOf;
    return this;
  }

  public Schema addAllOfItem(Schema allOfItem) {
    this.allOf.add(allOfItem);
    return this;
  }

   /**
   * Get allOf
   * @return allOf
  **/
  @ApiModelProperty(value = "")
  public List<Schema> getAllOf() {
    return allOf;
  }

  public void setAllOf(List<Schema> allOf) {
    this.allOf = allOf;
  }

  public Schema oneOf(List<Schema> oneOf) {
    this.oneOf = oneOf;
    return this;
  }

  public Schema addOneOfItem(Schema oneOfItem) {
    this.oneOf.add(oneOfItem);
    return this;
  }

   /**
   * Get oneOf
   * @return oneOf
  **/
  @ApiModelProperty(value = "")
  public List<Schema> getOneOf() {
    return oneOf;
  }

  public void setOneOf(List<Schema> oneOf) {
    this.oneOf = oneOf;
  }

  public Schema anyOf(List<Schema> anyOf) {
    this.anyOf = anyOf;
    return this;
  }

  public Schema addAnyOfItem(Schema anyOfItem) {
    this.anyOf.add(anyOfItem);
    return this;
  }

   /**
   * Get anyOf
   * @return anyOf
  **/
  @ApiModelProperty(value = "")
  public List<Schema> getAnyOf() {
    return anyOf;
  }

  public void setAnyOf(List<Schema> anyOf) {
    this.anyOf = anyOf;
  }

  public Schema not(Schema not) {
    this.not = not;
    return this;
  }

   /**
   * Get not
   * @return not
  **/
  @ApiModelProperty(value = "")
  public Schema getNot() {
    return not;
  }

  public void setNot(Schema not) {
    this.not = not;
  }

  public Schema items(Schema items) {
    this.items = items;
    return this;
  }

   /**
   * Get items
   * @return items
  **/
  @ApiModelProperty(value = "")
  public Schema getItems() {
    return items;
  }

  public void setItems(Schema items) {
    this.items = items;
  }

  public Schema properties(Map<String, Schema> properties) {
    this.properties = properties;
    return this;
  }

  public Schema addProperties(String key, Schema property) {
    if(this.properties == null) {
      this.properties = new LinkedHashMap<>();
    }
    this.properties.put(key, property);
    return this;
  }

  public Schema putPropertiesItem(String key, Schema propertiesItem) {
    this.properties.put(key, propertiesItem);
    return this;
  }

   /**
   * Get properties
   * @return properties
  **/
  @ApiModelProperty(value = "")
  public Map<String, Schema> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, Schema> properties) {
    this.properties = properties;
  }

  public Schema additionalProperties(Schema additionalProperties) {
    this.additionalProperties = additionalProperties;
    return this;
  }

   /**
   * Get additionalProperties
   * @return additionalProperties
  **/
  @ApiModelProperty(value = "")
  public Schema getAdditionalProperties() {
    return additionalProperties;
  }

  public void setAdditionalProperties(Schema additionalProperties) {
    this.additionalProperties = additionalProperties;
  }

  public Schema description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Schema format(String format) {
    this.format = format;
    return this;
  }

   /**
   * Get format
   * @return format
  **/
  @ApiModelProperty(value = "")
  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public Schema ref(String ref) {
    this.ref = ref;
    return this;
  }

   /**
   * Get ref
   * @return ref
  **/
  @ApiModelProperty(value = "")
  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public Schema nulable(Boolean nulable) {
    this.nulable = nulable;
    return this;
  }

   /**
   * Get nulable
   * @return nulable
  **/
  @ApiModelProperty(value = "")
  public Boolean getNulable() {
    return nulable;
  }

  public void setNulable(Boolean nulable) {
    this.nulable = nulable;
  }

  public Schema discriminator(String discriminator) {
    this.discriminator = discriminator;
    return this;
  }

   /**
   * Get discriminator
   * @return discriminator
  **/
  @ApiModelProperty(value = "")
  public String getDiscriminator() {
    return discriminator;
  }

  public void setDiscriminator(String discriminator) {
    this.discriminator = discriminator;
  }

  public Schema readOnly(Boolean readOnly) {
    this.readOnly = readOnly;
    return this;
  }

   /**
   * Get readOnly
   * @return readOnly
  **/
  @ApiModelProperty(value = "")
  public Boolean getReadOnly() {
    return readOnly;
  }

  public void setReadOnly(Boolean readOnly) {
    this.readOnly = readOnly;
  }

  public Schema writeOnly(Boolean writeOnly) {
    this.writeOnly = writeOnly;
    return this;
  }

   /**
   * Get writeOnly
   * @return writeOnly
  **/
  @ApiModelProperty(value = "")
  public Boolean getWriteOnly() {
    return writeOnly;
  }

  public void setWriteOnly(Boolean writeOnly) {
    this.writeOnly = writeOnly;
  }

  public Schema examples(List<Example> examples) {
    this.examples = examples;
    return this;
  }

  public Schema addExamplesItem(Example examplesItem) {
    this.examples.add(examplesItem);
    return this;
  }

   /**
   * Get examples
   * @return examples
  **/
  @ApiModelProperty(value = "")
  public List<Example> getExamples() {
    return examples;
  }

  public void setExamples(List<Example> examples) {
    this.examples = examples;
  }

  public Schema example(Example example) {
    this.example = example;
    return this;
  }

   /**
   * Get example
   * @return example
  **/
  @ApiModelProperty(value = "")
  public Example getExample() {
    return example;
  }

  public void setExample(Example example) {
    this.example = example;
  }

  public Schema externalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
    return this;
  }

   /**
   * Get externalDocs
   * @return externalDocs
  **/
  @ApiModelProperty(value = "")
  public ExternalDocumentation getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
  }

  public Schema deprecated(Boolean deprecated) {
    this.deprecated = deprecated;
    return this;
  }

   /**
   * Get deprecated
   * @return deprecated
  **/
  @ApiModelProperty(value = "")
  public Boolean getDeprecated() {
    return deprecated;
  }

  public void setDeprecated(Boolean deprecated) {
    this.deprecated = deprecated;
  }

  public Schema xml(XML xml) {
    this.xml = xml;
    return this;
  }

   /**
   * Get xml
   * @return xml
  **/
  @ApiModelProperty(value = "")
  public XML getXml() {
    return xml;
  }

  public void setXml(XML xml) {
    this.xml = xml;
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
        Objects.equals(this._enum, schema._enum) &&
        Objects.equals(this.type, schema.type) &&
        Objects.equals(this.allOf, schema.allOf) &&
        Objects.equals(this.oneOf, schema.oneOf) &&
        Objects.equals(this.anyOf, schema.anyOf) &&
        Objects.equals(this.not, schema.not) &&
        Objects.equals(this.items, schema.items) &&
        Objects.equals(this.properties, schema.properties) &&
        Objects.equals(this.additionalProperties, schema.additionalProperties) &&
        Objects.equals(this.description, schema.description) &&
        Objects.equals(this.format, schema.format) &&
        Objects.equals(this.ref, schema.ref) &&
        Objects.equals(this.nulable, schema.nulable) &&
        Objects.equals(this.discriminator, schema.discriminator) &&
        Objects.equals(this.readOnly, schema.readOnly) &&
        Objects.equals(this.writeOnly, schema.writeOnly) &&
        Objects.equals(this.examples, schema.examples) &&
        Objects.equals(this.example, schema.example) &&
        Objects.equals(this.externalDocs, schema.externalDocs) &&
        Objects.equals(this.deprecated, schema.deprecated) &&
        Objects.equals(this.xml, schema.xml);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, multipleOf, maximum, exclusiveMaximum, minimum, exclusiveMinimum, maxLength, minLength, pattern, maxItems, minItems, uniqueItems, maxProperties, minProperties, required, _enum, type, allOf, oneOf, anyOf, not, items, properties, additionalProperties, description, format, ref, nulable, discriminator, readOnly, writeOnly, examples, example, externalDocs, deprecated, xml);
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
    sb.append("    _enum: ").append(toIndentedString(_enum)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    allOf: ").append(toIndentedString(allOf)).append("\n");
    sb.append("    oneOf: ").append(toIndentedString(oneOf)).append("\n");
    sb.append("    anyOf: ").append(toIndentedString(anyOf)).append("\n");
    sb.append("    not: ").append(toIndentedString(not)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    additionalProperties: ").append(toIndentedString(additionalProperties)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    ref: ").append(toIndentedString(ref)).append("\n");
    sb.append("    nulable: ").append(toIndentedString(nulable)).append("\n");
    sb.append("    discriminator: ").append(toIndentedString(discriminator)).append("\n");
    sb.append("    readOnly: ").append(toIndentedString(readOnly)).append("\n");
    sb.append("    writeOnly: ").append(toIndentedString(writeOnly)).append("\n");
    sb.append("    examples: ").append(toIndentedString(examples)).append("\n");
    sb.append("    example: ").append(toIndentedString(example)).append("\n");
    sb.append("    externalDocs: ").append(toIndentedString(externalDocs)).append("\n");
    sb.append("    deprecated: ").append(toIndentedString(deprecated)).append("\n");
    sb.append("    xml: ").append(toIndentedString(xml)).append("\n");
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

