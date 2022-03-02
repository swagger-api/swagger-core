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

package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.Components;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class RefUtils {

    public static String constructRef(String simpleRef) {
        return Components.COMPONENTS_SCHEMAS_REF + simpleRef;
    }

    public static String constructRef(String simpleRef, String prefix) {
        return prefix + simpleRef;
    }

    public static Pair extractSimpleName(String ref) {
        int idx = ref.lastIndexOf('/');
        if (idx > 0) {
            String simple = ref.substring(idx + 1);
            if (!StringUtils.isBlank(simple)) {
                return new ImmutablePair<>(simple, ref.substring(0, idx + 1));
            }
        }
        return new ImmutablePair<>(ref, null);

    }
}
