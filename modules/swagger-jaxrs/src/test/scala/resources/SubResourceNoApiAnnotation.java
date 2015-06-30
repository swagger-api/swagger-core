package resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import models.Employee;

public class SubResourceNoApiAnnotation {
    @GET
    public void getAllEmployees() {
        return;
    }

    @GET
    @Path("{id}")
    public Employee getSubresourceOperation(@PathParam("id") Long userId) {
        return null;
    }
}
