package io.swagger.v3.jaxrs2.resources.rs;

public abstract class AbstractEntityRestService<DTO extends PersistentDTO> implements EntityRestService<DTO> {

    public DTO create(DTO object) throws Exception {
        return null;
    }

}

