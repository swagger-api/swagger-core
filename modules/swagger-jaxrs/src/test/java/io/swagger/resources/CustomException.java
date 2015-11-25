package io.swagger.resources;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.NotFoundModel;

@ApiResponses({
        @ApiResponse(code = 400, message = "Invalid ID", response = NotFoundModel.class),
        @ApiResponse(code = 404, message = "object not found")})
public class CustomException extends RuntimeException {

}
