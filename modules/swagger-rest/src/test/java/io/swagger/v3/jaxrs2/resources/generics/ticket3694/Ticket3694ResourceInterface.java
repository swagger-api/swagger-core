package io.swagger.v3.jaxrs2.resources.generics.ticket3694;

import javax.ws.rs.core.Response;
import java.util.List;

public interface Ticket3694ResourceInterface<T> {

    Response foo(List<T> foo);

    T bar(List<T> foo);

    Response another(T foo);

}
