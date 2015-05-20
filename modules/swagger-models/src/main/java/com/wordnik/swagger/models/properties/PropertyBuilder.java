package com.wordnik.swagger.models.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PropertyBuilder {
  static Logger LOGGER = LoggerFactory.getLogger(PropertyBuilder.class);

  public static Property build(String type, String format, Map<PropertyId, Object> args) {
    if(args == null) {
      args = Collections.emptyMap();
    }

    List<String> _enum = PropertyId.ENUM.findValue(args);
    String title = PropertyId.TITLE.findValue(args);
    String description = PropertyId.DESCRIPTION.findValue(args);
    String _default = PropertyId.DEFAULT.findValue(args);
    String pattern = PropertyId.PATTERN.findValue(args);
    Integer minLength = PropertyId.MIN_LENGTH.findValue(args);
    Integer maxLength = PropertyId.MAX_LENGTH.findValue(args);
    Double minimum = PropertyId.MINIMUM.findValue(args);
    Double maximum = PropertyId.MAXIMUM.findValue(args);
    Boolean exclusiveMinimum = PropertyId.EXCLUSIVE_MINIMUM.findValue(args);
    Boolean exclusiveMaximum = PropertyId.EXCLUSIVE_MAXIMUM.findValue(args);

    AbstractProperty property = null;
    if(BooleanProperty.isType(type, format)) {
      property = new BooleanProperty()
        ._default(_default);
    }
    if(DateProperty.isType(type, format))
      property = new DateProperty();
    if(DateTimeProperty.isType(type, format))
      property = new DateTimeProperty();
    if(DoubleProperty.isType(type, format)) {
      property = new DoubleProperty()
        ._default(_default)
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMaximum);
    }
    if(FloatProperty.isType(type, format)) {
      property = new FloatProperty()
        ._default(_default)
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMaximum);
    }
    if(FileProperty.isType(type, format)) {
      property = new FileProperty();
    }
    if(DecimalProperty.isType(type, format))
      property = new DecimalProperty()
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMaximum);
    if(IntegerProperty.isType(type, format)) {
      property = new IntegerProperty()
        ._default(_default)
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMaximum);
    }
    if(LongProperty.isType(type, format)) {
      property = new LongProperty()
        ._default(_default)
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMaximum);
    }
    if(RefProperty.isType(type, format))
      property = new RefProperty();
    if(EmailProperty.isType(type, format))
      property = new EmailProperty()
        .minLength(minLength)
        .maxLength(maxLength)
        .pattern(pattern)
        ._enum(_enum);
    if(StringProperty.isType(type, format)) {
      property = new StringProperty()
        ._default(_default)
        .minLength(minLength)
        .maxLength(maxLength)
        .pattern(pattern)
        ._enum(_enum);
    }
    if(UUIDProperty.isType(type, format)) {
      property = new UUIDProperty()
        ._default(_default)
        .minLength(minLength)
        .maxLength(maxLength)
        .pattern(pattern);
    }
    if(ByteArrayProperty.isType(type, format)){
        property = new ByteArrayProperty();
    }
    // general properties
    if(property != null) {
      property
        .title(title)
        .description(description);
      String example = PropertyId.EXAMPLE.findValue (args);
      if (example != null) {
        property.setExample(example);
      }
    }
    // fallbacks
    if("integer".equals(type) && format == null) {
        // fall back to Integer if type is integer and format is missing
      LOGGER.debug("no format specified for integer type, falling back to int32");
      property = new IntegerProperty()
        ._default(_default)
        .minimum(minimum)
        .maximum(maximum)
        .exclusiveMinimum(exclusiveMinimum)
        .exclusiveMaximum(exclusiveMaximum);
    }
    if(ObjectProperty.isType(type) && format == null) {
        // fall back to Map if type is object and format is missing
      LOGGER.debug("no format specified for object type, falling back to object");
      property = new ObjectProperty();
    }    
    if(property == null)
      LOGGER.debug("no property for " + type + ", " + format);
    return property;
  }

  public enum PropertyId {
    ENUM("enum"),
    TITLE("title"),
    DESCRIPTION("description"),
    DEFAULT("default"),
    PATTERN("pattern"),
    DESCRIMINATOR("discriminator"),
    MIN_ITEMS("minItems"),
    MAX_ITEMS("maxItems"),
    MIN_PROPERTIES("minProperties"),
    MAX_PROPERTIES("maxProperties"),
    MIN_LENGTH("minLength"),
    MAX_LENGTH("maxLength"),
    MINIMUM("minimum"),
    MAXIMUM("maximum"),
    EXCLUSIVE_MINIMUM("exclusiveMinimum"),
    EXCLUSIVE_MAXIMUM("exclusiveMaximum"),
    UNIQUE_ITEMS("uniqueItems"),
    EXAMPLE("example"),
    TYPE("type"),
    FORMAT("format");

    private String propertyName;

    private PropertyId(String propertyName) {
      this.propertyName = propertyName;
    }

    public String getPropertyName() {
      return propertyName;
    }

    public <T> T findValue(Map<PropertyId, Object> args) {
      return (T) args.get(this);
    }
  }
}
