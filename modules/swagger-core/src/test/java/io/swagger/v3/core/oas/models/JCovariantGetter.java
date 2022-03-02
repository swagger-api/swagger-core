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

import io.swagger.v3.oas.annotations.media.Schema;

public abstract class JCovariantGetter {
    @Schema
    public Object getMyProperty() {
        return "42";
    }

    @Schema
    public Object getMyOtherProperty() {
        return "42";
    }

    public static class Sub extends JCovariantGetter {
        @Override
        public Integer getMyProperty() {
            return 42;
        }

        @Override
        public Integer getMyOtherProperty() {
            return 42;
        }
    }
}
