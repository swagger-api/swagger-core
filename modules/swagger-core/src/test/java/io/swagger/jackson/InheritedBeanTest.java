package io.swagger.jackson;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiModel;
import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;

import org.testng.annotations.Test;

import java.util.Map;

public class InheritedBeanTest extends SwaggerTestBase {

	private final ModelResolver modelResolver = new ModelResolver(new ObjectMapper());
	private final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

	@Test
	public void testInheritedBean() throws Exception {
		final Model baseModel = context.resolve(BaseBean.class);

		assertNotNull(baseModel);
		Map<String, Property> baseProperites = baseModel.getProperties();
		assertEquals(baseProperites.size(), 3);
		for (Map.Entry<String, Property> entry : baseProperites.entrySet()) {
			final String name = entry.getKey();
			final Property prop = entry.getValue();
			if ("type".equals(name)) {
				assertEquals(prop.getType(), "string");
			} else if ("a".equals(name)) {
				assertEquals(prop.getType(), "integer");
				assertEquals(prop.getFormat(), "int32");
			} else if ("b".equals(name)) {
				assertEquals(prop.getType(), "string");
			}
		}

		final Model subModel = context.getDefinedModels().get("Sub1Bean");
		assertNotNull(subModel);
		// make sure child points at parent
		assertTrue(subModel instanceof ComposedModel);
		ComposedModel cm = (ComposedModel) subModel;
		assertEquals(cm.getParent().getReference(), "#/definitions/BaseBean");

		// make sure parent properties are filtered out of subclass
		Map<String, Property> subProperties = cm.getChild().getProperties();
		assertEquals(subProperties.size(), 1);
	}

	@JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
	@JsonSubTypes({ @JsonSubTypes.Type(value = Sub1Bean.class, name = "sub1") })
	@ApiModel(description = "BaseBean", discriminator = "type", subTypes = { Sub1Bean.class })
	static class BaseBean {
		public String type;
		public int a;
		public String b;
	}

	@ApiModel(description = "Sub1Bean")
	static class Sub1Bean extends BaseBean {
		public int c;
	}

}
