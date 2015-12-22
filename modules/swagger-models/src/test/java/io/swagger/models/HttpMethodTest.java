package io.swagger.models;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.testng.annotations.Test;

public class HttpMethodTest {
  @Test
  public void testValues() {
	  assertEquals(HttpMethod.DELETE, HttpMethod.valueOf("DELETE"));
	  assertTrue(Arrays.asList(HttpMethod.values()).contains(HttpMethod.DELETE));
  }
}
