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

package io.swagger.v3.core.oas.models;

public class Model1155 {
    private boolean valid;
    private String value;
    public boolean is;
    public String get;
    public boolean isA;
    public String getA;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // jackson treats this as getter
    public boolean is_persistent() {
        return true;
    }

    // jackson treats this as getter
    public String gettersAndHaters() {
        return null;
    }

    // jackson doesn't treat this as getter
    boolean isometric() {
        return true;
    }
}
