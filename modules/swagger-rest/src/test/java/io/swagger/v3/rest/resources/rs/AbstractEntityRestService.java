package io.swagger.v3.rest.resources.rs;

public abstract class AbstractEntityRestService<DTO extends PersistentDTO> implements EntityRestService<DTO> {

    public DTO create(DTO object) throws Exception {
        return null;
    }

}

