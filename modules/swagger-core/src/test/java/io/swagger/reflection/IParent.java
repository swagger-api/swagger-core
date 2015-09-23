package io.swagger.reflection;

import javax.ws.rs.Path;

@Path("parentInterfacePath")
public interface IParent<T extends Number> {

    public String parametrizedMethod2(T arg);
}
