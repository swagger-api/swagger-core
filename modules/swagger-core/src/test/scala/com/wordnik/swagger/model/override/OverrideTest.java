package com.wordnik.swagger.model.override;

import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.util.Json;

import java.util.*;

import junit.framework.Assert;
import org.junit.Test;

public class OverrideTest {
	@Test
	public void test() {
		GenericModel.declareProperty("name", String.class);
		GenericModel.declareProperty("count", int.class);			

    // create new instead of use singleton
    ModelConverters converters = new ModelConverters();
    converters.addConverter(new GericModelConverter());
		Map<String, Model> read = converters.read(GenericModel.class);

		Assert.assertTrue(read.containsKey(GenericModel.class.getSimpleName()));
		Model model = read.get(GenericModel.class.getSimpleName());

		Assert.assertTrue(model.getProperties().containsKey("name"));
		Assert.assertEquals("string",model.getProperties().get("name").getType());
		Assert.assertTrue(model.getProperties().containsKey("count"));
		Assert.assertEquals("integer",model.getProperties().get("count").getType());
	}
}
