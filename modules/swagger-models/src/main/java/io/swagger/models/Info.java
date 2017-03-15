package io.swagger.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * try this at home for fun!
 */
@ApiModel(description = "try this at home for fun!")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T10:33:02.362-07:00")
public class Info {
  @JsonProperty("title")
  private String title = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("termsOfService")
  private String termsOfService = null;

  @JsonProperty("contact")
  private Contact contact = null;

  @JsonProperty("license")
  private License license = null;

  @JsonProperty("version")
  private String version = null;

  public Info title(String title) {
    this.title = title;
    return this;
  }

   /**
   * Get title
   * @return title
  **/
  @ApiModelProperty(required = true, value = "")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Info description(String description) {
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

  public Info termsOfService(String termsOfService) {
    this.termsOfService = termsOfService;
    return this;
  }

   /**
   * Get termsOfService
   * @return termsOfService
  **/
  @ApiModelProperty(value = "")
  public String getTermsOfService() {
    return termsOfService;
  }

  public void setTermsOfService(String termsOfService) {
    this.termsOfService = termsOfService;
  }

  public Info contact(Contact contact) {
    this.contact = contact;
    return this;
  }

   /**
   * Get contact
   * @return contact
  **/
  @ApiModelProperty(value = "")
  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public Info license(License license) {
    this.license = license;
    return this;
  }

   /**
   * Get license
   * @return license
  **/
  @ApiModelProperty(value = "")
  public License getLicense() {
    return license;
  }

  public void setLicense(License license) {
    this.license = license;
  }

  public Info version(String version) {
    this.version = version;
    return this;
  }

   /**
   * Get version
   * @return version
  **/
  @ApiModelProperty(required = true, value = "")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Info info = (Info) o;
    return Objects.equals(this.title, info.title) &&
        Objects.equals(this.description, info.description) &&
        Objects.equals(this.termsOfService, info.termsOfService) &&
        Objects.equals(this.contact, info.contact) &&
        Objects.equals(this.license, info.license) &&
        Objects.equals(this.version, info.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, termsOfService, contact, license, version);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Info {\n");
    
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    termsOfService: ").append(toIndentedString(termsOfService)).append("\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    license: ").append(toIndentedString(license)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

