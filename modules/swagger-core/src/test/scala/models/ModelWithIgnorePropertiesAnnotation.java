package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"ignoredString", "middleName", "favoriteColor"})
public class ModelWithIgnorePropertiesAnnotation {
  private String name, ignoredString;

  public String favoriteColor;
  public String favoriteAnimal;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIgnoredString() {
    return ignoredString;
  }

  public void setIgnoredString(String ignoredString) {
    this.ignoredString = ignoredString;
  }

  public String getMiddleName() {
    return "ignore middle name";
  }

  public void setMiddleName(String name) {}
}
