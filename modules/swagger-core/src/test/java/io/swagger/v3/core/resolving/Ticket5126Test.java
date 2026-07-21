package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * Defensive invariant tests related to
 * <a href="https://github.com/swagger-api/swagger-core/issues/5126">#5126</a>.
 *
 * <p>{@link ModelResolver#handleUnwrapped} must not forward null-named property schemas into the
 * outer model's properties list. The later {@code modelProps.put(prop.getName(), prop)} step uses
 * those names as map keys, and a {@code null} key makes Jackson fail when serializing the schema.
 *
 * <p>These tests exercise the private method directly because the reported Spring HATEOAS path has
 * not been reproduced as a swagger-core-only resolver test. The clone-based case demonstrates the
 * concrete intermediate state this guard is intended to tolerate: {@code AnnotationsUtils.clone}
 * keeps the properties-map keys while losing nested {@code Schema.name} values because
 * {@code Schema.getName()} is {@code @JsonIgnore}.
 */
public class Ticket5126Test extends SwaggerTestBase {

    private ModelResolver modelResolver;
    private Method handleUnwrapped;

    @BeforeMethod
    public void setup() throws Exception {
        modelResolver = new ModelResolver(new ObjectMapper());
        handleUnwrapped = ModelResolver.class.getDeclaredMethod(
                "handleUnwrapped", List.class, Schema.class, String.class, String.class, List.class);
        handleUnwrapped.setAccessible(true);
    }

    @Test
    public void noPrefixOrSuffix_restoresNameFromClonedInnerPropertiesMapKey() throws Exception {
        Schema<Object> inner = cloneLosingNestedPropertyNames(namedInnerSchema());

        assertEquals(inner.getProperties().keySet(), new LinkedHashSet<>(Arrays.asList("name", "count")));
        assertNull(((Schema<?>) inner.getProperties().get("name")).getName());
        assertNull(((Schema<?>) inner.getProperties().get("count")).getName());

        List<Schema> props = invokeHandleUnwrapped(inner, "", "");

        assertPropertyNames(props, "name", "count");
        Schema<Object> outer = schemaWithModelProps(props);
        assertFalse(outer.getProperties().containsKey(null));
        Json.mapper().writeValueAsString(outer);
    }

    @Test
    public void noPrefixOrSuffix_restoresNameFromInnerPropertiesMapKeyForNullNameState() throws Exception {
        Schema<Object> inner = innerSchemaWithNullPropertyNames();

        List<Schema> props = invokeHandleUnwrapped(inner, "", "");

        assertPropertyNames(props, "name", "count");
    }

    @Test
    public void noPrefixOrSuffix_preservesExistingPropertyName() throws Exception {
        Schema<?> nameSchema = new Schema<>().type("string");
        nameSchema.setName("schemaName");
        Schema<Object> inner = new Schema<>();
        Map<String, Schema> innerProps = new LinkedHashMap<>();
        innerProps.put("mapName", nameSchema);
        inner.setProperties(innerProps);

        List<Schema> props = invokeHandleUnwrapped(inner, "", "");

        assertPropertyNames(props, "schemaName");
    }

    @Test
    public void withPrefixAndSuffix_combinesMapKeyWhenClonedInnerNameIsNull() throws Exception {
        Schema<Object> inner = cloneLosingNestedPropertyNames(namedInnerSchema());

        List<Schema> props = invokeHandleUnwrapped(inner, "p_", "_s");

        assertPropertyNames(props, "p_name_s", "p_count_s");
        Schema<Object> outer = schemaWithModelProps(props);
        assertFalse(outer.getProperties().containsKey(null));
        Json.mapper().writeValueAsString(outer);
    }

    @Test
    public void withPrefixAndSuffix_combinesMapKeyWhenInnerNameIsNull() throws Exception {
        Schema<Object> inner = innerSchemaWithNullPropertyNames();

        List<Schema> props = invokeHandleUnwrapped(inner, "p_", "_s");

        assertPropertyNames(props, "p_name_s", "p_count_s");
    }

    private Schema<Object> namedInnerSchema() {
        Schema<?> nameSchema = new Schema<>().type("string");
        nameSchema.setName("name");
        Schema<?> countSchema = new Schema<>().type("integer");
        countSchema.setName("count");

        Schema<Object> inner = new Schema<>();
        inner.setName("Inner");
        Map<String, Schema> innerProps = new LinkedHashMap<>();
        innerProps.put("name", nameSchema);
        innerProps.put("count", countSchema);
        inner.setProperties(innerProps);
        return inner;
    }

    private Schema<Object> innerSchemaWithNullPropertyNames() {
        Schema<?> nameSchema = new Schema<>().type("string");
        Schema<?> countSchema = new Schema<>().type("integer");
        Schema<Object> inner = new Schema<>();
        Map<String, Schema> innerProps = new LinkedHashMap<>();
        innerProps.put("name", nameSchema);
        innerProps.put("count", countSchema);
        inner.setProperties(innerProps);
        return inner;
    }

    private Schema<Object> cloneLosingNestedPropertyNames(Schema<Object> inner) {
        return AnnotationsUtils.clone(inner, false);
    }

    private List<Schema> invokeHandleUnwrapped(Schema<Object> inner, String prefix, String suffix) throws Exception {
        List<Schema> props = new ArrayList<>();
        List<String> requiredProps = new ArrayList<>();
        handleUnwrapped.invoke(modelResolver, props, inner, prefix, suffix, requiredProps);
        return props;
    }

    private Schema<Object> schemaWithModelProps(List<Schema> props) {
        Schema<Object> outer = new Schema<>();
        Map<String, Schema> modelProps = new LinkedHashMap<>();
        for (Schema prop : props) {
            modelProps.put(prop.getName(), prop);
        }
        outer.setProperties(modelProps);
        return outer;
    }

    private void assertPropertyNames(List<Schema> props, String... expectedNames) {
        assertEquals(props.size(), expectedNames.length);
        LinkedHashSet<String> names = new LinkedHashSet<>();
        for (Schema p : props) {
            assertNotNull(p.getName(),
                    "Each unwrapped property must carry a non-null name to avoid null keys in the outer's properties map");
            names.add(p.getName());
        }
        assertEquals(names, new LinkedHashSet<>(Arrays.asList(expectedNames)));
    }
}
