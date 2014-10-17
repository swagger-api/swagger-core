package com.wordnik.swagger.models;

/**
 * Container for a <a href="https://github.com/wordnik/swagger-spec/blob/master/versions/2.0.md#externalDocumentationObject">External Documentation Object</a>.
 */
public class ExternalDocs {
  /** A short description of the target documentation. GFM syntax can be used for rich text representation. */
  private String description;

  /** Required. The URL for the target documentation. Value MUST be in the format of a URL. */
  private String url;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}