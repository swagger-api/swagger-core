package io.swagger.converter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public enum PrimitiveType {

    INT(Integer.class, "int", "integer"),
    LONG(Long.class, "long"),
    FLOAT(Float.class, "float"),
    DOUBLE(Double.class, "double"),
    STRING(String.class, "string"),
    BYTE(Byte.class, "byte"),
    BOOLEAN(Boolean.class, "boolean"),
    URI(java.net.URI.class, "uri"),
    URL(java.net.URL.class, "url"),
    UUID(java.util.UUID.class, "uuid"),
    DATE_TIME(Date.class, "dateTime");

    private final Type type;
    private final Set<String> names;

    private PrimitiveType(Type type, String name, String... aliases) {
        this.type = type;
        final Set<String> tmpNames = new HashSet<String>();
        tmpNames.add(name.toUpperCase());
        for (String item : aliases) {
            tmpNames.add(item.toUpperCase());
        }
        this.names = Collections.unmodifiableSet(tmpNames);
    }

    public static PrimitiveType fromName(String name) {
        if (name != null) {
            final String key = name.toUpperCase();
            for (PrimitiveType item : values()) {
                if (item.names.contains(key)) {
                    return item;
                }
            }
        }
        return null;
    }

    public Type getType() {
        return type;
    }
}
