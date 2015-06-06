package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelWithJsonProperty {
    @JsonProperty("theCount")
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
