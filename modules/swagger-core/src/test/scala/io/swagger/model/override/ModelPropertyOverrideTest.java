package io.swagger.model.override;

import io.swagger.annotations.ApiModelProperty;

import io.swagger.converter.ModelConverters;

import io.swagger.util.Json;

import org.junit.Test;

public class ModelPropertyOverrideTest {
  @Test
  public void OverrideTest() throws Exception {
    ModelConverters.getInstance().addConverter(new SamplePropertyConverter());
    Json.prettyPrint(ModelConverters.getInstance().read(MyPojo.class));
  }

  public static class MyPojo {
    public String getId() {
      return "";
    }
    public void setId(String id) {}

    @ApiModelProperty(value = "instead of modeling this class in the documentation, we will model a string")
    public MyCustomClass getMyCustomClass() {
      return null;
    }
    public void setMyCustomClass(MyCustomClass myCustomClass) {}
  }
}

