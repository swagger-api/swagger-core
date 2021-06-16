package io.swagger.v3.plugin.annotator.model;

import java.lang.annotation.ElementType;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnotatorConfig {

    /**
     * what type to hit
     */
    private ElementType annotateType;

    /**
     * what annotations to tag
     */
    private Set<JavadocMapping> javadocMappings;
}
