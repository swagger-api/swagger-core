package models;

import java.util.Set;

public class ModelWithEnumArray {
  public enum Action {
    CREATE, UPDATE, DELETE, COPY;
  }
  private Set<Action> actions;

  public Set<Action> getActions() {
    return actions;
  }
}