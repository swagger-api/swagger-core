package io.swagger.jaxrs;

import io.swagger.models.parameters.AbstractSerializableParameter;

import java.util.List;

class ParameterEnumProcessor extends AbstractEnumProcessor<AbstractSerializableParameter> {

    @Override
    protected void setEnum(AbstractSerializableParameter container, List<String> items) {
        container.setEnum(items);
    }
}
