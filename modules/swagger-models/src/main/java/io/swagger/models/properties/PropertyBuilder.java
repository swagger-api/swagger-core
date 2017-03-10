package io.swagger.models.properties;

import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PropertyBuilder {
    static final Logger LOGGER = LoggerFactory.getLogger(PropertyBuilder.class);

    /**
     * Creates new property on the passed arguments.
     *
     * @param type   property type
     * @param format property format
     * @param args   mapping of argument identifier to value
     * @return new property instance or <code>null</code> for unknown types
     */
    public static Property build(String type, String format, Map<PropertyId, Object> args) {
        final Processor processor = Processor.fromType(type, format);
        if (processor == null) {
            return null;
        }
        final Map<PropertyId, Object> safeArgs = args == null ? Collections.<PropertyId, Object>emptyMap() : args;
        final Map<PropertyId, Object> fixedArgs;
        if (format != null) {
            fixedArgs = new EnumMap<PropertyId, Object>(PropertyId.class);
            fixedArgs.putAll(safeArgs);
            fixedArgs.put(PropertyId.FORMAT, format);
        } else {
            fixedArgs = safeArgs;
        }
        return processor.build(fixedArgs);
    }

    /**
     * Merges passed arguments into an existing property instance.
     *
     * @param property property to be updated
     * @param args     mapping of argument identifier to value. <code>null</code>s
     *                 will replace existing values
     * @return updated property instance
     */
    public static Property merge(Property property, Map<PropertyId, Object> args) {
        if (args != null && !args.isEmpty()) {
            final Processor processor = Processor.fromProperty(property);
            if (processor != null) {
                processor.merge(property, args);
            }
        }
        return property;
    }

    /**
     * Converts passed property into a model.
     *
     * @param property property to be converted
     * @return model instance or <code>null</code> for unknown types
     */
    public static Model toModel(Property property) {
        final Processor processor = Processor.fromProperty(property);
        if (processor != null) {
            return processor.toModel(property);
        }
        return null;
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
        FORMAT("format"),
        READ_ONLY("readOnly"),
        REQUIRED("required"),
        VENDOR_EXTENSIONS("vendorExtensions"),
        ALLOW_EMPTY_VALUE("allowEmptyValue"),
        MULTIPLE_OF("multipleOf");

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
        BOOLEAN(BooleanProperty.class) {
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
        BYTE_ARRAY(ByteArrayProperty.class) {
            @Override
            protected boolean isType(String type, String format) {
                return ByteArrayProperty.isType(type, format);
            }

            @Override
            protected ByteArrayProperty create() {
                return new ByteArrayProperty();
            }

            @Override
            public Property merge(final Property property, final Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof ByteArrayProperty) {
                    final ByteArrayProperty resolved = (ByteArrayProperty) property;
                    mergeString(resolved, args);
                    // the string properties for pattern and enum will be ignored, they doesn't make sense for
                    // base64 encoded strings - instead an appropriate base64 pattern is set
                    resolved.setEnum(null);
                    resolved.setPattern("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");
                }

                return property;
            }

            @Override
            public Model toModel(final Property property) {
                if (isType(property)) {
                    return createStringModel((StringProperty) property);
                }

                return null;
            }
        },
        BINARY(BinaryProperty.class) {
            @Override
            protected boolean isType(String type, String format) {
                return BinaryProperty.isType(type, format);
            }

            @Override
            protected BinaryProperty create() {
                return new BinaryProperty();
            }
        },
        DATE(DateProperty.class) {
            @Override
            protected boolean isType(String type, String format) {
                return DateProperty.isType(type, format);
            }

            @Override
            protected DateProperty create() {
                return new DateProperty();
            }
        },
        DATE_TIME(DateTimeProperty.class) {
            @Override
            protected boolean isType(String type, String format) {
                return DateTimeProperty.isType(type, format);
            }

            @Override
            protected DateTimeProperty create() {
                return new DateTimeProperty();
            }
        },
        INT(IntegerProperty.class) {
            @Override
            protected boolean isType(String type, String format) {
                return IntegerProperty.isType(type, format);
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

            @Override
            public Model toModel(Property property) {
                if (isType(property)) {
                    final IntegerProperty resolved = (IntegerProperty) property;
                    final ModelImpl model = createModel(resolved);
                    final Integer defaultValue = resolved.getDefault();
                    if (defaultValue != null) {
                        model.setDefaultValue(defaultValue.toString());
                    }
                    return model;
                }
                return null;
            }
        },
        LONG(LongProperty.class) {
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

            @Override
            public Model toModel(Property property) {
                if (isType(property)) {
                    final LongProperty resolved = (LongProperty) property;
                    final ModelImpl model = createModel(resolved);
                    final Long defaultValue = resolved.getDefault();
                    if (defaultValue != null) {
                        model.setDefaultValue(defaultValue.toString());
                    }
                    return model;
                }
                return null;
            }
        },
        FLOAT(FloatProperty.class) {
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

            @Override
            public Model toModel(Property property) {
                if (isType(property)) {
                    final FloatProperty resolved = (FloatProperty) property;
                    final ModelImpl model = createModel(resolved);
                    final Float defaultValue = resolved.getDefault();
                    if (defaultValue != null) {
                        model.setDefaultValue(defaultValue.toString());
                    }
                    return model;
                }
                return null;
            }
        },
        DOUBLE(DoubleProperty.class) {
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

            @Override
            public Model toModel(Property property) {
                if (isType(property)) {
                    final DoubleProperty resolved = (DoubleProperty) property;
                    final ModelImpl model = createModel(resolved);
                    final Double defaultValue = resolved.getDefault();
                    if (defaultValue != null) {
                        model.setDefaultValue(defaultValue.toString());
                    }
                    return model;
                }
                return null;
            }
        },

        // note: this must be in the enum order after both INT and LONG
        // (and any integer types added in the future), so the more specific
        // ones will be found first.
        INTEGER(BaseIntegerProperty.class) {
            @Override
            protected boolean isType(String type, String format) {
                return BaseIntegerProperty.isType(type, format);
            }

            @Override
            protected BaseIntegerProperty create() {
                return new BaseIntegerProperty();
            }

            @Override
            public Property merge(Property property, Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof BaseIntegerProperty) {
                    final BaseIntegerProperty resolved = (BaseIntegerProperty) property;
                    mergeNumeric(resolved, args);
                }
                return property;
            }
        },

        // note: this must be in the enum order after both DOUBLE and FLOAT
        // (and any number types added in the future), so the more specific
        // ones will be found first.
        DECIMAL(DecimalProperty.class) {
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
        FILE(FileProperty.class) {
            @Override
            protected boolean isType(String type, String format) {
                return FileProperty.isType(type, format);
            }

            @Override
            protected FileProperty create() {
                return new FileProperty();
            }
        },
        REFERENCE(RefProperty.class) {
            @Override
            protected boolean isType(String type, String format) {
                return RefProperty.isType(type, format);
            }

            @Override
            protected RefProperty create() {
                return new RefProperty();
            }

            @Override
            public Model toModel(Property property) {
                if (property instanceof RefProperty) {
                    final RefProperty resolved = (RefProperty) property;
                    final RefModel model = new RefModel(resolved.get$ref());
                    model.setDescription(resolved.getDescription());
                    return model;
                }
                return null;
            }
        },
        E_MAIL(EmailProperty.class) {
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

            @Override
            public Model toModel(Property property) {
                if (isType(property)) {
                    return createStringModel((StringProperty) property);
                }
                return null;
            }
        },
        UUID(UUIDProperty.class) {
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

            @Override
            public Model toModel(Property property) {
                if (isType(property)) {
                    final UUIDProperty resolved = (UUIDProperty) property;
                    final ModelImpl model = createModel(resolved);
                    model.setDefaultValue(resolved.getDefault());
                    return model;
                }
                return null;
            }
        },
        OBJECT(ObjectProperty.class) {
            @Override
            protected boolean isType(String type, String format) {
                return ObjectProperty.isType(type, format);
            }

            @Override
            protected ObjectProperty create() {
                return new ObjectProperty();
            }
        },
        ARRAY(ArrayProperty.class) {
            @Override
            protected boolean isType(String type, String format) {
                return ArrayProperty.isType(type);
            }

            @Override
            protected ArrayProperty create() {
                return new ArrayProperty();
            }

            @Override
            public Model toModel(Property property) {
                if (property instanceof ArrayProperty) {
                    final ArrayProperty resolved = (ArrayProperty) property;
                    final ArrayModel model = new ArrayModel().items(resolved.getItems()).description(resolved.getDescription());
                    return model;
                }
                return null;
            }

            @Override
            public Property merge(final Property property, final Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof ArrayProperty) {
                    final ArrayProperty resolved = (ArrayProperty) property;
                    if (args.containsKey(PropertyId.MIN_ITEMS)) {
                        final Integer value = PropertyId.MIN_ITEMS.findValue(args);
                        resolved.setMinItems(value);
                    }
                    if (args.containsKey(PropertyId.MAX_ITEMS)) {
                        final Integer value = PropertyId.MAX_ITEMS.findValue(args);
                        resolved.setMaxItems(value);
                    }
                    if (args.containsKey(PropertyId.UNIQUE_ITEMS)) {
                        final Boolean value = PropertyId.UNIQUE_ITEMS.findValue(args);
                        resolved.setUniqueItems(value);
                    }
                }

                return property;
            }
        },
        MAP(MapProperty.class) {
            @Override
            protected boolean isType(String type, String format) {
                // Note: It's impossible to distinct MAP and OBJECT as they use the same
                // set of values for type and format
                return MapProperty.isType(type, format);
            }

            @Override
            protected MapProperty create() {
                return new MapProperty();
            }

            @Override
            public Model toModel(Property property) {
                if (property instanceof MapProperty) {
                    final MapProperty resolved = (MapProperty) property;
                    return createModel(property).additionalProperties(resolved.getAdditionalProperties());
                }
                return null;
            }
        },

        // String is intentionally last, so it is found after the more specific property
        // types which also use the "string" type.
        STRING(StringProperty.class) {
            @Override
            protected boolean isType(final String type, final String format) {
                return StringProperty.isType(type, format);
            }

            @Override
            protected StringProperty create() {
                return new StringProperty();
            }

            @Override
            public Property merge(final Property property, final Map<PropertyId, Object> args) {
                super.merge(property, args);
                if (property instanceof StringProperty) {
                    mergeString((StringProperty) property, args);
                }

                return property;
            }

            @Override
            public Model toModel(final Property property) {
                if (isType(property)) {
                    return createStringModel((StringProperty) property);
                }

                return null;
            }

        },
        ;

        private final Class<? extends Property> type;

        Processor(Class<? extends Property> type) {
            this.type = type;
        }

        public static Processor fromType(String type, String format) {
            for (Processor item : values()) {
                if (item.isType(type, format)) {
                    return item;
                }
            }
            LOGGER.debug("no property for " + type + ", " + format);
            return null;
        }

        public static Processor fromProperty(Property property) {
            for (Processor item : values()) {
                if (item.isType(property)) {
                    return item;
                }
            }
            LOGGER.error("no property for " + (property == null ? "null" : property.getClass().getName()));
            return null;
        }

        protected abstract boolean isType(String type, String format);

        protected boolean isType(Property property) {
            return type.isInstance(property);
        }

        protected abstract Property create();

        protected <N extends AbstractNumericProperty> N mergeNumeric(N property, Map<PropertyId, Object> args) {
            if (args.containsKey(PropertyId.MINIMUM)) {
                final BigDecimal value = PropertyId.MINIMUM.findValue(args);
                if(value != null) {
                    property.setMinimum(value);
                }
            }
            if (args.containsKey(PropertyId.MAXIMUM)) {
                final BigDecimal value = PropertyId.MAXIMUM.findValue(args);
                if(value != null) {
                    property.setMaximum(value);
                }
            }
            if (args.containsKey(PropertyId.EXCLUSIVE_MINIMUM)) {
                final Boolean value = PropertyId.EXCLUSIVE_MINIMUM.findValue(args);
                property.setExclusiveMinimum(value);
            }
            if (args.containsKey(PropertyId.EXCLUSIVE_MAXIMUM)) {
                final Boolean value = PropertyId.EXCLUSIVE_MAXIMUM.findValue(args);
                property.setExclusiveMaximum(value);
            }
            if (args.containsKey(PropertyId.MULTIPLE_OF)) {
                final BigDecimal value = PropertyId.MULTIPLE_OF.findValue(args);
                if(value != null) {
                    property.setMultipleOf(value);
                }
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

        protected ModelImpl createModel(Property property) {
            return new ModelImpl().type(property.getType()).format(property.getFormat())
                    .description(property.getDescription());
        }

        protected ModelImpl createStringModel(StringProperty property) {
            final ModelImpl model = createModel(property);
            model.setDefaultValue(property.getDefault());
            return model;
        }

        /**
         * Creates new property on the passed arguments.
         *
         * @param args mapping of argument identifier to value
         * @return new property instance
         */
        public Property build(Map<PropertyId, Object> args) {
            return merge(create(), args);
        }

        /**
         * Merges passed arguments into an existing property instance.
         *
         * @param property property to be updated
         * @param args     mapping of argument identifier to value. <code>null</code>s
         *                 will replace existing values
         * @return updated property instance
         */
        public Property merge(Property property, Map<PropertyId, Object> args) {
            if(args.containsKey(PropertyId.READ_ONLY)) {
                property.setReadOnly(PropertyId.READ_ONLY.<Boolean>findValue(args));
            }
            if (property instanceof AbstractProperty) {
                final AbstractProperty resolved = (AbstractProperty) property;
                if (resolved.getFormat() == null) {
                    resolved.setFormat(PropertyId.FORMAT.<String>findValue(args));
                }
                if(args.containsKey(PropertyId.ALLOW_EMPTY_VALUE)) {
                    final Boolean value = PropertyId.ALLOW_EMPTY_VALUE.findValue(args);
                    resolved.setAllowEmptyValue(value);
                }
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
                if(args.containsKey(PropertyId.VENDOR_EXTENSIONS)) {
                    final Map<String, Object> value = PropertyId.VENDOR_EXTENSIONS.findValue(args);
                    resolved.setVendorExtensionMap(value);
                }
                if(args.containsKey(PropertyId.ENUM)) {
                    final List<String> values = PropertyId.ENUM.findValue(args);
                    if(values != null) {
                        if(property instanceof BooleanProperty) {
                            BooleanProperty p = (BooleanProperty) property;
                            for(String value : values) {
                                try {
                                    p._enum(Boolean.parseBoolean(value));
                                }
                                catch(Exception e) {
                                    // continue
                                }
                            }
                        }
                        if(property instanceof IntegerProperty) {
                            IntegerProperty p = (IntegerProperty) property;
                            for(String value : values) {
                              try {
                                p._enum(Integer.parseInt(value));
                              }
                              catch(Exception e) {
                                // continue
                              }
                            }
                        }
                        if(property instanceof LongProperty) {
                          LongProperty p = (LongProperty) property;
                          for(String value : values) {
                            try {
                              p._enum(Long.parseLong(value));
                            }
                            catch(Exception e) {
                              // continue
                            }
                          }
                        }
                        if(property instanceof DoubleProperty) {
                            DoubleProperty p = (DoubleProperty) property;
                            for(String value : values) {
                              try {
                                p._enum(Double.parseDouble(value));
                              }
                              catch(Exception e) {
                                // continue
                              }
                            }
                        }
                        if(property instanceof FloatProperty) {
                          FloatProperty p = (FloatProperty) property;
                          for(String value : values) {
                            try {
                              p._enum(Float.parseFloat(value));
                            }
                            catch(Exception e) {
                              // continue
                            }
                          }
                       }
                       if(property instanceof DateProperty) {
                          DateProperty p = (DateProperty) property;
                          for(String value : values) {
                            try {
                              p._enum(value);
                            }
                            catch(Exception e) {
                              // continue
                            }
                          }
                       }
                       if(property instanceof DateTimeProperty) {
                         DateTimeProperty p = (DateTimeProperty) property;
                         for(String value : values) {
                           try {
                             p._enum(value);
                           }
                           catch(Exception e) {
                             // continue
                           }
                         }
                       }
                       if(property instanceof UUIDProperty) {
                         UUIDProperty p = (UUIDProperty) property;
                         for(String value : values) {
                           try {
                             p._enum(value);
                           }
                           catch(Exception e) {
                             // continue
                           }
                         }
                       }
                    }
                }
            }
            return property;
        }

        /**
         * Converts passed property into a model.
         *
         * @param property property to be converted
         * @return model instance or <code>null</code> for unknown types
         */
        public Model toModel(Property property) {
            return createModel(property);
        }
    }
}
