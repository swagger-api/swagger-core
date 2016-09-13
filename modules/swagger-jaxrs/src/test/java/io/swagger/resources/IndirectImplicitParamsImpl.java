package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "/v1/users", tags = "annotatedInterface")
public class IndirectImplicitParamsImpl implements IndirectImplicitParams {

    @Override
    @ApiOperation(value = "create user")
    public void createUser() {

    }

    @Override
    @ApiOperation(value = "Load by userId")
    public String findById(String id) {
        return "";
    }

}
