package models;

import com.wordnik.swagger.annotations.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@ApiModel(value = "Foo")
public class Foo1 extends Bar1 {
  public Foo1(Baz1 baz) {
    super(baz);
  }

  @Override
  @JsonIgnore
  public BazField1 getBazField() {
    return super.getBazField();
  }

  public String getOne() {
    return null;
  }
  public void setOne(String one) {}
}