package models;

import com.google.common.base.Optional;

public class GuavaModel {
  private Optional<String> name;

  public void setName(Optional<String> name) {
    this.name = name;
  }
  public Optional<String> getName() {
    return name;
  }
}
