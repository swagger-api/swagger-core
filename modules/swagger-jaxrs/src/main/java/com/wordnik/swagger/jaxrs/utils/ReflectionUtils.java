package com.wordnik.swagger.jaxrs.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ReflectionUtils {

  /**
   * Checks if the method methodToFind is the overridden method from the superclass.
   *
   * @param methodToFind is method to check
   * @param cls          is method class
   * @return true if the method is overridden method
   */
  public static boolean isOverriddenMethod(Method methodToFind, Class<?> cls) {
    Class<?> superClass = cls.getSuperclass();
    if (superClass != null && !(superClass.getClass().equals(Object.class))) {
      for (Method method : superClass.getMethods()) {
        if (!method.getName().equals(methodToFind.getName()) || !method.getReturnType().isAssignableFrom(methodToFind.getReturnType())) {
          continue;
        }
        if (Arrays.equals(method.getParameterTypes(), methodToFind.getParameterTypes()) &&
            !Arrays.equals(method.getGenericParameterTypes(), methodToFind.getGenericParameterTypes())) {
          return true;
        }
      }
      return isOverriddenMethod(methodToFind, superClass);
    }
    return false;
  }
  
  /**
   * Returns overridden method from superclass if it exists. If method was not found returns null.
   *
   * @param method is method to find
   * @return overridden method from superclass
   */
  public static Method getOverriddenMethod(Method method) {
    Class<?> declaringClass = method.getDeclaringClass();
    Class<?> superClass = declaringClass.getSuperclass();
    if (superClass != null && !(superClass.getClass().equals(Object.class))) {
      Method result = findMethod(method, superClass);
      if (result == null) {
        for (Class<?> anInterface : declaringClass.getInterfaces()) {
          result = findMethod(method, anInterface);
          if (result != null) {
            return result;
          }
        }
      } else {
        return result;
      }
    }
    return null;
  }

  /**
   * Searches the method methodToFind in given class cls. If the method is found returns it, else return null.
   *
   * @param methodToFind is the method to search
   * @param cls          is the class or interface where to search
   * @return method if it is found
   */
  public static Method findMethod(Method methodToFind, Class<?> cls) {
    String methodToSearch = methodToFind.getName();  
    Class<?>[] pTypes = methodToFind.getParameterTypes();
    Type[] gpTypes = methodToFind.getGenericParameterTypes();
    methodLoop:
    for (Method method : cls.getMethods()) {
      if (!method.getName().equals(methodToSearch) || !method.getReturnType().isAssignableFrom(methodToFind.getReturnType())) {
        continue;
      }
      Class<?>[] pt = method.getParameterTypes();
      Type[] gpt = method.getGenericParameterTypes();
      for (int j = 0; j < pTypes.length; j++) {
        Class<?> parameterType = pTypes[j];
        if (!(pt[j].equals(parameterType) || (!gpt[j].equals(gpTypes[j]) && pt[j].isAssignableFrom(parameterType)))) {
          continue methodLoop;
        }
      }
      return method;
    }
    return null;
  }
}
