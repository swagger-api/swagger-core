package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.swagger.v3.core.resolving.SwaggerTestBase.mapper;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class Ticket5028Test {

    @Test
    public void testSubtypeKeepsCompositionOpenApi30() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        context.resolve(new AnnotatedType(AttributeType.class));
        final Map<String, io.swagger.v3.oas.models.media.Schema> models = context.getDefinedModels();

        final io.swagger.v3.oas.models.media.Schema subtype = models.get("DateAttributeTypeImpl");
        assertNotNull(subtype, "subtype schema should be defined");
        assertNotNull(subtype.getAllOf(),
                "subtype must keep its allOf reference to the parent, but was: " + subtype);
        assertEquals(subtype.getAllOf().size(), 1);
        assertEquals(((io.swagger.v3.oas.models.media.Schema) subtype.getAllOf().get(0)).get$ref(),
                "#/components/schemas/AttributeType");
    }

    @Test
    public void testSubtypeKeepsCompositionOpenApi31() {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        context.resolve(new AnnotatedType(AttributeType.class));
        final Map<String, io.swagger.v3.oas.models.media.Schema> models = context.getDefinedModels();

        final io.swagger.v3.oas.models.media.Schema subtype = models.get("DateAttributeTypeImpl");
        assertNotNull(subtype, "subtype schema should be defined");
        assertNotNull(subtype.getAllOf(),
                "subtype must keep its allOf reference to the parent, but was: " + subtype);
        assertTrue(subtype.getAllOf().stream()
                        .anyMatch(s -> ("#/components/schemas/AttributeType")
                                .equals(((io.swagger.v3.oas.models.media.Schema) s).get$ref())),
                "allOf must reference the parent AttributeType, but was: " + subtype.getAllOf());
    }

    @Test
    public void testUnwrappedUsageKeepsSubtypeCompositionOpenApi30() {
        assertUnwrappedUsageKeepsComposition(false, Step.class, Transform.class);
        assertUnwrappedUsageKeepsComposition(false, Transform.class, Step.class);
    }

    @Test
    public void testUnwrappedUsageKeepsSubtypeCompositionOpenApi31() {
        assertUnwrappedUsageKeepsComposition(true, Step.class, Transform.class);
        assertUnwrappedUsageKeepsComposition(true, Transform.class, Step.class);
    }

    private void assertUnwrappedUsageKeepsComposition(boolean openapi31, Class<?>... resolveInOrder) {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        modelResolver.setOpenapi31(openapi31);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        for (Class<?> type : resolveInOrder) {
            context.resolve(new AnnotatedType(type));
        }

        final io.swagger.v3.oas.models.media.Schema customAttr = context.getDefinedModels().get("CustomAttr");
        assertNotNull(customAttr, "CustomAttr schema should be defined");
        assertNotNull(customAttr.getAllOf(),
                "CustomAttr must keep its allOf reference to Location, but was: " + customAttr);
        assertTrue(customAttr.getAllOf().stream()
                        .anyMatch(s -> "#/components/schemas/Location"
                                .equals(((io.swagger.v3.oas.models.media.Schema) s).get$ref())),
                "allOf must reference Location, but was: " + customAttr.getAllOf());
    }

    interface ResourceType {
    }

    @Schema(
            subTypes = {DateAttributeType.class},
            discriminatorProperty = "resourceType",
            discriminatorMapping = {
                    @DiscriminatorMapping(value = "DateAttributeType", schema = DateAttributeType.class)
            })
    interface AttributeType extends ResourceType {
        @Schema(description = "The public id of the attribute type.", example = "ApprovalDate_C")
        String getPublicId();
    }

    @Schema(implementation = DateAttributeTypeImpl.class)
    interface DateAttributeType extends AttributeType {
    }

    static class AttributeTypeImpl implements AttributeType {
        @Override
        public String getPublicId() {
            return "";
        }
    }

    static class DateAttributeTypeImpl extends AttributeTypeImpl implements DateAttributeType {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes({@JsonSubTypes.Type(value = CustomAttr.class, name = "customAttr")})
    @Schema(discriminatorProperty = "type",
            discriminatorMapping = @DiscriminatorMapping(value = "customAttr", schema = CustomAttr.class))
    interface Location {
    }

    // also used unwrapped, hence no type id of its own
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    static class CustomAttr implements Location {
        public String customAspect;
        public String attribute;
    }

    static class Transform {
        public String expression;

        @JsonUnwrapped
        public CustomAttr target;
    }

    static class Step {
        public List<Location> oldDefinitions;
    }
}
