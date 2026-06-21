package io.swagger.v3.jakartarest.resources;

import io.swagger.v3.oas.annotations.parameters.ValidatedParameter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("/test")
public class Ticket4804Resource {

    @POST
    @Path("/createcart")
    public void postCart(@Valid @ValidatedParameter(onCreate.class) Cart cart) {}

    @PUT
    @Path("/updatecart")
    public void putCart(@Valid @ValidatedParameter(onUpdate.class) Cart cart) {}

    @PUT
    @Path("/foocart")
    public void fooCart(@Valid @ValidatedParameter(onFoo.class) Cart cart) {}

    @PUT
    @Path("/barcart")
    public void barCart(Cart cart) {}

    public static interface onCreate {}
    public static interface onUpdate {}
    public static interface onFoo {}

    public static class CartDetails {
        @NotNull(groups = {onCreate.class})
        public String name;

        @NotNull
        public String description;
    }


    public static class Cart {
        @NotNull(groups = {onCreate.class})
        public int pageSize;
        @NotNull(groups = {onFoo.class})
        public CartDetails cartDetails;

        @NotNull
        public CartDetails notNullcartDetails;
    }
}
