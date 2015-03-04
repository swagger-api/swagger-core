package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.*;

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