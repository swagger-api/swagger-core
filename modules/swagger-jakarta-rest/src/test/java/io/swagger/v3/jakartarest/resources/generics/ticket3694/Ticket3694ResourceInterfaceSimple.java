package io.swagger.v3.jakartarest.resources.generics.ticket3694;

import java.util.List;

public interface Ticket3694ResourceInterfaceSimple<T> {

    T bar(List<T> foo);

}
