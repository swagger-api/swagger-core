package io.swagger.v3.jaxrs2.resources.model;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class NestedBeanParam {

    @BeanParam
    private NestedClass nestedParams;

    public NestedClass getNestedParams() {
        return nestedParams;
    }

    public void setNestedParams(final NestedClass nestedParams) {
        this.nestedParams = nestedParams;
    }

    public class NestedClass {
        @QueryParam(value = "queryParam")
        @DefaultValue("10")
        private Integer queryParam = 10;
        public Integer getQueryParam() {
            return queryParam;
        }

        public void setQueryParam(final Integer queryParam) {
            this.queryParam = queryParam;
        }
    }
}
