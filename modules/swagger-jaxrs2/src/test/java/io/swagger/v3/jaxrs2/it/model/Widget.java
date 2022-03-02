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

package io.swagger.v3.jaxrs2.it.model;

public class Widget {

    private String a;
    private String b;
    private String id;

    public String getA() {
        return a;
    }

    public Widget setA(String a) {
        this.a = a;
        return this;
    }

    public String getB() {
        return b;
    }

    public Widget setB(String b) {
        this.b = b;
        return this;
    }

    public String getId() {
        return id;
    }

    public Widget setId(String id) {
        this.id = id;
        return this;
    }
}
