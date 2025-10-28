package io.swagger.v3.core.converting;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import org.testng.annotations.Test;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertEquals;

public class PolymorphicSubtypePropertyBleedTest {

    @Schema(
            description = "base",
            discriminatorProperty = "kind",
            oneOf = { ConcretionA.class }
    )
    public static class BaseResponse {
        public String sharedField;
        public String kind;
    }

    @Schema(description = "subtype A")
    public static class ConcretionA extends BaseResponse {
        public String onlyInA;
    }

    public static class Wrapper {
        @NotNull
        public ConcretionA concretionA;
    }

    /**
     * Mimics how swagger-core resolves a bean property:
     *  - schemaProperty=true
     *  - propertyName set
     *  - ctxAnnotations from the backing field (e.g. @NotNull)
     */
    private io.swagger.v3.oas.models.media.Schema resolveAsBeanProperty(
            Class<?> clazz,
            String propertyName,
            Field backingField,
            ModelConverterContextImpl ctx,
            ModelResolver resolver
    ) {
        AnnotatedType at = new AnnotatedType()
                .type(clazz)
                .schemaProperty(true)
                .propertyName(propertyName)
                .ctxAnnotations(backingField.getAnnotations());
        return ctx.resolve(at);
    }

    /**
     * Mimics how resolveSubtypes(...) resolves a polymorphic subtype:
     *  - NO schemaProperty(true)
     *  - NO propertyName
     *  - NO ctxAnnotations from some containing field

     */
    private io.swagger.v3.oas.models.media.Schema resolveAsPolymorphicSubtype(
            Class<?> clazz,
            ModelConverterContextImpl ctx,
            ModelResolver resolver
    ) {
        AnnotatedType at = new AnnotatedType()
                .type(clazz);
        return ctx.resolve(at);
    }

    /**
     * Baseline for comparison: new context, clean standalone schema resolution.
     */
    private io.swagger.v3.oas.models.media.Schema resolveAsStandaloneSchema(
            Class<?> clazz,
            ModelResolver resolver
    ) {
        ModelConverterContextImpl freshCtx = new ModelConverterContextImpl(resolver);
        AnnotatedType at = new AnnotatedType()
                .type(clazz);
        return freshCtx.resolve(at);
    }

    @Test
    public void subtypeResolution_shouldMatchStandalone_andNotBleedFromPropertyContext() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ModelResolver resolver = new ModelResolver(mapper);

        ModelConverterContextImpl sharedCtx = new ModelConverterContextImpl(resolver);

        // 1. Resolve ConcretionA in the bean property context
        Field wrapperField = Wrapper.class.getDeclaredField("concretionA");
        io.swagger.v3.oas.models.media.Schema concretionAsPropertySchema =
                resolveAsBeanProperty(
                        ConcretionA.class,
                        "concretionA",
                        wrapperField,
                        sharedCtx,
                        resolver
                );

        assertNotNull(concretionAsPropertySchema, "bean property resolution returned null");

        // 2. Resolve ConcretionA again, but this time as a polymorphic subtype using the SAME sharedCtx
        io.swagger.v3.oas.models.media.Schema concretionAsPolymorphicSchema =
                resolveAsPolymorphicSubtype(
                        ConcretionA.class,
                        sharedCtx,
                        resolver
                );
        assertNotNull(concretionAsPolymorphicSchema, "polymorphic subtype resolution returned null");

        // 3. Resolve ConcretionA in a clean context to represent the canonical standalone subtype schema.
        io.swagger.v3.oas.models.media.Schema concretionStandaloneSchema =
                resolveAsStandaloneSchema(
                        ConcretionA.class,
                        resolver
                );
        assertNotNull(concretionStandaloneSchema, "standalone subtype resolution returned null");

        // nullable should be consistent between standalone and subtype
        assertEquals(
                concretionStandaloneSchema.getNullable(),
                concretionAsPolymorphicSchema.getNullable()
        );

        // required list should match between standalone and subtype-in-sharedCtx
        List<?> requiredStandalone = concretionStandaloneSchema.getRequired();
        List<?> requiredPolymorphic = concretionAsPolymorphicSchema.getRequired();
        if (requiredStandalone == null) requiredStandalone = java.util.Collections.emptyList();
        if (requiredPolymorphic == null) requiredPolymorphic = java.util.Collections.emptyList();
        assertEquals(
                requiredStandalone,
                requiredPolymorphic
        );

        // Name should match standalone. We don't want subtype schemas
        assertEquals(
                concretionStandaloneSchema.getName(),
                concretionAsPolymorphicSchema.getName()
        );

        //Properties should still include subtype-specific fields like 'onlyInA'.
        Map<?, ?> propsStandalone = concretionStandaloneSchema.getProperties();
        Map<?, ?> propsPolymorphic = concretionAsPolymorphicSchema.getProperties();
        assertNotNull(propsPolymorphic);
        assertTrue(propsPolymorphic.containsKey("onlyInA"));
        assertEquals(
                propsStandalone == null
                        ? java.util.Collections.emptySet()
                        : propsStandalone.keySet(),
                propsPolymorphic.keySet()
        );

        assertNotSame(
                concretionAsPropertySchema,
                concretionAsPolymorphicSchema
        );
    }


    @Test
    public void polymorphicSubtype_mustContainSubtypeFragment_notJustBaseRef() {
        ObjectMapper mapper = new ObjectMapper();
        ModelResolver resolver = new ModelResolver(mapper);
        ModelConverterContextImpl ctx = new ModelConverterContextImpl(resolver);

        AnnotatedType subtypeAT = new AnnotatedType()
                .type(ConcretionA.class)
                .schemaProperty(true);

        io.swagger.v3.oas.models.media.Schema resolvedSubtypeSchema = ctx.resolve(subtypeAT);

        assertTrue(resolvedSubtypeSchema.getProperties().containsKey("onlyInA"));
    }
}
