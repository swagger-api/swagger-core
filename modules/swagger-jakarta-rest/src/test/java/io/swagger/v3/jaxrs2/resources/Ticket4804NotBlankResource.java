package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.parameters.ValidatedParameter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("/test")
public class Ticket4804NotBlankResource {

    @POST
    @Path("/createcart")
    public void postCart(@Valid @ValidatedParameter(onCreate.class) Cart cart) {}

    @PUT
    @Path("/updatecart")
    public void putCart(@Valid @ValidatedParameter(onUpdate.class) Cart cart) {}
    
    @PUT
    @Path("/barcart")
    public void barCart(Cart cart) {}

    public static interface onCreate {}
    public static interface onUpdate {}

    public static class CartDetails {
        @NotBlank(groups = {onCreate.class, onUpdate.class})
        public String name;

        @NotEmpty
        public String[] description;
    }


    public static class Cart {
        @NotBlank(groups = {onCreate.class, onUpdate.class})
        public int[] pageSizes;

        @NotNull
        public CartDetails notNullcartDetails;
    }
}
