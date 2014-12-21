package models;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

public class ModelWithEnumProperty {
  private TestEnum e;

  public TestEnum getEnumValue() {
    return e;
  }

  public void setEnumValue(TestEnum e) {
    this.e = e;
  }
}
