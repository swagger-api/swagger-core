package io.swagger.sample;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

public class SwaggerSampleConfiguration extends Configuration {
  @NotEmpty
  @JsonProperty
  private String defaultName = "swagger-sample";

  public String getDefaultName() {
    return defaultName;
  }
}
