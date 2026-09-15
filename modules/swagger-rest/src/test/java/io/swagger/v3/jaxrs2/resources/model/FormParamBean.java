package io.swagger.v3.jaxrs2.resources.model;

import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;

public class FormParamBean {

    @FormParam(value = "param1")
    private String param1;

    @FormParam(value = "param2")
    private String param2;

    @BeanParam
    private NestedBeanParam nestedParams;

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public NestedBeanParam getNestedParams() {
        return nestedParams;
    }

    public void setNestedParams(final NestedBeanParam nestedParams) {
        this.nestedParams = nestedParams;
    }

    private static class NestedBeanParam {
        @FormParam(value = "param3")
        private String param3;
        public String getParam3() {
            return param3;
        }

        public void setParam3(String param3) {
            this.param3 = param3;
        }
    }
}
