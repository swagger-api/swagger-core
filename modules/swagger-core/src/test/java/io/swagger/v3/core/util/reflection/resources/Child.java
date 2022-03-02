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

package io.swagger.v3.core.util.reflection.resources;

import javax.inject.Inject;

public class Child extends Parent<Integer> implements IParent<Long> {

    protected Child() {

    }

    public Child(String arg) {

    }

    @Override
    public Integer parametrizedMethod1(Integer arg) {
        return null;
    }

    @Override
    public String parametrizedMethod2(Long arg) {
        return null;
    }

    public Integer parametrizedMethod3(Long arg) {
        return null;
    }

    public Integer parametrizedMethod4(Long arg) {
        return null;
    }

    @Override
    public String parametrizedMethod5(Long arg) {
        return null;
    }

    @Override
    public void annotationHolder() {

    }

    @Deprecated
    @Inject
    public void injectableMethod() {

    }

    @IndirectAnnotation
    public void indirectAnnotationMethod() {

    }

}
