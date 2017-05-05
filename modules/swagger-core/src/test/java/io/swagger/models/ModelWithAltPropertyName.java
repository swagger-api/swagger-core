package io.swagger.models;

import io.swagger.annotations.media.OASSchema;

@OASSchema(title = "sample_model")
public class ModelWithAltPropertyName {
    protected int id;

    @OASSchema(
//            access = "public",
            name = "the_id",
            description = "Note, this is server generated.",
            title = "Read-only")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}