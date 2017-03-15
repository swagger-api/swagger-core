package io.swagger.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Header
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class Header {
  @JsonProperty("description")
  private String description = null;

  @JsonProperty("required")
  private Boolean required = null;

  @JsonProperty("deprecated")
  private Boolean deprecated = null;

  @JsonProperty("allowEmptyValue")
  private Boolean allowEmptyValue = null;

  /**
   * Gets or Sets style
   */
  public enum StyleEnum {
    SIMPLE("simple");

    private String value;

    StyleEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StyleEnum fromValue(String text) {
      for (StyleEnum b : StyleEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("style")
  private StyleEnum style = null;

  @JsonProperty("explode")
  private Boolean explode = null;

  @JsonProperty("schema")
  private Schema schema = null;

  @JsonProperty("examples")
  private List<Example> examples = new ArrayList<Example>();

  @JsonProperty("example")
  private Example example = null;

  @JsonProperty("content")
  private Content content = null;

  public Header description(String description) {
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

  public Header required(Boolean required) {
    this.required = required;
    return this;
  }

   /**
   * Get required
   * @return required
  **/
  @ApiModelProperty(value = "")
  public Boolean getRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  public Header deprecated(Boolean deprecated) {
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

  public Header allowEmptyValue(Boolean allowEmptyValue) {
    this.allowEmptyValue = allowEmptyValue;
    return this;
  }

   /**
   * Get allowEmptyValue
   * @return allowEmptyValue
  **/
  @ApiModelProperty(value = "")
  public Boolean getAllowEmptyValue() {
    return allowEmptyValue;
  }

  public void setAllowEmptyValue(Boolean allowEmptyValue) {
    this.allowEmptyValue = allowEmptyValue;
  }

  public Header style(StyleEnum style) {
    this.style = style;
    return this;
  }

   /**
   * Get style
   * @return style
  **/
  @ApiModelProperty(value = "")
  public StyleEnum getStyle() {
    return style;
  }

  public void setStyle(StyleEnum style) {
    this.style = style;
  }

  public Header explode(Boolean explode) {
    this.explode = explode;
    return this;
  }

   /**
   * Get explode
   * @return explode
  **/
  @ApiModelProperty(value = "")
  public Boolean getExplode() {
    return explode;
  }

  public void setExplode(Boolean explode) {
    this.explode = explode;
  }

  public Header schema(Schema schema) {
    this.schema = schema;
    return this;
  }

   /**
   * Get schema
   * @return schema
  **/
  @ApiModelProperty(value = "")
  public Schema getSchema() {
    return schema;
  }

  public void setSchema(Schema schema) {
    this.schema = schema;
  }

  public Header examples(List<Example> examples) {
    this.examples = examples;
    return this;
  }

  public Header addExamplesItem(Example examplesItem) {
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

  public Header example(Example example) {
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

  public Header content(Content content) {
    this.content = content;
    return this;
  }

   /**
   * Get content
   * @return content
  **/
  @ApiModelProperty(value = "")
  public Content getContent() {
    return content;
  }

  public void setContent(Content content) {
    this.content = content;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Header header = (Header) o;
    return Objects.equals(this.description, header.description) &&
        Objects.equals(this.required, header.required) &&
        Objects.equals(this.deprecated, header.deprecated) &&
        Objects.equals(this.allowEmptyValue, header.allowEmptyValue) &&
        Objects.equals(this.style, header.style) &&
        Objects.equals(this.explode, header.explode) &&
        Objects.equals(this.schema, header.schema) &&
        Objects.equals(this.examples, header.examples) &&
        Objects.equals(this.example, header.example) &&
        Objects.equals(this.content, header.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, required, deprecated, allowEmptyValue, style, explode, schema, examples, example, content);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Header {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
    sb.append("    deprecated: ").append(toIndentedString(deprecated)).append("\n");
    sb.append("    allowEmptyValue: ").append(toIndentedString(allowEmptyValue)).append("\n");
    sb.append("    style: ").append(toIndentedString(style)).append("\n");
    sb.append("    explode: ").append(toIndentedString(explode)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
    sb.append("    examples: ").append(toIndentedString(examples)).append("\n");
    sb.append("    example: ").append(toIndentedString(example)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
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

