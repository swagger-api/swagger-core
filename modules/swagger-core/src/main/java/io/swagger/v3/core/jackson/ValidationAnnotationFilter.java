package io.swagger.v3.core.jackson;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public interface ValidationAnnotationFilter {

    default boolean isNotEmptyAnnotationApplicable(NotEmpty notEmpty) {
        return annotationGroupMatches(notEmpty.groups());
    }

    default boolean isNotBlankAnnotationApplicable(NotBlank notBlank) {
        return annotationGroupMatches(notBlank.groups());
    }

    default boolean isNotNullAnnotationApplicable(NotNull notNull) {
        return annotationGroupMatches(notNull.groups());
    }

    default boolean isMaxAnnotationApplicable(Max max) {
        return annotationGroupMatches(max.groups());
    }

    default boolean isMinAnnotationApplicable(Min min) {
        return annotationGroupMatches(min.groups());
    }

    default boolean isDecimalMaxAnnotationApplicable(DecimalMax decimalMax) {
        return annotationGroupMatches(decimalMax.groups());
    }

    default boolean isDecimalMinAnnotationApplicable(DecimalMin decimalMin) {
        return annotationGroupMatches(decimalMin.groups());
    }

    default boolean isSizeAnnotationApplicable(Size size) {
        return annotationGroupMatches(size.groups());
    }

    default boolean isPatternAnnotationApplicable(Pattern pattern) {
        return annotationGroupMatches(pattern.groups());
    }

    boolean annotationGroupMatches(Class<?>[] annotationGroups);

    class DefaultValidationAnnotationFilter implements ValidationAnnotationFilter {

        @Override
        public boolean annotationGroupMatches(Class<?>[] annotationGroups) {
            return annotationGroups.length == 0;
        }
    }
}
