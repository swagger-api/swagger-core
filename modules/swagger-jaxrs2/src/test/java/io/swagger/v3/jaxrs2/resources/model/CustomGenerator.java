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

package io.swagger.v3.jaxrs2.resources.model;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

public class CustomGenerator extends ObjectIdGenerators.PropertyGenerator {
    private static final long serialVersionUID = 1L;

    protected CustomGenerator(Class<?> scope) {
        super(scope);
    }

    @Override
    public ObjectIdGenerator<Object> forScope(Class<?> scope) {
        return null;
    }

    @Override
    public ObjectIdGenerator<Object> newForSerialization(Object context) {
        return null;
    }

    @Override
    public IdKey key(Object key) {
        return null;
    }

    @Override
    public Object generateId(Object forPojo) {
        return null;
    }
}