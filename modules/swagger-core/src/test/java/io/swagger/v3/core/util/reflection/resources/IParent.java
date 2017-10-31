package io.swagger.v3.core.util.reflection.resources;

import javax.ws.rs.Path;

@Path("parentInterfacePath")
@IndirectAnnotation
public interface IParent<T extends Number> extends IGrandparent<T> {

    public String parametrizedMethod2(T arg);

    @Override
    String parametrizedMethod5(T arg);

}
