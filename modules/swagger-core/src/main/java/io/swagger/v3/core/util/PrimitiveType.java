package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.models.media.BinarySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ByteArraySchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.FileSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import org.apache.commons.lang3.StringUtils;

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
    STRING(String.class, "string") {
        public Schema createProperty() {
            return new StringSchema();
        }
    },
    BOOLEAN(Boolean.class, "boolean") {
        @Override
        public Schema createProperty() {
            return new BooleanSchema();
        }
    },
    BYTE(Byte.class, "byte") {
        @Override
        public ByteArraySchema createProperty() {
            return new ByteArraySchema();
        }
    },
    BINARY(Byte.class, "binary") {
        @Override
        public BinarySchema createProperty() {
            return new BinarySchema();
        }
    },
    URI(java.net.URI.class, "uri") {
        @Override
        public Schema createProperty() {
            return new StringSchema().format("uri");
        }
    },
    URL(java.net.URL.class, "url") {
        @Override
        public Schema createProperty() {
            return new StringSchema().format("url");
        }
    },
    EMAIL(String.class, "email") {
        @Override
        public Schema createProperty() {
            return new StringSchema().format("email");
        }
    },
    UUID(java.util.UUID.class, "uuid") {
        @Override
        public UUIDSchema createProperty() {
            return new UUIDSchema();
        }
    },
    INT(Integer.class, "integer") {
        @Override
        public IntegerSchema createProperty() {
            return new IntegerSchema();
        }
    },
    LONG(Long.class, "long") {
        @Override
        public Schema createProperty() {
            return new IntegerSchema().format("int64");
        }
    },
    FLOAT(Float.class, "float") {
        @Override
        public Schema createProperty() {
            return new NumberSchema().format("float");
        }
    },
    DOUBLE(Double.class, "double") {
        @Override
        public Schema createProperty() {
            return new NumberSchema().format("double");
        }
    },
    INTEGER(java.math.BigInteger.class) {
        @Override
        public Schema createProperty() {
            return new IntegerSchema().format(null);
        }
    },
    DECIMAL(java.math.BigDecimal.class, "number") {
        @Override
        public Schema createProperty() {
            return new NumberSchema();
        }
    },
    DATE(DateStub.class, "date") {
        @Override
        public DateSchema createProperty() {
            return new DateSchema();
        }
    },
    DATE_TIME(java.util.Date.class, "date-time") {
        @Override
        public DateTimeSchema createProperty() {
            return new DateTimeSchema();
        }
    },
    FILE(java.io.File.class, "file") {
        @Override
        public FileSchema createProperty() {
            return new FileSchema();
        }
    },
    OBJECT(Object.class) {
        @Override
        public Schema createProperty() {
            return new Schema().type("object");
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

    public static final Map<String, String> datatypeMappings = ImmutableMap.<String, String>builder()
            .put("integer_int32", "integer")
            .put("integer_", "integer")
            .put("integer_int64", "long")
            .put("number_", "number")
            .put("number_float", "float")
            .put("number_double", "double")
            .put("string_", "string")
            .put("string_byte", "byte")
            .put("string_email", "email")
            .put("string_binary", "binary")
            .put("string_uri", "uri")
            .put("string_url", "url")
            .put("string_uuid", "uuid")
            .put("string_date", "date")
            .put("string_date-time", "date-time")
            .put("string_password", "password")
            .put("boolean", "boolean")
            .put("object_", "object")
            .build();


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
        addKeys(names, OBJECT, "object");
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

    public static PrimitiveType fromTypeAndFormat(String type, String format) {
        if ( StringUtils.isNotBlank(type) && type.equals("object")) {
            return null;
        }
        return fromName(datatypeMappings.get(String.format("%s_%s", StringUtils.isBlank(type) ? "" : type, StringUtils.isBlank(format) ? "" : format)));
    }

    public static Schema createProperty(Type type) {
        final PrimitiveType item = fromType(type);
        return item == null ? null : item.createProperty();
    }

    public static Schema createProperty(String name) {
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

    public abstract Schema createProperty();

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
