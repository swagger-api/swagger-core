package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class Ticket5017IfThenElseResource {
    @GET
    public void myMethod(Example request) {}

    @Schema(_if = IfCondition.class, then = ThenSchema.class, _else = ElseSchema.class)
    public static class Example {
        private String myField;

        public String getMyField() {
            return myField;
        }

        public void setMyField(String myField) {
            this.myField = myField;
        }
    }

    public static class IfCondition {
        private String condition;

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }
    }

    public static class ThenSchema {
        private String thenValue;

        public String getThenValue() {
            return thenValue;
        }

        public void setThenValue(String thenValue) {
            this.thenValue = thenValue;
        }
    }

    public static class ElseSchema {
        private String elseValue;

        public String getElseValue() {
            return elseValue;
        }

        public void setElseValue(String elseValue) {
            this.elseValue = elseValue;
        }
    }
}
