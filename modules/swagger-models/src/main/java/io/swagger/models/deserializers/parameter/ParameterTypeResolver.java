package io.swagger.models.deserializers.parameter;

import java.util.Collection;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

/**
 * @author <a href="mailto:dilip.gundu@wavemaker.com">Dilip Kumar</a>
 * @since 12/6/15
 */
public class ParameterTypeResolver extends StdTypeResolverBuilder {

    @Override
    public TypeDeserializer buildTypeDeserializer(
            final DeserializationConfig config, final JavaType baseType, final Collection<NamedType> subtypes) {
        return new ParameterTypeDeserializer(baseType, null,
                _typeProperty, _typeIdVisible, _defaultImpl);
    }
}
