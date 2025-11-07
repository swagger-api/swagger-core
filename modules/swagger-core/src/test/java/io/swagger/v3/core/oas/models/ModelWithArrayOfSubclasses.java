package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

public class ModelWithArrayOfSubclasses {
    
    @Schema(description = "The holder")
    public class Holder extends SubB {
    }
  
    @Schema(
            discriminatorProperty = "name"
            , discriminatorMapping = {
              @DiscriminatorMapping(schema = SubA.class, value = "a") 
              , @DiscriminatorMapping(schema = SubB.class, value = "b") 
            }
            , description = "Stuff"
    )
    public class Base {
      
        private String name;
      
        public String getName() {
            return name;
        }
    }

    @Schema(description = "The SubA class")
    public class SubA extends Base {
      
        private Long count;
      
        public Long getCount() {
            return count;
        }
    }

    @Schema(description = "The SubB class")
    public class SubB extends Base {
      
        private String friend;
        private Base[] baseArray;

        public String getFriend() {
            return friend;
        }

        @ArraySchema(
                schema = @Schema(implementation = Base.class)
                , arraySchema = @Schema(
                        type = "array"
                        , description = "Thingy"
                )
                , minItems = 0
                , uniqueItems = true
        )
        public Base[] getBaseArray() {
            return baseArray;
        }
    }
}
