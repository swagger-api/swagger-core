package io.swagger.models.parameters;

public class PathParameter extends AbstractSerializableParameter<PathParameter> {

    public PathParameter() {
        super.setIn("path");
        super.setRequired(true);
    }
}
