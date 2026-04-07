package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

public class ModelWithManySubtypesAndRecursion {
    @Schema(description = "The holder")
    public class Holder extends SubA {
    }
    @Schema(
            discriminatorProperty = "name",
            discriminatorMapping = {
                    @DiscriminatorMapping(schema = SubA.class, value = "a"),
                    @DiscriminatorMapping(schema = SubB.class, value = "b"),
                    @DiscriminatorMapping(schema = SubC.class, value = "c"),
                    @DiscriminatorMapping(schema = SubD.class, value = "d"),
                    @DiscriminatorMapping(schema = SubE.class, value = "e"),
                    @DiscriminatorMapping(schema = SubF.class, value = "f"),
                    @DiscriminatorMapping(schema = SubG.class, value = "g"),
                    @DiscriminatorMapping(schema = SubH.class, value = "h"),
                    @DiscriminatorMapping(schema = SubI.class, value = "i"),
                    @DiscriminatorMapping(schema = SubJ.class, value = "j"),
            },
            description = "Stuff"
    )
    public interface Base {

    }

    @Schema(description = "The SubA class")
    public class SubA implements Base {
        private ModelWithManySubtypesAndRecursion.Base[] baseArray;

        @ArraySchema(
                schema = @Schema(implementation = ModelWithManySubtypesAndRecursion.Base.class),
                arraySchema = @Schema(
                        type = "array",
                        description = "Thingy"
                ),
                minItems = 0,
                uniqueItems = true
        )
        public ModelWithManySubtypesAndRecursion.Base[] getBaseArray() {
            return baseArray;
        }
    }

    @Schema(description = "The SubB class")
    public class SubB implements Base {
        private ModelWithManySubtypesAndRecursion.Base[] baseArray;

        @ArraySchema(
                schema = @Schema(implementation = ModelWithManySubtypesAndRecursion.Base.class),
                arraySchema = @Schema(
                        type = "array",
                        description = "Thingy"
                ),
                minItems = 0,
                uniqueItems = true
        )
        public ModelWithManySubtypesAndRecursion.Base[] getBaseArray() {
            return baseArray;
        }
    }

    @Schema(description = "The SubC class")
    public class SubC implements Base {
        private ModelWithManySubtypesAndRecursion.Base[] baseArray;

        @ArraySchema(
                schema = @Schema(implementation = ModelWithManySubtypesAndRecursion.Base.class),
                arraySchema = @Schema(
                        type = "array",
                        description = "Thingy"
                ),
                minItems = 0,
                uniqueItems = true
        )
        public ModelWithManySubtypesAndRecursion.Base[] getBaseArray() {
            return baseArray;
        }
    }

    @Schema(description = "The SubD class")
    public class SubD implements Base {
        private ModelWithManySubtypesAndRecursion.Base[] baseArray;

        @ArraySchema(
                schema = @Schema(implementation = ModelWithManySubtypesAndRecursion.Base.class),
                arraySchema = @Schema(
                        type = "array",
                        description = "Thingy"
                ),
                minItems = 0,
                uniqueItems = true
        )
        public ModelWithManySubtypesAndRecursion.Base[] getBaseArray() {
            return baseArray;
        }
    }

    @Schema(description = "The SubE class")
    public class SubE implements Base {
        private ModelWithManySubtypesAndRecursion.Base[] baseArray;

        @ArraySchema(
                schema = @Schema(implementation = ModelWithManySubtypesAndRecursion.Base.class),
                arraySchema = @Schema(
                        type = "array",
                        description = "Thingy"
                ),
                minItems = 0,
                uniqueItems = true
        )
        public ModelWithManySubtypesAndRecursion.Base[] getBaseArray() {
            return baseArray;
        }
    }

    @Schema(description = "The SubF class")
    public class SubF implements Base {
        private ModelWithManySubtypesAndRecursion.Base[] baseArray;

        @ArraySchema(
                schema = @Schema(implementation = ModelWithManySubtypesAndRecursion.Base.class),
                arraySchema = @Schema(
                        type = "array",
                        description = "Thingy"
                ),
                minItems = 0,
                uniqueItems = true
        )
        public ModelWithManySubtypesAndRecursion.Base[] getBaseArray() {
            return baseArray;
        }
    }

    @Schema(description = "The SubG class")
    public class SubG implements Base {
        private ModelWithManySubtypesAndRecursion.Base[] baseArray;

        @ArraySchema(
                schema = @Schema(implementation = ModelWithManySubtypesAndRecursion.Base.class),
                arraySchema = @Schema(
                        type = "array",
                        description = "Thingy"
                ),
                minItems = 0,
                uniqueItems = true
        )
        public ModelWithManySubtypesAndRecursion.Base[] getBaseArray() {
            return baseArray;
        }
    }

    @Schema(description = "The SubH class")
    public class SubH implements Base {
        private ModelWithManySubtypesAndRecursion.Base[] baseArray;

        @ArraySchema(
                schema = @Schema(implementation = ModelWithManySubtypesAndRecursion.Base.class),
                arraySchema = @Schema(
                        type = "array",
                        description = "Thingy"
                ),
                minItems = 0,
                uniqueItems = true
        )
        public ModelWithManySubtypesAndRecursion.Base[] getBaseArray() {
            return baseArray;
        }
    }

    @Schema(description = "The SubI class")
    public class SubI implements Base {
        private ModelWithManySubtypesAndRecursion.Base[] baseArray;

        @ArraySchema(
                schema = @Schema(implementation = ModelWithManySubtypesAndRecursion.Base.class),
                arraySchema = @Schema(
                        type = "array",
                        description = "Thingy"
                ),
                minItems = 0,
                uniqueItems = true
        )
        public ModelWithManySubtypesAndRecursion.Base[] getBaseArray() {
            return baseArray;
        }
    }

    @Schema(description = "The SubJ class")
    public class SubJ implements Base {
        private ModelWithManySubtypesAndRecursion.Base[] baseArray;

        @ArraySchema(
                schema = @Schema(implementation = ModelWithManySubtypesAndRecursion.Base.class),
                arraySchema = @Schema(
                        type = "array",
                        description = "Thingy"
                ),
                minItems = 0,
                uniqueItems = true
        )
        public ModelWithManySubtypesAndRecursion.Base[] getBaseArray() {
            return baseArray;
        }
    }
}
