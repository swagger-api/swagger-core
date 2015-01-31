package com.wordnik.swagger.jaxrs.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Utilities for interacting with parameters.
 */
public class ParameterUtils {

  /**
   * Return whether or not the method argument is an array.
   *
   * @param paramClass the parameter type
   * @param paramGenericType the parameter generic type
   *
   * @return true if the parameter is an array
   */
  public static boolean isMethodArgumentAnArray(final Class<?> paramClass, final Type paramGenericType) {
    final Class<?>[] interfaces = paramClass.getInterfaces();
    boolean isArray = false;

    for (final Class<?> aCls : interfaces) {
      if (List.class.equals(aCls)) {
        isArray = true;

        break;
      }
    }

    if (paramGenericType instanceof ParameterizedType){
      final Type[] parameterArgTypes = ((ParameterizedType)paramGenericType).getActualTypeArguments();
      Class<?> testClass = paramClass;

      for (Type parameterArgType : parameterArgTypes){
        if (testClass.isAssignableFrom(List.class)){
          isArray = true;

          break;
        }

        testClass = (Class<?>) parameterArgType;
      }
    }

    return isArray;
  }
}
