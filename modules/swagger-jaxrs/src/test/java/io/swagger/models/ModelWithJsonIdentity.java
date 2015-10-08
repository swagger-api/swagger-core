package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

public class ModelWithJsonIdentity {

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
    @JsonProperty("PropertyGeneratorAsId")
    public SourceDefinition1 testPropertyGeneratorAsId;

    @JsonIdentityReference(alwaysAsId = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
    @JsonProperty("PropertyGeneratorAsProperty")
    public SourceDefinition1 testPropertyGeneratorAsProperty;

    public class SourceDefinition1 {
        public String driver;
        public String name;
    }

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "driverId")
    @JsonProperty("ChangedPropertyName")
    public SourceDefinition2 testChangedPropertyName;

    public class SourceDefinition2 {
        @JsonProperty("driverId")
        public String driver;
        public String name;
    }

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class)
    @JsonProperty("SourceWithoutPropertyAsId")
    public SourceDefinition3 testWithoutPropertyAsId;

    @JsonIdentityReference(alwaysAsId = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class)
    @JsonProperty("SourceWithoutPropertyAsProperty")
    public SourceDefinition3 testWithoutPropertyAsProperty;

    public class SourceDefinition3 {
        @JsonProperty("driverId")
        public String driver;
        public String name;

        @JsonProperty("@id")
        public String id;
    }

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "testName1")
    @JsonProperty("IntSequenceGeneratorAsId")
    public SourceDefinition4 testIntSequenceGeneratorAsId;

    @JsonIdentityReference(alwaysAsId = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "testName2")
    @JsonProperty("IntSequenceGeneratorAsProperty")
    public SourceDefinition4 testIntSequenceGeneratorAsProperty;

    public class SourceDefinition4 {
        public String name;
    }

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    @JsonProperty("IntSequenceWithoutPropertyAsId")
    public SourceDefinition5 testIntSequenceWithoutPropertyAsId;

    @JsonIdentityReference(alwaysAsId = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    @JsonProperty("IntSequenceWithoutPropertyAsProperty")
    public SourceDefinition5 testIntSequenceWithoutPropertyAsProperty;

    public class SourceDefinition5 {
        public String name;
    }

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "UUID1")
    @JsonProperty("UUIDGeneratorAsId")
    public SourceDefinition6 testUUIDGeneratorAsId;

    @JsonIdentityReference(alwaysAsId = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "UUID2")
    @JsonProperty("UUIDGeneratorAsProperty")
    public SourceDefinition6 testUUIDGeneratorAsProperty;

    public class SourceDefinition6 {
        public String name;
    }

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
    @JsonProperty("UUIDGeneratorWithoutPropertyAsId")
    public SourceDefinition7 testUUIDGeneratorWithoutPropertyAsId;

    @JsonIdentityReference(alwaysAsId = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
    @JsonProperty("UUIDGeneratorWithoutPropertyAsProperty")
    public SourceDefinition7 testUUIDGeneratorWithoutPropertyAsProperty;

    public class SourceDefinition7 {
        public String name;
    }

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "testGeneratorsNone")
    @JsonProperty("GeneratorsNone")
    public SourceDefinition8 testGeneratorsNone;

    public class SourceDefinition8 {
        @JsonProperty("driverId")
        public String driver;
        public String name;
    }

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = CustomGenerator.class, property = "name")
    @JsonProperty("CustomGenerator")
    public SourceDefinition9 testCustomGenerator;

    public class SourceDefinition9 {
        public String driver;
        public String name;
    }

    @JsonIdentityInfo(generator = CustomGenerator.class, property = "name")
    @JsonProperty("WithoutJsonIdentityReference")
    public SourceDefinition10 testWithoutJsonIdentityReference;

    public class SourceDefinition10 {
        public String driver;
        public String name;
    }
}
