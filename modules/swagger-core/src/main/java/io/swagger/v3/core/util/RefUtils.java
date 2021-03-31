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
