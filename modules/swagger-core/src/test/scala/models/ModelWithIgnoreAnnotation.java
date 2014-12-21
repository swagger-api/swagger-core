package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ModelWithIgnoreAnnotation {
  private String name, ignoredString;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonIgnore
  public String getIgnoredString() {
    return ignoredString;
  }

  public void setIgnoredString(String ignoredString) {
    this.ignoredString = ignoredString;
  }

  @JsonIgnore
  public String getMiddleName() {
    return "ignore middle name";
  }

  public void setMiddleName(String name) {}
}
