package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        $id = "http://yourdomain.com/schemas/myschema.json",
        description = "this is model for testing OAS 3.1 resolving",
        $comment = "Random comment at schema level",
        types = {"object"}
)
public class ModelWithOAS31StuffMinimal {

    private Addr address;

    public Addr getAddress() {
        return address;
    }

    public void setAddress(Addr address) {
        this.address = address;
    }
    public static class Addr {
        public String foo;
    }
}
