package io.swagger.v3.jaxrs2;

import io.swagger.v3.oas.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;

public class ResolvedParameter {
    public List<Parameter> parameters = new ArrayList<>();
    public Parameter requestBody;
    public Parameter formParameter;
}
