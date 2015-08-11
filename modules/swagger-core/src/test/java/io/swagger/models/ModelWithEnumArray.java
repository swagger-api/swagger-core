package io.swagger.models;

import java.util.Set;

public class ModelWithEnumArray {
    private Set<Action> actions;

    public Set<Action> getActions() {
        return actions;
    }

    public enum Action {
        CREATE, UPDATE, DELETE, COPY;
    }
}