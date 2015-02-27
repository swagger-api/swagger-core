package com.wordnik.swagger.model.override;

import com.wordnik.swagger.annotations.ApiModelProperty;

import com.wordnik.swagger.converter.ModelConverters;

import com.wordnik.swagger.util.Json;

import junit.framework.Assert;
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

