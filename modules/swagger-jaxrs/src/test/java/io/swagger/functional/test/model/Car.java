package io.swagger.functional.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.functional.test.view.Detail;
import io.swagger.functional.test.view.Sale;
import io.swagger.functional.test.view.Summary;
import java.util.Arrays;
import java.util.List;

public class Car {

  @JsonView(Summary.class)
  @JsonProperty("manufacture")
  private String made = "Honda";

  @JsonView({Summary.class, Detail.class})
  private String model = "Accord Hybrid";

  @JsonView({Detail.class})
  @JsonProperty
  private List<Tire> tires = Arrays.asList(new Tire());

  @JsonView({Sale.class})
  @JsonProperty
  private int price = 40000;

  // always in
  private String color = "White";

  public String getColor() {
    return color;
  }
}