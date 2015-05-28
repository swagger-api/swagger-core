package com.wordnik.swagger.jackson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * Helper class used for converting well-known (property) types into
 * Swagger type names.
 */
public class TypeNameResolver {
  public final static TypeNameResolver std = new TypeNameResolver();

  /**
   * Not quite sure if it should be "dateTime" or "date-time";
   * spec and code seem to disagree
   */
  public final static String TYPE_DATE_TIME = "dateTime";

  /**
   * Not quite sure if it should be "date" or "Date";
   * spec and code seem to disagree
   */
  public final static String TYPE_DATE = "date";

  protected final static Map<Class<?>, String> JDK_TYPES = jdkTypes();

  /**
   * We also support handling of a small number of "well-known" types,
   * specifically for Joda lib.
   */
  protected final static Map<String, String> EXTERNAL_TYPES = externalTypes();
  
  protected TypeNameResolver() { }

  public String nameForType(JavaType type) {
    if (type.hasGenericTypes()) {
      return nameForGenericType(type);
    }
    final Class<?> raw = type.getRawClass();
    final String name = findStdName(raw);
    return (name == null) ? nameForClass(raw) : name;
  }


  protected String nameForClass(Class<?> cls) {
    final ApiModel model = cls.getAnnotation(ApiModel.class);
    final String modelName = model == null ? null : StringUtils.trimToNull(model.value());
    return modelName == null ? cls.getSimpleName() : modelName;
  }

  protected String nameForGenericType(JavaType type) {
    final StringBuilder generic = new StringBuilder(nameForClass(type.getRawClass()));
    final int count = type.containedTypeCount();
    for (int i = 0; i < count; ++i) {
      final JavaType arg = type.containedType(i);
      final Class<?> argClass = arg.getRawClass();
      final String argName = findStdName(argClass) != null ? nameForClass(argClass) : nameForType(arg);
      generic.append(WordUtils.capitalize(argName));
    }
    return generic.toString();
  }

  protected String findStdName(Class<?> raw) {
    String name = JDK_TYPES.get(raw);
    if (name == null) {
      name = EXTERNAL_TYPES.get(raw.getName());
      if (name == null) {
        // these are implemented by concrete types, so:
        if (Date.class.isAssignableFrom(raw)) {
          return TYPE_DATE_TIME;
        }
        if (Calendar.class.isAssignableFrom(raw)) {
          return TYPE_DATE_TIME;
        }
      }
      return name;
    }
    return name;
  }
  
  private static Map<Class<?>, String> jdkTypes() {
    Map<Class<?>, String> map = new HashMap<Class<?>, String>();
    _add(map, "boolean", Boolean.class, Boolean.TYPE);
    _add(map, "byte", Byte.class, Byte.TYPE);
    _add(map, "integer", Integer.class, Integer.TYPE,
        Short.class, Short.TYPE);
    _add(map, "long", Long.class, Long.TYPE,
        BigInteger.class);
    _add(map, "float", Float.class, Float.TYPE);
    _add(map, "double", Double.class, Double.TYPE,
        BigDecimal.class);
    _add(map, "string", String.class,
        Character.class, Character.TYPE);
    
    // Date, Calendar types are not exact matches (but sub-types), not added here

    _add(map, "string", UUID.class, URL.class, URI.class);

    return map;
  }

  private static Map<String, String> externalTypes() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("org.joda.time.DateTime", TYPE_DATE_TIME);
    map.put("org.joda.time.LocalDate", TYPE_DATE);
    map.put("org.joda.time.ReadableDateTime", TYPE_DATE_TIME);
    map.put("javax.xml.datatype.XMLGregorianCalendar", TYPE_DATE_TIME);
    return map;
  }

  private static Map<Class<?>, String> _add(Map<Class<?>, String> map, String name,
      Class<?>... types) {
    for (Class<?> type : types) {
      map.put(type, name);
    }
    return map;
  }
}
