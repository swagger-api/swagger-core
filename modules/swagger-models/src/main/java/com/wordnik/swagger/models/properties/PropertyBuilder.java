package com.wordnik.swagger.models.properties;

import java.util.*;

public class PropertyBuilder {
  public static Property build(String type, String format, Map<String, Object> args) {
    if(args == null) {
      args = new HashMap<String, Object>();
    }

    List<String> _enum = (List<String>) args.get("enum");
    String title = (String)args.get("title");
    String description = (String)args.get("description");
    String _default = (String)args.get("default");
    String pattern = (String)args.get("pattern");
    String discriminator = (String)args.get("discriminator");
    Integer minItems = (Integer)args.get("minItems");
    Integer maxItems = (Integer)args.get("maxItems");
    Integer minProperties = (Integer)args.get("minProperties");
    Integer maxProperties = (Integer)args.get("maxProperties");
    Integer minLength = (Integer)args.get("minLength");
    Integer maxLength = (Integer)args.get("maxLength");
    Double minimum = (Double)args.get("minimum");
    Double maximum = (Double)args.get("maximum");
    Double exclusiveMinimum = (Double)args.get("exclusiveMinimum");
    Double exclusiveMaximum = (Double)args.get("exclusiveMaximum");
    Boolean uniqueItems = (Boolean)args.get("uniqueItems");

    AbstractProperty property = null;
    if(BooleanProperty.isType(type, format))
      property = new BooleanProperty();
    if(DateProperty.isType(type, format))
      property = new DateProperty();
    if(DateTimeProperty.isType(type, format))
      property = new DateTimeProperty();
    if(DoubleProperty.isType(type, format))
      property = new DoubleProperty()
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMinimum);
    if(FloatProperty.isType(type, format))
      property = new FloatProperty()
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMinimum);
    if(FileProperty.isType(type, format)) {
      property = new FileProperty();
    }
    if(DecimalProperty.isType(type, format))
      property = new DecimalProperty()
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMinimum);
    if(IntegerProperty.isType(type, format))
      property = new IntegerProperty()
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMinimum);
    if(LongProperty.isType(type, format))
      property = new LongProperty()
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMinimum);
    if(RefProperty.isType(type, format))
      property = new RefProperty();
    if(StringProperty.isType(type, format))
      property = new StringProperty()
        .minLength(minLength)
        .maxLength(maxLength)
        .pattern(pattern)
        ._enum(_enum);
    if(UUIDProperty.isType(type, format))
      property = new UUIDProperty()
        .minLength(minLength)
        .maxLength(maxLength)
        .pattern(pattern);
    // fallbacks
    if("integer".equals(type) && format == null) {
        // fall back to Integer if type is integer and format is missing
      System.out.println("no format specified for integer type, falling back to int32");
      property = new IntegerProperty()
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMinimum);
    }
    if("object".equals(type) && format == null) {
        // fall back to Map if type is object and format is missing
      System.out.println("no format specified for object type, falling back to object");
      property = new ObjectProperty();
    }    
    if (property == null) {
      throw new IllegalArgumentException("Invalid property type (type=" + type + ", format=" + format + ")\n" + args);
    }
    // general properties
    property
      .title(title)
      .description(description);
    String example = (String)args.get("example");
    if (example != null) {
      property.setExample(example);
    }
    if (_default != null) {
      property.setDefaultValue(_default);
    }
    return property;
  }
}