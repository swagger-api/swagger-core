package com.wordnik.swagger.models.properties;

public class FileProperty extends AbstractProperty implements Property {
  public FileProperty() {
    super.type = "file";
  }

  public static boolean isType(String type, String format) {
    if(type != null && "file".equals(type.toLowerCase()))
      return true;
    else return false;
  }
}