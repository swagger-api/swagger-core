package io.swagger.v3.jaxrs2.resources.generics.ticket3694;

import jakarta.ws.rs.core.Response;
import java.util.List;

public interface Ticket3694ResourceInterfaceSimpleSameReturn<T> {

    Response bar(List<T> foo);

}
