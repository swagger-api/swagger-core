package io.swagger.models.deserializers.model;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import io.swagger.models.ArrayModel;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.deserializers.CustomAsTypeDeserializer;

/**
 * @author <a href="mailto:dilip.gundu@wavemaker.com">Dilip Kumar</a>
 * @since 27/3/15
 */
public class ModelTypeDeserializer extends CustomAsTypeDeserializer {
    public ModelTypeDeserializer(
            final JavaType bt, final TypeIdResolver idRes,
            final String typePropertyName, final boolean typeIdVisible, final Class<?> defaultImpl) {
        super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    public ModelTypeDeserializer(final ModelTypeDeserializer src, final BeanProperty property) {
        super(src, property);
    }

    @Override
    protected Class<?> findSubType(final JsonNode node) {
        Class<? extends Model> subType;

        if (node.get("$ref") != null) {
            subType = RefModel.class;
        } else if (node.get("allOf") != null) {
            subType = ComposedModel.class;
        } else {
            subType = ModelImpl.class; // default
            if (node.get("type") != null) {
                String type = node.get("type").asText();
                if (type != null && !type.equals(NULL)) {
                    if (type.equals("array")) {
                        subType = ArrayModel.class;
                    }
                }
            }
        }

        return subType;
    }

    @Override
    protected TypeDeserializer newInstance(final BeanProperty property) {
        return new ModelTypeDeserializer(this, property);
    }

}
