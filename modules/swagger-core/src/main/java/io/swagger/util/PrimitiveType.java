package io.swagger.util;

import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.models.properties.*;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The <code>PrimitiveType</code> enumeration defines a mapping of limited set
 * of classes into Swagger primitive types.
 */
public enum PrimitiveType {
    /**
     * Boolean.
     */
    BOOLEAN(Boolean.class, BooleanProperty.TYPE) {
        @Override
        public BooleanProperty createProperty() {
            return new BooleanProperty();
        }
    },
    /**
     * String.
     */
    STRING(String.class, StringProperty.TYPE) {
        @Override
        public StringProperty createProperty() {
            return new StringProperty();
        }
    },
    /**
     * Byte.
     */
    BYTE(Byte.class, "byte") {
        @Override
        public ByteArrayProperty createProperty() {
            return new ByteArrayProperty();
        }
    },
    /**
     * Binary
     */
    BINARY(Byte.class, "binary") {
        @Override
        public BinaryProperty createProperty() {
            return new BinaryProperty();
        }
    },
    /**
     * URI string.
     */
    URI(java.net.URI.class) {
        @Override
        public StringProperty createProperty() {
            return new StringProperty(StringProperty.Format.URI);
        }
    },
    /**
     * URL string.
     */
    URL(java.net.URL.class) {
        @Override
        public StringProperty createProperty() {
            return new StringProperty(StringProperty.Format.URL);
        }
    },
    /**
     * UUID string.
     */
    UUID(java.util.UUID.class) {
        @Override
        public UUIDProperty createProperty() {
            return new UUIDProperty();
        }
    },
    /**
     * 32-bit integer.
     */
    INT(Integer.class, IntegerProperty.TYPE) {
        @Override
        public IntegerProperty createProperty() {
            return new IntegerProperty();
        }
    },
    /**
     * 64-bit integer.
     */
    LONG(Long.class, "long") {
        @Override
        public LongProperty createProperty() {
            return new LongProperty();
        }
    },
    /**
     * 32-bit decimal.
     */
    FLOAT(Float.class, "float") {
        @Override
        public FloatProperty createProperty() {
            return new FloatProperty();
        }
    },
    /**
     * 64-bit decimal.
     */
    DOUBLE(Double.class, "double") {
        @Override
        public DoubleProperty createProperty() {
            return new DoubleProperty();
        }
    },
    /**
     * Generic integer number without specific format.
     */
    INTEGER(java.math.BigInteger.class) {
        @Override
        public BaseIntegerProperty createProperty() {
            return new BaseIntegerProperty();
        }
    },
    /**
     * Generic decimal number without specific format.
     */
    DECIMAL(java.math.BigDecimal.class, "number") {
        @Override
        public DecimalProperty createProperty() {
            return new DecimalProperty();
        }
    },
    /**
     * Date.
     */
    DATE(DateStub.class, "date") {
        @Override
        public DateProperty createProperty() {
            return new DateProperty();
        }
    },
    /**
     * Date and time.
     */
    DATE_TIME(java.util.Date.class, "dateTime") {
        @Override
        public DateTimeProperty createProperty() {
            return new DateTimeProperty();
        }
    },
    /**
     * Native Java file.
     */
    FILE(java.io.File.class, "file") {
        @Override
        public FileProperty createProperty() {
            return new FileProperty();
        }
    },
    /**
     * Generic object.
     */
    OBJECT(Object.class) {
        @Override
        public ObjectProperty createProperty() {
            return new ObjectProperty();
        }
    };

    private static final Map<Class<?>, PrimitiveType> KEY_CLASSES;
    private static final Map<Class<?>, PrimitiveType> BASE_CLASSES;
    /**
     * Adds support of a small number of "well-known" types, specifically for
     * Joda lib.
     */
    private static final Map<String, PrimitiveType> EXTERNAL_CLASSES;
    /**
     * Alternative names for primitive types that have to be supported for
     * backward compatibility.
     */
    private static final Map<String, PrimitiveType> NAMES;
    private final Class<?> keyClass;
    private final String commonName;

