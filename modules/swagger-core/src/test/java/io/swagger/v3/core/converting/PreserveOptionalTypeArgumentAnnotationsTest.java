package io.swagger.v3.core.converting;

import tools.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.validation.constraints.Size;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class PreserveOptionalTypeArgumentAnnotationsTest {

    @AfterMethod
    public void resetStaticToggles() {
        ModelResolver.preserveOptionalTypeArgumentAnnotations = false;
        ModelResolver.optionalTypeArgumentAnnotationsToPreserve = annotation ->
                annotation.annotationType().getName().startsWith("jakarta.validation.constraints");
    }

    @Test(description = "it should not keep Optional type-argument annotations in AnnotatedType by default")
    public void preserveOptionalTypeArgumentAnnotationsDisabledByDefault() {
        CapturingConverter capturingConverter = new CapturingConverter();
        ModelResolver modelResolver = new ModelResolver(new ObjectMapper());
        ModelConverterContextImpl context = new ModelConverterContextImpl(Arrays.asList(capturingConverter, modelResolver));

        context.resolve(new AnnotatedType(OptionalValidationModel.class));

        assertFalse(capturingConverter.sawSizeOnOptionalValue, "Optional generic annotations should be omitted by default");
        assertFalse(capturingConverter.sawCustomOptionalTagOnOptionalValue, "Custom Optional type-argument annotations should be omitted by default");
    }

    @Test(description = "it should keep Optional type-argument annotations in AnnotatedType when opted in globally")
    public void preserveOptionalTypeArgumentAnnotationsEnabledByGlobalToggle() {
        ModelResolver.preserveOptionalTypeArgumentAnnotations = true;

        CapturingConverter capturingConverter = new CapturingConverter();
        ModelResolver modelResolver = new ModelResolver(new ObjectMapper());
        ModelConverterContextImpl context = new ModelConverterContextImpl(Arrays.asList(capturingConverter, modelResolver));

        context.resolve(new AnnotatedType(OptionalValidationModel.class));

        assertTrue(capturingConverter.sawSizeOnOptionalValue, "Optional generic annotations should be kept when globally enabled");
        assertFalse(capturingConverter.sawCustomOptionalTagOnOptionalValue, "Custom Optional type-argument annotations should be omitted by default");
    }

    @Test(description = "it should keep custom Optional type-argument annotations when configured")
    public void preserveOptionalTypeArgumentCustomAnnotationsWhenConfigured() {
        ModelResolver.preserveOptionalTypeArgumentAnnotations = true;
        ModelResolver.optionalTypeArgumentAnnotationsToPreserve = annotation ->
                annotation.annotationType().getName().startsWith("jakarta.validation.constraints") ||
                        OptionalTag.class.equals(annotation.annotationType());

        CapturingConverter capturingConverter = new CapturingConverter();
        ModelResolver modelResolver = new ModelResolver(new ObjectMapper());
        ModelConverterContextImpl context = new ModelConverterContextImpl(Arrays.asList(capturingConverter, modelResolver));

        context.resolve(new AnnotatedType(OptionalValidationModel.class));

        assertTrue(capturingConverter.sawSizeOnOptionalValue, "Optional generic annotations should be kept when globally enabled");
        assertTrue(capturingConverter.sawCustomOptionalTagOnOptionalValue, "Custom Optional type-argument annotations should be kept when configured");
    }


    static class OptionalValidationModel {
        public Optional<@Size(min = 3) @OptionalTag String> value;
    }

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface OptionalTag {
    }

    static class CapturingConverter implements ModelConverter {

        boolean sawSizeOnOptionalValue;
        boolean sawCustomOptionalTagOnOptionalValue;

        @Override
        public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
            if ("value".equals(type.getPropertyName())) {
                Annotation[] ctxAnnotations = type.getCtxAnnotations();
                if (ctxAnnotations != null) {
                    sawSizeOnOptionalValue = Arrays.stream(ctxAnnotations)
                            .anyMatch(annotation -> Size.class.equals(annotation.annotationType()));
                    sawCustomOptionalTagOnOptionalValue = Arrays.stream(ctxAnnotations)
                            .anyMatch(annotation -> OptionalTag.class.equals(annotation.annotationType()));
                }
            }
            if (chain.hasNext()) {
                return chain.next().resolve(type, context, chain);
            }
            return null;
        }
    }
}