package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class SiblingAnnotationFilter {

    public static class FilterResult {
        private final Annotation[] filteredAnnotations;
        private final Schema.SchemaResolution resolvedSchemaResolution;

        public FilterResult(Annotation[] filteredAnnotations, Schema.SchemaResolution resolvedSchemaResolution) {
            this.filteredAnnotations = filteredAnnotations;
            this.resolvedSchemaResolution = resolvedSchemaResolution;
        }

        public Annotation[] getFilteredAnnotations() {
            return filteredAnnotations;
        }

        public Schema.SchemaResolution getResolvedSchemaResolution() {
            return resolvedSchemaResolution;
        }
    }

    /**
     * Filter out Schema/ArraySchema annotations to prevent duplicate processing:
     * 1. They are already processed and merged by AnnotationsUtils.mergeSchemaAnnotations()
     * 2. Re-processing would cause annotation metadata to leak incorrectly between levels
     * 3. Without preserving ArraySchema, Stream is treated as generic object instead of iterable collection.
     *      * The ArraySchema is needed for the Stream to be recognized as an array, but it doesn't leak to items
     *      * because the container path filters it out.
     *
     * @param annotations the annotations to filter
     * @param propType the property type
     * @param ctxSchema the schema annotation
     * @param ctxArraySchema the array schema annotation
     * @param schemaResolution the schema resolution configuration
     * @param openapi31 whether OpenAPI 3.1 is enabled
     * @return FilterResult containing the filtered annotations and resolved schema resolution
     */
    public static FilterResult filterSiblingAnnotations(
            Annotation[] annotations,
            JavaType propType,
            io.swagger.v3.oas.annotations.media.Schema ctxSchema,
            io.swagger.v3.oas.annotations.media.ArraySchema ctxArraySchema,
            Schema.SchemaResolution schemaResolution,
            boolean openapi31) {
        
        Annotation[] ctxFilteredSiblingAnnotations = null;
        
        Schema.SchemaResolution resolvedSchemaResolution = AnnotationsUtils.resolveSchemaResolution(schemaResolution, ctxSchema);

        if (AnnotationsUtils.areSiblingsAllowed(resolvedSchemaResolution, openapi31)) {
            List<Annotation> filteredAnnotationsList = new ArrayList<>();
            
            if (annotations != null) {
                boolean isStreamWithArraySchema = isStreamType(propType) && ctxArraySchema != null;

                for (Annotation a : annotations) {
                    boolean isSchemaAnnotation = a instanceof io.swagger.v3.oas.annotations.media.Schema;
                    boolean isArraySchemaAnnotation = a instanceof io.swagger.v3.oas.annotations.media.ArraySchema;
                    boolean shouldIncludeAnnotation = (!isSchemaAnnotation && !isArraySchemaAnnotation) || isStreamWithArraySchema;
                    
                    if (shouldIncludeAnnotation) {
                        filteredAnnotationsList.add(a);
                    }
                }
                
                ctxFilteredSiblingAnnotations = filteredAnnotationsList.toArray(new Annotation[filteredAnnotationsList.size()]);
            }
        }
        
        return new FilterResult(ctxFilteredSiblingAnnotations, resolvedSchemaResolution);
    }

    private static boolean isStreamType(JavaType type) {
        return type != null && 
               type.getRawClass() != null && 
               java.util.stream.Stream.class.isAssignableFrom(type.getRawClass());
    }
}
