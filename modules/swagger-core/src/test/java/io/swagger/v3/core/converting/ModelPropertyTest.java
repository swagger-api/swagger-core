package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.oas.models.Model1979;
import io.swagger.v3.core.oas.models.ModelWithBooleanProperty;
import io.swagger.v3.core.oas.models.ModelWithModelPropertyOverrides;
import io.swagger.v3.core.oas.models.ModelWithPrimitiveArray;
import io.swagger.v3.core.oas.models.ReadOnlyFields;
import io.swagger.v3.core.oas.models.RequiredFields;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ModelPropertyTest {
    @Test
    public void extractProperties() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(Family.class);
        assertEquals(models.size(), 3);

        final Schema person = models.get("Person");
        final Schema employer = (Schema) person.getProperties().get("employer");

        assertTrue(employer instanceof ArraySchema);
        final ArraySchema employerProperty = (ArraySchema) employer;

        final Schema items = employerProperty.getItems();
        assertEquals(items.get$ref(), "#/components/schemas/Employer");

        final Schema awards = (Schema) person.getProperties().get("awards");
        assertTrue(awards instanceof ArraySchema);
        assertTrue(((ArraySchema) awards).getItems() instanceof StringSchema);
    }

    @Test
    public void extractPrimitiveArray() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithPrimitiveArray.class);
        assertEquals(models.size(), 1);

        final Schema model = models.get("ModelWithPrimitiveArray");
        final ArraySchema longArray = (ArraySchema) model.getProperties().get("longArray");
        final Schema longArrayItems = longArray.getItems();
        assertTrue(longArrayItems instanceof IntegerSchema);

        final ArraySchema intArray = (ArraySchema) model.getProperties().get("intArray");
        assertTrue(intArray.getItems() instanceof IntegerSchema);
    }

    @Test
    public void readModelProperty() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(IsModelTest.class);
        final Schema model = models.get("IsModelTest");
        assertNotNull(model);
    }

    @Test(description = "it should read a model with property dataTypes configured #679")
    public void readDataTypesProperty() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithModelPropertyOverrides.class);
        final String json = "{" +
                "   \"Children\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"name\":{" +
                "            \"type\":\"string\"" +
                "         }" +
                "      }" +
                "   }," +
                "   \"ModelWithModelPropertyOverrides\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"children\":{" +
                "            \"type\":\"array\"," +
                "            \"items\":{" +
                "               \"$ref\":\"#/components/schemas/Children\"" +
                "            }" +
                "         }" +
                "      }" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(models, json);
    }

    @Test
    public void testReadOnlyProperty() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ReadOnlyFields.class);
        Schema model = models.get("ReadOnlyFields");
        assertTrue(((Schema) model.getProperties().get("id")).getReadOnly());
    }

    @Test
    public void testRequiredProperty() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(RequiredFields.class);
        Schema model = models.get("RequiredFields");
        assertFalse(model.getRequired().contains("optionalField"));
        assertFalse(model.getRequired().contains("primitiveTypeWithoutConstraint"));
        assertTrue(model.getRequired().contains("primitiveTypeWithConstraint"));
        assertTrue(model.getRequired().contains("required"));
        assertFalse(model.getRequired().contains("notRequired"));
        assertTrue(model.getRequired().contains("notRequiredWithAnnotation"));
        assertFalse(model.getRequired().contains("modeAuto"));
        assertTrue(model.getRequired().contains("modeAutoWithAnnotation"));
        assertTrue(model.getRequired().contains("modeRequired"));
        assertFalse(model.getRequired().contains("modeNotRequired"));
        assertFalse(model.getRequired().contains("modeNotRequiredWithAnnotation"));
        assertFalse(model.getRequired().contains("modeNotRequiredWithAnnotationForNotBlank"));
        assertFalse(model.getRequired().contains("modeNotRequiredWithAnnotationForNotEmpty"));
    }

    @Test
    public void modelAllowEmptyTest() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(Model1979.class);
        Schema model = models.get("Model1979");
        assertTrue(((Schema) model.getProperties().get("id")).getNullable());
    }

    @Test
    public void testIssue1743() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithBooleanProperty.class);
        final Schema model = models.get("ModelWithBooleanProperty");
        assertNotNull(model);

        BooleanSchema bp = (BooleanSchema) model.getProperties().get("isGreat");
        assertTrue(bp.getEnum().size() == 1);
        assertEquals(bp.getEnum().get(0), Boolean.TRUE);

        IntegerSchema is = (IntegerSchema) model.getProperties().get("intValue");
        assertTrue(is.getEnum().size() == 2);
        assertEquals(is.getEnum().get(0), new Integer(1));
        assertEquals(is.getEnum().get(1), new Integer(2));
    }

    @Test
    public void testNotNullWithNotBlankAndNotEmpty() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(NotNullWithNotBlankNotEmptyModel.class);
        Schema model = models.get("NotNullWithNotBlankNotEmptyModel");
        assertTrue(model.getRequired().contains("notNullAndNotBlank"));
        assertTrue(model.getRequired().contains("notNullAndNotEmptyList"));
        assertTrue(model.getRequired().contains("notNullAndNotEmptySet"));
    }

    @Test
    public void testCollectionWithNotEmpty() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(CollectionWithNotEmptyModel.class);
        Schema model = models.get("CollectionWithNotEmptyModel");
        ArraySchema listSchema = (ArraySchema) model.getProperties().get("notEmptyList");
        assertNotNull(listSchema);
        assertEquals(listSchema.getMinItems(), Integer.valueOf(1));
        ArraySchema setSchema = (ArraySchema) model.getProperties().get("notEmptySet");
        assertNotNull(setSchema);
        assertEquals(setSchema.getMinItems(), Integer.valueOf(1));
    }

    @Test
    public void testStringWithNotBlankAndSize() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(StringWithNotBlankAndSizeModel.class);
        Schema model = models.get("StringWithNotBlankAndSizeModel");
        Schema strSchema = (Schema) model.getProperties().get("notBlankAndSized");
        assertNotNull(strSchema);
        assertEquals(strSchema.getMinLength(), Integer.valueOf(5));
        assertEquals(strSchema.getMaxLength(), Integer.valueOf(10));
    }

    @Test
    public void testNotBlankNotEmptyWithRequiredModeNotRequired() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(NotBlankNotEmptyWithRequiredModeNotRequiredModel.class);
        Schema model = models.get("NotBlankNotEmptyWithRequiredModeNotRequiredModel");
        assertFalse(model.getRequired() != null && model.getRequired().contains("notBlankNotRequired"));
        assertFalse(model.getRequired() != null && model.getRequired().contains("notEmptyListNotRequired"));
        assertFalse(model.getRequired() != null && model.getRequired().contains("notEmptySetNotRequired"));
    }

    @Test
    public void testNotBlankNotEmptyWithRequiredModeRequired() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(NotBlankNotEmptyWithRequiredModeRequiredModel.class);
        Schema model = models.get("NotBlankNotEmptyWithRequiredModeRequiredModel");
        assertTrue(model.getRequired().contains("notBlankRequired"));
        assertTrue(model.getRequired().contains("notEmptyListRequired"));
        assertTrue(model.getRequired().contains("notEmptySetRequired"));
    }

    class Family {
        public Date membersSince;
        public List<Person> members;
    }

    class Person {
        public String firstname;
        public String lastname;
        public int age;
        public Date birthday;
        public List<Employer> employer;
        public List<String> awards;
    }

    class Employer {
        public String name;
        public int size;
    }

    class IsModelTest {
        public Boolean is_happy;
        public String name;
    }

    static class NotNullWithNotBlankNotEmptyModel {
        @NotNull
        @NotBlank
        public String notNullAndNotBlank;

        @NotNull
        @NotEmpty
        public List<String> notNullAndNotEmptyList;

        @NotNull
        @NotEmpty
        public Set<String> notNullAndNotEmptySet;
    }

    static class CollectionWithNotEmptyModel {
        @NotEmpty
        public List<String> notEmptyList;

        @NotEmpty
        public Set<String> notEmptySet;
    }

    static class StringWithNotBlankAndSizeModel {
        @NotBlank
        @Size(min = 5, max = 10)
        public String notBlankAndSized;
    }

    static class NotBlankNotEmptyWithRequiredModeRequiredModel {
        @NotBlank
        @io.swagger.v3.oas.annotations.media.Schema(requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
        public String notBlankRequired;

        @NotEmpty
        @io.swagger.v3.oas.annotations.media.Schema(requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
        public List<String> notEmptyListRequired;

        @NotEmpty
        @io.swagger.v3.oas.annotations.media.Schema(requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
        public Set<String> notEmptySetRequired;
    }

    static class NotBlankNotEmptyWithRequiredModeNotRequiredModel {
        @NotBlank
        @io.swagger.v3.oas.annotations.media.Schema(requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED)
        public String notBlankNotRequired;

        @NotEmpty
        @io.swagger.v3.oas.annotations.media.Schema(requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED)
        public List<String> notEmptyListNotRequired;

        @NotEmpty
        @io.swagger.v3.oas.annotations.media.Schema(requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED)
        public Set<String> notEmptySetNotRequired;
    }
}
