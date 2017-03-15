package io.swagger.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * MediaType
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class MediaType {
  @JsonProperty("schema")
  private Schema schema = null;

  @JsonProperty("examples")
  private List<Example> examples = new ArrayList<Example>();

  @JsonProperty("example")
  private Example example = null;

  @JsonProperty("Encoding")
  private Encoding encoding = null;

  public MediaType schema(Schema schema) {
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

  public MediaType examples(List<Example> examples) {
    this.examples = examples;
    return this;
  }

  public MediaType addExamplesItem(Example examplesItem) {
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

  public MediaType example(Example example) {
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

  public MediaType encoding(Encoding encoding) {
    this.encoding = encoding;
    return this;
  }

   /**
   * Get encoding
   * @return encoding
  **/
  @ApiModelProperty(value = "")
  public Encoding getEncoding() {
    return encoding;
  }

  public void setEncoding(Encoding encoding) {
    this.encoding = encoding;
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
        Objects.equals(this.encoding, mediaType.encoding);
  }

  @Override
  public int hashCode() {
    return Objects.hash(schema, examples, example, encoding);
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

