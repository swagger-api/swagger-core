package io.swagger.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.jackson.mixin.IgnoreOriginalRefMixin;
import io.swagger.jackson.mixin.OriginalRefMixin;
import io.swagger.models.RefModel;
import io.swagger.models.RefPath;
import io.swagger.models.RefResponse;
import io.swagger.models.parameters.RefParameter;
import io.swagger.models.properties.RefProperty;

/**
 * @since 1.5.21
 */
public abstract class ReferenceSerializationConfigurer {

    private static void serializeAs(Class<?> cls, ObjectMapper mapper) {
        mapper.addMixIn(RefModel.class, cls);
        mapper.addMixIn(RefProperty.class, cls);
        mapper.addMixIn(RefPath.class, cls);
        mapper.addMixIn(RefParameter.class, cls);
        mapper.addMixIn(RefResponse.class, cls);
    }

    public static void serializeAsOriginalRef() {
        serializeAs(OriginalRefMixin.class, Json.mapper());
        serializeAs(OriginalRefMixin.class, Yaml.mapper());
    }

    public static void serializeAsComputedRef() {
        serializeAs(IgnoreOriginalRefMixin.class, Json.mapper());
        serializeAs(IgnoreOriginalRefMixin.class, Yaml.mapper());
    }

    public static void serializeAsOriginalRef(ObjectMapper mapper) {
        serializeAs(OriginalRefMixin.class, mapper);
    }

    public static void serializeAsComputedRef(ObjectMapper mapper) {
        serializeAs(IgnoreOriginalRefMixin.class, mapper);
    }

}
