package io.swagger.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * XML
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class XML {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("namespace")
  private String namespace = null;

  @JsonProperty("prefix")
  private String prefix = null;

  @JsonProperty("attribute")
  private Boolean attribute = false;

  @JsonProperty("wrapperd")
  private Boolean wrapperd = false;

  public XML name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public XML namespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

   /**
   * Get namespace
   * @return namespace
  **/
  @ApiModelProperty(value = "")
  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public XML prefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

   /**
   * Get prefix
   * @return prefix
  **/
  @ApiModelProperty(value = "")
  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public XML attribute(Boolean attribute) {
    this.attribute = attribute;
    return this;
  }

   /**
   * Get attribute
   * @return attribute
  **/
  @ApiModelProperty(value = "")
  public Boolean getAttribute() {
    return attribute;
  }

  public void setAttribute(Boolean attribute) {
    this.attribute = attribute;
  }

  public XML wrapperd(Boolean wrapperd) {
    this.wrapperd = wrapperd;
    return this;
  }

   /**
   * Get wrapperd
   * @return wrapperd
  **/
  @ApiModelProperty(value = "")
  public Boolean getWrapperd() {
    return wrapperd;
  }

  public void setWrapperd(Boolean wrapperd) {
    this.wrapperd = wrapperd;
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
        Objects.equals(this.wrapperd, XML.wrapperd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, namespace, prefix, attribute, wrapperd);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XML {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    namespace: ").append(toIndentedString(namespace)).append("\n");
    sb.append("    prefix: ").append(toIndentedString(prefix)).append("\n");
    sb.append("    attribute: ").append(toIndentedString(attribute)).append("\n");
    sb.append("    wrapperd: ").append(toIndentedString(wrapperd)).append("\n");
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

