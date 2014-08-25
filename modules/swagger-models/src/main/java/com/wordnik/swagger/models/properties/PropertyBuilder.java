package com.wordnik.swagger.models.properties;

public class PropertyBuilder {
  public static Property build(String type, String format) {
    if(BooleanProperty.isType(type, format))
      return new BooleanProperty();
    if(DateProperty.isType(type, format))
      return new DateProperty();
    if(DateTimeProperty.isType(type, format))
      return new DateTimeProperty();
    if(DoubleProperty.isType(type, format))
      return new DoubleProperty();
    if(FloatProperty.isType(type, format))
      return new FloatProperty();
    if(IntegerProperty.isType(type, format))
      return new IntegerProperty();
    if(LongProperty.isType(type, format))
      return new LongProperty();
    if(RefProperty.isType(type, format))
      return new RefProperty();
    if(StringProperty.isType(type, format))
      return new StringProperty();
    return null;
  }
}