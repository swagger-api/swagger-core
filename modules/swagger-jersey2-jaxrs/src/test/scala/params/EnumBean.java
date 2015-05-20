package params;

import javax.ws.rs.HeaderParam;

import models.TestEnum;

public class EnumBean {

  @HeaderParam("HeaderParam")
  private TestEnum value;

  public TestEnum getValue() {
    return value;
  }

  public void setValue(TestEnum value) {
    this.value = value;
  }
}
