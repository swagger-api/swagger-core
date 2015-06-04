package io.swagger.models.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PropertyBuilder {
  static Logger LOGGER = LoggerFactory.getLogger(PropertyBuilder.class);

  /**
   * Creates new property on the passed arguments.
   * @param type property type
   * @param format property format
   * @param args mapping of argument identifier to value
   * @return new property instance or <code>null</code> for unknown types
   */
  public static Property build(String type, String format, Map<PropertyId, Object> args) {
    final Processor processor = Processor.fromType(type, format);
    if (processor == null) {
      return null;
    }
    if (args == null) {
      args = Collections.emptyMap();
    }
    return processor.build(args);
  }

  /**
   * Merges passed arguments into an existing property instance.
   * @param property property to be updated
   * @param args mapping of argument identifier to value. <code>null</code>s
   *          will replace existing values
   * @return updated property instance
   */
  public static Property merge(Property property, Map<PropertyId, Object> args) {
    if (args != null && !args.isEmpty()) {
      final Processor processor = Processor.fromType(property.getType(), property.getFormat());
      if (processor != null) {
        processor.merge(property, args);
      }
    }
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
      @SuppressWarnings("unchecked")
      final T value = (T) args.get(this);
      return value;
    }
  }

  private enum Processor {
    BOOLEAN {
      @Override
      protected boolean isType(String type, String format) {
        return BooleanProperty.isType(type, format);
      }

      @Override
      protected BooleanProperty create() {
        return new BooleanProperty();
      }

      @Override
      public Property merge(Property property, Map<PropertyId, Object> args) {
        super.merge(property, args);
        if (property instanceof BooleanProperty) {
          final BooleanProperty resolved = (BooleanProperty) property;
          if (args.containsKey(PropertyId.DEFAULT)) {
            final String value = PropertyId.DEFAULT.findValue(args);
            if (value != null) {
              resolved.setDefault(value);
            } else {
              resolved.setDefault((Boolean) null);
            }
          }
        }
        return property;
      }
    },
    STRING {
      @Override
      protected boolean isType(String type, String format) {
        return StringProperty.isType(type, format);
      }

      @Override
      protected StringProperty create() {
        return new StringProperty();
      }

      @Override
      public Property merge(Property property, Map<PropertyId, Object> args) {
        super.merge(property, args);
        if (property instanceof StringProperty) {
          mergeString((StringProperty) property, args);
        }
        return property;
      }
    },
    DATE {
      @Override
      protected boolean isType(String type, String format) {
        return DateProperty.isType(type, format);
      }

      @Override
      protected DateProperty create() {
        return new DateProperty();
      }
    },
    DATE_TIME {
      @Override
      protected boolean isType(String type, String format) {
        return DateTimeProperty.isType(type, format);
      }

      @Override
      protected DateTimeProperty create() {
        return new DateTimeProperty();
      }
    },
    INTEGER {
      @Override
      protected boolean isType(String type, String format) {
        if (IntegerProperty.isType(type, format)) {
          return true;
        }
        if (IntegerProperty.TYPE.equals(type) && format == null) {
          LOGGER.debug("no format specified for integer type, falling back to int32");
          return true;
        }
        return false;
      }

      @Override
      protected IntegerProperty create() {
        return new IntegerProperty();
      }

      @Override
      public Property merge(Property property, Map<PropertyId, Object> args) {
        super.merge(property, args);
        if (property instanceof IntegerProperty) {
          final IntegerProperty resolved = (IntegerProperty) property;
          mergeNumeric(resolved, args);
          if (args.containsKey(PropertyId.DEFAULT)) {
            final String value = PropertyId.DEFAULT.findValue(args);
            if (value != null) {
              resolved.setDefault(value);
            } else {
              resolved.setDefault((Integer) null);
            }
          }
        }
        return property;
      }
    },
    LONG {
      @Override
      protected boolean isType(String type, String format) {
        return LongProperty.isType(type, format);
      }

      @Override
      protected LongProperty create() {
        return new LongProperty();
      }

      @Override
      public Property merge(Property property, Map<PropertyId, Object> args) {
        super.merge(property, args);
        if (property instanceof LongProperty) {
          final LongProperty resolved = (LongProperty) property;
          mergeNumeric(resolved, args);
          if (args.containsKey(PropertyId.DEFAULT)) {
            final String value = PropertyId.DEFAULT.findValue(args);
            if (value != null) {
              resolved.setDefault(value);
            } else {
              resolved.setDefault((Long) null);
            }
          }
        }
        return property;
      }
    },
    FLOAT {
      @Override
      protected boolean isType(String type, String format) {
        return FloatProperty.isType(type, format);
      }

      @Override
      protected FloatProperty create() {
        return new FloatProperty();
      }

      @Override
      public Property merge(Property property, Map<PropertyId, Object> args) {
        super.merge(property, args);
        if (property instanceof FloatProperty) {
          final FloatProperty resolved = (FloatProperty) property;
          mergeNumeric(resolved, args);
          if (args.containsKey(PropertyId.DEFAULT)) {
            final String value = PropertyId.DEFAULT.findValue(args);
            if (value != null) {
              resolved.setDefault(value);
            } else {
              resolved.setDefault((Float) null);
            }
          }
        }
        return property;
      }
    },
    DOUBLE {
      @Override
      protected boolean isType(String type, String format) {
        return DoubleProperty.isType(type, format);
      }

      @Override
      protected DoubleProperty create() {
        return new DoubleProperty();
      }

      @Override
      public Property merge(Property property, Map<PropertyId, Object> args) {
        super.merge(property, args);
        if (property instanceof DoubleProperty) {
          final DoubleProperty resolved = (DoubleProperty) property;
          mergeNumeric(resolved, args);
          if (args.containsKey(PropertyId.DEFAULT)) {
            final String value = PropertyId.DEFAULT.findValue(args);
            if (value != null) {
              resolved.setDefault(value);
            } else {
              resolved.setDefault((Double) null);
            }
          }
        }
        return property;
      }
    },
    DECIMAL {
      @Override
      protected boolean isType(String type, String format) {
        return DecimalProperty.isType(type, format);
      }

      @Override
      protected DecimalProperty create() {
        return new DecimalProperty();
      }

      @Override
      public Property merge(Property property, Map<PropertyId, Object> args) {
        super.merge(property, args);
        if (property instanceof DecimalProperty) {
          final DecimalProperty resolved = (DecimalProperty) property;
          mergeNumeric(resolved, args);
        }
        return property;
      }
    },
    FILE {
      @Override
      protected boolean isType(String type, String format) {
        return FileProperty.isType(type, format);
      }

      @Override
      protected FileProperty create() {
        return new FileProperty();
      }
    },
    REFERENCE {
      @Override
      protected boolean isType(String type, String format) {
        return RefProperty.isType(type, format);
      }

      @Override
      protected RefProperty create() {
        return new RefProperty();
      }
    },
    E_MAIL {
      @Override
      protected boolean isType(String type, String format) {
        return EmailProperty.isType(type, format);
      }

      @Override
      protected EmailProperty create() {
        return new EmailProperty();
      }

      @Override
      public Property merge(Property property, Map<PropertyId, Object> args) {
        super.merge(property, args);
        if (property instanceof EmailProperty) {
          mergeString((EmailProperty) property, args);
        }
        return property;
      }
    },
    UUID {
      @Override
      protected boolean isType(String type, String format) {
        return UUIDProperty.isType(type, format);
      }

      @Override
      protected UUIDProperty create() {
        return new UUIDProperty();
      }

      @Override
      public Property merge(Property property, Map<PropertyId, Object> args) {
        super.merge(property, args);
        if (property instanceof UUIDProperty) {
          final UUIDProperty resolved = (UUIDProperty) property;
          if (args.containsKey(PropertyId.DEFAULT)) {
            final String value = PropertyId.DEFAULT.findValue(args);
            property.setDefault(value);
          }
          if (args.containsKey(PropertyId.MIN_LENGTH)) {
            final Integer value = PropertyId.MIN_LENGTH.findValue(args);
            resolved.setMinLength(value);
          }
          if (args.containsKey(PropertyId.MAX_LENGTH)) {
            final Integer value = PropertyId.MAX_LENGTH.findValue(args);
            resolved.setMaxLength(value);
          }
          if (args.containsKey(PropertyId.PATTERN)) {
            final String value = PropertyId.PATTERN.findValue(args);
            resolved.setPattern(value);
          }
        }
        return property;
      }
    },
    OBJECT {
      @Override
      protected boolean isType(String type, String format) {
        if (ObjectProperty.isType(type, format)) {
          return true;
        }
        if (ObjectProperty.TYPE.equals(type) && format == null) {
          LOGGER.debug("no format specified for object type, falling back to object");
          return true;
        }
        return false;
      }

      @Override
      protected ObjectProperty create() {
        return new ObjectProperty();
      }
    };

    protected abstract boolean isType(String type, String format);

    protected abstract Property create();

    protected <N extends AbstractNumericProperty> N mergeNumeric(N property, Map<PropertyId, Object> args) {
      if (args.containsKey(PropertyId.MINIMUM)) {
        final Double value = PropertyId.MINIMUM.findValue(args);
        property.setMinimum(value);
      }
      if (args.containsKey(PropertyId.MAXIMUM)) {
        final Double value = PropertyId.MAXIMUM.findValue(args);
        property.setMaximum(value);
      }
      if (args.containsKey(PropertyId.EXCLUSIVE_MINIMUM)) {
        final Boolean value = PropertyId.EXCLUSIVE_MINIMUM.findValue(args);
        property.setExclusiveMinimum(value);
      }
      if (args.containsKey(PropertyId.EXCLUSIVE_MAXIMUM)) {
        final Boolean value = PropertyId.EXCLUSIVE_MAXIMUM.findValue(args);
        property.setExclusiveMaximum(value);
      }
      return property;
    }

    protected <N extends StringProperty> N mergeString(N property, Map<PropertyId, Object> args) {
      if (args.containsKey(PropertyId.DEFAULT)) {
        final String value = PropertyId.DEFAULT.findValue(args);
        property.setDefault(value);
      }
      if (args.containsKey(PropertyId.MIN_LENGTH)) {
        final Integer value = PropertyId.MIN_LENGTH.findValue(args);
        property.setMinLength(value);
      }
      if (args.containsKey(PropertyId.MAX_LENGTH)) {
        final Integer value = PropertyId.MAX_LENGTH.findValue(args);
        property.setMaxLength(value);
      }
      if (args.containsKey(PropertyId.PATTERN)) {
        final String value = PropertyId.PATTERN.findValue(args);
        property.setPattern(value);
      }
      if (args.containsKey(PropertyId.ENUM)) {
        final List<String> value = PropertyId.ENUM.findValue(args);
        property.setEnum(value);
      }
      return property;
    }

    /**
     * Creates new property on the passed arguments.
     * @param args mapping of argument identifier to value
     * @return new property instance
     */
    public Property build(Map<PropertyId, Object> args) {
      return merge(create(), args);
    }

    /**
     * Merges passed arguments into an existing property instance.
     * @param property property to be updated
     * @param args mapping of argument identifier to value. <code>null</code>s
     *          will replace existing values
     * @return updated property instance
     */
    public Property merge(Property property, Map<PropertyId, Object> args) {
      if (property instanceof AbstractProperty) {
        final AbstractProperty resolved = (AbstractProperty) property;
        if (args.containsKey(PropertyId.TITLE)) {
          final String value = PropertyId.TITLE.findValue(args);
          resolved.setTitle(value);
        }
        if (args.containsKey(PropertyId.DESCRIPTION)) {
          final String value = PropertyId.DESCRIPTION.findValue(args);
          resolved.setDescription(value);
        }
        if (args.containsKey(PropertyId.EXAMPLE)) {
          final String value = PropertyId.EXAMPLE.findValue(args);
          resolved.setExample(value);
        }
      }
      return property;
    }

    public static Processor fromType(String type, String format) {
      for (Processor item : values()) {
        if (item.isType(type, format)) {
          return item;
        }
      }
      LOGGER.error("no property for " + type + ", " + format);
      return null;
    }
  }
}
