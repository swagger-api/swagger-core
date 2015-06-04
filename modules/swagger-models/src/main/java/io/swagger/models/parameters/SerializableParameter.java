package io.swagger.models.parameters;

import io.swagger.models.properties.Property;

import java.util.List;

public interface SerializableParameter extends Parameter {
  String getType();
  void setType(String type);

  void setItems(Property items);
  Property getItems();

  String getFormat();
  void setFormat(String format);

  String getCollectionFormat();
  void setCollectionFormat(String collectionFormat);

  List<String> getEnum();
  void setEnum(List<String> _enum);
}