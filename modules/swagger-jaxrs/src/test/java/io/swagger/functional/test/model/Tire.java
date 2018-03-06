package io.swagger.functional.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.functional.test.view.Detail;
import io.swagger.functional.test.view.Summary;

public class Tire {

  @JsonView(Summary.class)
  @JsonProperty("brand")
  private String made = "Michelin";

  @JsonView(Detail.class)
  @JsonProperty
  private String condition = "new";
}