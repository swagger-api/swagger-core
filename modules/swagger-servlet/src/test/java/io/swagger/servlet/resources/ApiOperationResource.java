package io.swagger.servlet.resources;

public class ApiOperationResource {

    @Uber(description = "Description")
    public String getResponse() {
        return "Response";
    }
}
