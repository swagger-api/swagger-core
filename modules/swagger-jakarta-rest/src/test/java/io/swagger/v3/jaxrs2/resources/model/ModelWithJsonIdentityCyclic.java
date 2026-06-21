package io.swagger.v3.jaxrs2.resources.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;

public class ModelWithJsonIdentityCyclic {

    public Long id;

    public List<SourceDefinition> sourceDefinitions;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "name")
    public static class SourceDefinition {
        public String driver;
        public String name;

        @JsonIdentityReference(alwaysAsId=true)
        @JsonIdentityInfo(
                generator = ObjectIdGenerators.PropertyGenerator.class,
                property = "id")
        public ModelWithJsonIdentityCyclic model;
    }

}