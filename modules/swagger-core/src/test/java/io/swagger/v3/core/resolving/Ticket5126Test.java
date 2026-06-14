package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Regression test for <a href="https://github.com/swagger-api/swagger-core/issues/5126">#5126</a>.
 *
 * <p>When an inner model's nested property schemas have a {@code null} name — which happens
 * naturally after {@code AnnotationsUtils.clone} round-trips the schema through JSON, because
 * {@code Schema.getName()} is {@code @JsonIgnore} — {@link ModelResolver#handleUnwrapped} would
 * forward those schemas into the outer model's properties list as-is. The subsequent
 * {@code modelProps.put(prop.getName(), prop)} then inserts a {@code null} key into the outer's
 * properties map, which later causes Jackson to fail serializing the OpenAPI document with
 * {@code JsonMappingException: Null key for a Map not allowed in JSON}.
 *
 * <p>This test invokes {@code handleUnwrapped} directly with a hand-crafted inner model whose
 * property schemas have a {@code null} name (simulating the post-clone state) and verifies that
 * the resulting property names come from the inner properties-map keys, not from the transient
 * {@code Schema.name} field.
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
    public void noPrefixOrSuffix_restoresNameFromInnerPropertiesMapKey() throws Exception {
        Schema<?> nameSchema = new Schema<>().type("string");      // name field unset
        Schema<?> countSchema = new Schema<>().type("integer");    // name field unset
        Schema<Object> inner = new Schema<>();
        Map<String, Schema> innerProps = new LinkedHashMap<>();
        innerProps.put("name", nameSchema);
        innerProps.put("count", countSchema);
        inner.setProperties(innerProps);

        List<Schema> props = new ArrayList<>();
        List<String> requiredProps = new ArrayList<>();
        handleUnwrapped.invoke(modelResolver, props, inner, "", "", requiredProps);

        assertEquals(props.size(), 2);
        LinkedHashSet<String> names = new LinkedHashSet<>();
        for (Schema p : props) {
            assertNotNull(p.getName(),
                    "Each unwrapped property must carry a non-null name to avoid null keys in the outer's properties map");
            names.add(p.getName());
        }
        assertEquals(names, new LinkedHashSet<>(java.util.Arrays.asList("name", "count")));
    }

    @Test
    public void withPrefixAndSuffix_combinesMapKeyWhenInnerNameIsNull() throws Exception {
        Schema<?> nameSchema = new Schema<>().type("string");
        Schema<?> countSchema = new Schema<>().type("integer");
        Schema<Object> inner = new Schema<>();
        Map<String, Schema> innerProps = new LinkedHashMap<>();
        innerProps.put("name", nameSchema);
        innerProps.put("count", countSchema);
        inner.setProperties(innerProps);

        List<Schema> props = new ArrayList<>();
        List<String> requiredProps = new ArrayList<>();
        handleUnwrapped.invoke(modelResolver, props, inner, "p_", "_s", requiredProps);

        assertEquals(props.size(), 2);
        LinkedHashSet<String> names = new LinkedHashSet<>();
        for (Schema p : props) {
            assertNotNull(p.getName(),
                    "Prefixed/suffixed names must be derived from the inner properties-map key when the original Schema.name has been lost");
            names.add(p.getName());
        }
        assertEquals(names, new LinkedHashSet<>(java.util.Arrays.asList("p_name_s", "p_count_s")));
    }
}
