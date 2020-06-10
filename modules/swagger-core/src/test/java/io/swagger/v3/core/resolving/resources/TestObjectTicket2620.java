package io.swagger.v3.core.resolving.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TestObjectTicket2620", description = "TestObject description", oneOf = {
        TestObjectTicket2620.ChildTestObject.class,
        TestObjectTicket2620.Child2TestObject.class
})
public class TestObjectTicket2620 {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    class ChildTestObject extends TestObjectTicket2620{
        private String childName;

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }
    }

    class Child2TestObject extends TestObjectTicket2620{
        private String childName;

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }
    }
}


