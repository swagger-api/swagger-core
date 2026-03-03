package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CompositionSuperfluousRefTest {

    static class SomeDto {}
    static class OtherDto {}

    static class MyDtoOneOf {
        @io.swagger.v3.oas.annotations.media.Schema(oneOf = { SomeDto.class, OtherDto.class })
        public Object myProperty;
    }

    static class MyDtoWithAnyOf {
        @io.swagger.v3.oas.annotations.media.Schema(anyOf = { SomeDto.class, OtherDto.class })
        public Object myProperty;
    }

    static class MyDtoWithAllOf {
        @io.swagger.v3.oas.annotations.media.Schema(allOf = { SomeDto.class, OtherDto.class })
        public Object myProperty;
    }

    static class MyDtoWithoutComposition {
        @io.swagger.v3.oas.annotations.media.Schema(implementation = SomeDto.class)
        public Object myProperty;
    }

    static class MyDtoWithNonMatchingRef {
        @io.swagger.v3.oas.annotations.media.Schema(
                ref = "#/components/schemas/ThirdDto",
                oneOf = { SomeDto.class, OtherDto.class }
        )
        public Object myProperty;
    }

    @Test
    public void oneOf_shouldNotHaveRef() {
        ResolvedSchema rs = ModelConverters.getInstance(false)
                .resolveAsResolvedSchema(new AnnotatedType(MyDtoOneOf.class));

        Schema<?> prop = (Schema<?>) rs.schema.getProperties().get("myProperty");

        Assert.assertNull(prop.get$ref());
        Assert.assertNotNull(prop.getOneOf());
        Assert.assertEquals(prop.getOneOf().size(), 2);
    }

    @Test
    public void anyOf_shouldNotHaveRef() {
        ResolvedSchema rs = ModelConverters.getInstance(false)
                .resolveAsResolvedSchema(new AnnotatedType(MyDtoWithAnyOf.class));

        Schema<?> prop = (Schema<?>) rs.schema.getProperties().get("myProperty");

        Assert.assertNull(prop.get$ref());
        Assert.assertNotNull(prop.getAnyOf());
        Assert.assertEquals(prop.getAnyOf().size(), 2);
    }

    @Test
    public void allOf_shouldNotHaveRef() {

        ResolvedSchema rs = ModelConverters.getInstance(false)
                .resolveAsResolvedSchema(new AnnotatedType(MyDtoWithAllOf.class));

        Schema<?> prop = (Schema<?>) rs.schema.getProperties().get("myProperty");
        Assert.assertNull(prop.get$ref());
        Assert.assertNotNull(prop.getAllOf());
        Assert.assertEquals(prop.getAllOf().size(), 2);
    }

    @Test
    public void testNonMatchingRef_shouldPreserveRef() {
        ResolvedSchema rs = ModelConverters.getInstance(false)
                .resolveAsResolvedSchema(new AnnotatedType(MyDtoWithNonMatchingRef.class));

        Schema<?> prop = (Schema<?>) rs.schema.getProperties().get("myProperty");

        Assert.assertNotNull(prop.get$ref());
        Assert.assertEquals(prop.get$ref(), "#/components/schemas/ThirdDto");
        //In 3.x refs cannot have siblings
        Assert.assertNull(prop.getOneOf());
    }

    @Test
    public void oneOf_shouldNotHaveRef31() {
        ResolvedSchema rs = ModelConverters.getInstance(true)
                .resolveAsResolvedSchema(new AnnotatedType(MyDtoOneOf.class));

        Schema<?> prop = (Schema<?>) rs.schema.getProperties().get("myProperty");

        Assert.assertNull(prop.get$ref());
        Assert.assertNotNull(prop.getOneOf());
        Assert.assertEquals(prop.getOneOf().size(), 2);
    }

    @Test
    public void anyOf_shouldNotHaveRef31() {
        ResolvedSchema rs = ModelConverters.getInstance(true)
                .resolveAsResolvedSchema(new AnnotatedType(MyDtoWithAnyOf.class));

        Schema<?> prop = (Schema<?>) rs.schema.getProperties().get("myProperty");

        Assert.assertNull(prop.get$ref());
        Assert.assertNotNull(prop.getAnyOf());
        Assert.assertEquals(prop.getAnyOf().size(), 2);
    }

    @Test
    public void allOf_shouldNotHaveRef31() {

        ResolvedSchema rs = ModelConverters.getInstance(true)
                .resolveAsResolvedSchema(new AnnotatedType(MyDtoWithAllOf.class));

        Schema<?> prop = (Schema<?>) rs.schema.getProperties().get("myProperty");
        Assert.assertNull(prop.get$ref());
        Assert.assertNotNull(prop.getAllOf());
        Assert.assertEquals(prop.getAllOf().size(), 2);
    }

    @Test
    public void testNonMatchingRef_shouldPreserveRef31() {
        ResolvedSchema rs = ModelConverters.getInstance(true)
                .resolveAsResolvedSchema(new AnnotatedType(MyDtoWithNonMatchingRef.class));

        Schema<?> prop = (Schema<?>) rs.schema.getProperties().get("myProperty");

        Assert.assertNotNull(prop.get$ref());
        Assert.assertEquals(prop.get$ref(), "#/components/schemas/ThirdDto");
        Assert.assertNotNull(prop.getOneOf());
        Assert.assertEquals(prop.getOneOf().size(), 2);
    }

}


