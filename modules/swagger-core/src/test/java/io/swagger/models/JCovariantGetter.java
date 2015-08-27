package io.swagger.models;

import io.swagger.annotations.ApiModelProperty;

public abstract class JCovariantGetter {
    @ApiModelProperty(position = 1)
    public Object getMyProperty() {
        return "42";
    }

    @ApiModelProperty(position = 2)
    public Object getMyOtherProperty() {
        return "42";
    }

    public static class Sub extends JCovariantGetter {
        @Override
        public Integer getMyProperty() {
            return 42;
        }

        @Override
        public Integer getMyOtherProperty() {
            return 42;
        }
    }
}
