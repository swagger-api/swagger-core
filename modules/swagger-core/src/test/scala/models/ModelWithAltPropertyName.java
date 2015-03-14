package models;

import com.wordnik.swagger.annotations.*;

@ApiModel("sample_model")
public class ModelWithAltPropertyName {
  protected int id;

  @ApiModelProperty(
    access = "public",
    name = "the_id",
    notes = "Note, this is server generated.",
    value = "Read-only")
  public int getId() {
    return id;
  }

  public void setId( int id ) {
    this.id = id;
  }
}