package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "/v1/users", tags = "annotatedInterface")
public class AnnotatedInterfaceImpl implements AnnotatedInterface {

    @Override
    @ApiOperation(value = "Load by userId")
    public String findById(String id) {
        return "";
    }
}
