package com.wordnik.swagger.sample.service;

public class TokenScope {
  public static final ThreadLocal userThreadLocal = new ThreadLocal();

  public static void setUserId(Long userId) {
    userThreadLocal.set(userId);
  }

  public static Long getUserId() {
    return (Long)userThreadLocal.get();
  }

  public static void unsetUserId() {
    userThreadLocal.remove();
  }
}
