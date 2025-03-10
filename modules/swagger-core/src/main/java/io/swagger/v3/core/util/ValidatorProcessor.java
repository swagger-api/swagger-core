package io.swagger.v3.core.util;

import io.swagger.v3.oas.models.media.Schema;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public interface ValidatorProcessor {
    enum MODE {
        BEFORE,
        REPLACE,
        AFTER
    }

    public default MODE getMode() {
        return MODE.REPLACE;
    }
    public default boolean applyBeanValidatorAnnotations(Schema property, Annotation[] annotations, Schema parent, boolean applyNotNullAnnotations) {
        return false;
    }

    public default Set<Class> resolveInvocationGroups(Map<String, Annotation> annos) {
        return null;
    }

    public default Set<Annotation> resolveInvocationAnnotations(Annotation[] annotations) {
        return null;
    }

}
