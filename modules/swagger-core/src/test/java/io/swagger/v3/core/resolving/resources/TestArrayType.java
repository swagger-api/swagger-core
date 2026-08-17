package io.swagger.v3.core.resolving.resources;

import io.swagger.v3.oas.annotations.media.ArraySchema;

import java.util.ArrayList;
import java.util.List;

public class TestArrayType {

    private Integer id;

    @ArraySchema(maxItems = 10)
    private List<String> names;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
