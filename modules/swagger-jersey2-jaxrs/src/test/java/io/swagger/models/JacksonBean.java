package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;

public class JacksonBean {

    private String id;
    private String ignored;
    private StringValueBean bean;
    private NotFoundModel model;
    private NotFoundModel model2;

    @JsonIgnore
    public String getIgnored() {
        return ignored;
    }

    public void setIgnored(String ignored) {
        this.ignored = ignored;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setModel(NotFoundModel model) {
        this.model = model;
    }

    public StringValueBean getBean() {
        return bean;
    }

    public void setBean(StringValueBean bean) {
        this.bean = bean;
    }

    @JsonProperty("identity")
    public String getId() {
        return id;
    }

    @JsonUnwrapped
    public NotFoundModel getModel() {
        return model;
    }

    @JsonUnwrapped(prefix = "pre", suffix = "suf")
    public NotFoundModel getModel2() {
        return model2;
    }

    public void setModel2(NotFoundModel model2) {
        this.model2 = model2;
    }

    public static class StringValueBean {

        private final String value;

        @JsonCreator
        public StringValueBean(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

}
