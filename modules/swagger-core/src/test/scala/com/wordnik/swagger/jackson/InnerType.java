package com.fasterxml.jackson.module.swagger;

import com.wordnik.swagger.jackson.*;

public class InnerType {
  public int foo;
  public String name;

  public String getName() {return name;}
  public void setName(String name) {this.name = name;}
}