    static {
        final Map<Class<?>, PrimitiveType> keyClasses = new HashMap<Class<?>, PrimitiveType>();
        addKeys(keyClasses, BOOLEAN, Boolean.class, Boolean.TYPE);
        addKeys(keyClasses, STRING, String.class, Character.class, Character.TYPE);
        addKeys(keyClasses, BYTE, Byte.class, Byte.TYPE);
        addKeys(keyClasses, URL, java.net.URL.class);
        addKeys(keyClasses, URI, java.net.URI.class);
        addKeys(keyClasses, UUID, java.util.UUID.class);
        addKeys(keyClasses, INT, Integer.class, Integer.TYPE, Short.class, Short.TYPE);
        addKeys(keyClasses, LONG, Long.class, Long.TYPE);
        addKeys(keyClasses, FLOAT, Float.class, Float.TYPE);
        addKeys(keyClasses, DOUBLE, Double.class, Double.TYPE);
        addKeys(keyClasses, INTEGER, java.math.BigInteger.class);
        addKeys(keyClasses, DECIMAL, java.math.BigDecimal.class);
        addKeys(keyClasses, DATE, DateStub.class);
        addKeys(keyClasses, DATE_TIME, java.util.Date.class);
        addKeys(keyClasses, FILE, java.io.File.class);
        addKeys(keyClasses, OBJECT, Object.class);
        KEY_CLASSES = Collections.unmodifiableMap(keyClasses);

        final Map<Class<?>, PrimitiveType> baseClasses = new HashMap<Class<?>, PrimitiveType>();
        addKeys(baseClasses, DATE_TIME, java.util.Date.class, java.util.Calendar.class);
        BASE_CLASSES = Collections.unmodifiableMap(baseClasses);

        final Map<String, PrimitiveType> externalClasses = new HashMap<String, PrimitiveType>();
        addKeys(externalClasses, DATE, "org.joda.time.LocalDate", "java.time.LocalDate");
        addKeys(externalClasses, DATE_TIME, "org.joda.time.DateTime", "org.joda.time.ReadableDateTime",
                "javax.xml.datatype.XMLGregorianCalendar", "java.time.LocalDateTime", "java.time.ZonedDateTime",
                "java.time.OffsetDateTime");
        addKeys(externalClasses, LONG, "java.time.Instant");                
        EXTERNAL_CLASSES = Collections.unmodifiableMap(externalClasses);

        final Map<String, PrimitiveType> names = new TreeMap<String, PrimitiveType>(String.CASE_INSENSITIVE_ORDER);
        for (PrimitiveType item : values()) {
            final String name = item.getCommonName();
            if (name != null) {
                addKeys(names, item, name);
            }
        }
        addKeys(names, INT, "int");
        addKeys(names, OBJECT, ObjectProperty.TYPE);
        NAMES = Collections.unmodifiableMap(names);
    }

    private PrimitiveType(Class<?> keyClass) {
        this(keyClass, null);
    }

    private PrimitiveType(Class<?> keyClass, String commonName) {
        this.keyClass = keyClass;
        this.commonName = commonName;
    }

    public static PrimitiveType fromType(Type type) {
        final Class<?> raw = TypeFactory.defaultInstance().constructType(type).getRawClass();
        final PrimitiveType key = KEY_CLASSES.get(raw);
        if (key != null) {
            return key;
        }
        final PrimitiveType external = EXTERNAL_CLASSES.get(raw.getName());
        if (external != null) {
            return external;
        }
        for (Map.Entry<Class<?>, PrimitiveType> entry : BASE_CLASSES.entrySet()) {
            if (entry.getKey().isAssignableFrom(raw)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static PrimitiveType fromName(String name) {
        if(name == null) {
            return null;
        }
        PrimitiveType fromName = NAMES.get(name);
        if(fromName == null) {
            fromName = EXTERNAL_CLASSES.get(name);
        }
        return fromName;
    }

    public static Property createProperty(Type type) {
        final PrimitiveType item = fromType(type);
        return item == null ? null : item.createProperty();
    }

    public static Property createProperty(String name) {
        final PrimitiveType item = fromName(name);
        return item == null ? null : item.createProperty();
    }

    public static String getCommonName(Type type) {
        final PrimitiveType item = fromType(type);
        return item == null ? null : item.getCommonName();
    }

    public Class<?> getKeyClass() {
        return keyClass;
    }

    public String getCommonName() {
        return commonName;
    }

    public abstract Property createProperty();

    private static <K> void addKeys(Map<K, PrimitiveType> map, PrimitiveType type, K... keys) {
        for (K key : keys) {
            map.put(key, type);
        }
    }

    private static class DateStub {
        private DateStub() {
        }
    }
}
