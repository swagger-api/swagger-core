package models;

import com.wordnik.swagger.annotations.ApiModelProperty;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class ModelWithTuple2 {
  @ApiModelProperty(value = "Possible values for state property of timesheet or timesheet entry", required = true)
  public Pair<String, String> timesheetStates;

  @ApiModelProperty(value = "set of pairs", required = true)
  public Set<Pair<String, String>> manyPairs;

  @ApiModelProperty(value = "set of pairs wiht complex left", required = true)
  public Set<Pair<ComplexLeft, String>> complexLeft;

  static class ComplexLeft {
    public String name;
    public Integer age;
  }
}