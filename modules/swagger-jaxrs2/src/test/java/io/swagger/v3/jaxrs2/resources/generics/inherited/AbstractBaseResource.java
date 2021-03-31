package io.swagger.v3.jaxrs2.resources.generics.inherited;

import io.swagger.v3.jaxrs2.resources.model.MultipleBaseBean;

import java.util.List;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * abstract generic resource
 */
public abstract class AbstractBaseResource<T extends MultipleBaseBean> implements IAbstractGenericBase {
    @ApiGenericOperationAnnotation
    @GET
    @Path("/path")
    public List<T> index() {
        return new ArrayList<>();
    }
}