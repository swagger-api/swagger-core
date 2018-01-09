package io.swagger.models;

import io.swagger.annotations.ApiParam;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;

public class TestBeanParam {

    @HeaderParam("test order annotation 1")
    @DefaultValue("1")
    private Integer order1;

    @DefaultValue("2")
    @HeaderParam("test order annotation 2")
    private Integer order2;

    @QueryParam("priority1")
    @DefaultValue("default")
    @ApiParam(name = "test priority 1", defaultValue = "overridden")
    private Integer priority1;

    @QueryParam("priority2")
    @ApiParam(name = "test priority 2", defaultValue = "4")
    @DefaultValue("3")
    private Integer priority2;

    public Integer getOrder1() {
        return order1;
    }

    public void setOrder1(Integer order1) {
        this.order1 = order1;
    }

    public Integer getOrder2() {
        return order2;
    }

    public void setOrder2(Integer order2) {
        this.order2 = order2;
    }

    public Integer getPriority1() {
        return priority1;
    }

    public void setPriority1(Integer priority1) {
        this.priority1 = priority1;
    }

    public Integer getPriority2() {
        return priority2;
    }

    public void setPriority2(Integer priority2) {
        this.priority2 = priority2;
    }
}
