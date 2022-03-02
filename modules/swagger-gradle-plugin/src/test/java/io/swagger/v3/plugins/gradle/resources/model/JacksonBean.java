/**
 * Copyright 2021 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.v3.plugins.gradle.resources.model;

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
