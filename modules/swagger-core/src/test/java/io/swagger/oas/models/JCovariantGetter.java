package io.swagger.oas.models;

import io.swagger.annotations.media.OASSchema;

public abstract class JCovariantGetter {
    @OASSchema//(position = 1)
    public Object getMyProperty() {
        return "42";
    }

    @OASSchema//(position = 2)
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
