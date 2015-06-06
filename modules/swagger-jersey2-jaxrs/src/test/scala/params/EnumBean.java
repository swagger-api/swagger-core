package params;

import models.TestEnum;

import javax.ws.rs.HeaderParam;

public class EnumBean {

    @HeaderParam("HeaderParam")
    private TestEnum value;

    public TestEnum getValue() {
        return value;
    }

    public void setValue(TestEnum value) {
        this.value = value;
    }
}
