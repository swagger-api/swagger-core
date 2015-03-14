package models;

import javax.validation.constraints.NotNull;

public class ModelWithBeanValidations {
  @NotNull
  protected Long id;

  @NotNull
  public Long getId() {
    return id;
  }
  @NotNull
  public void setId(Long id) {
    this.id = id;
  }
}