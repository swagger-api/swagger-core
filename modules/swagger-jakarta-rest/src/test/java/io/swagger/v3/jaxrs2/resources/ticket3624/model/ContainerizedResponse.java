package io.swagger.v3.jaxrs2.resources.ticket3624.model;

import java.util.List;

public abstract class ContainerizedResponse {
    public abstract List<ModelContainer> getContainerizedModels();
    public abstract int getTotalCount();
}
