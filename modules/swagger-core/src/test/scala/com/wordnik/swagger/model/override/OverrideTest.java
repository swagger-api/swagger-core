package com.wordnik.swagger.model.override;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.models.Model;

public class OverrideTest {

	@Test
	public void test() {
		GenericModel.declareProperty("name", String.class);
		GenericModel.declareProperty("count", int.class);			
		ModelConverters.getInstance().addConverter(new GericModelConverter());
		Map<String, Model> read = ModelConverters.getInstance().read(GenericModel.class);				
		Assert.assertTrue(read.containsKey(GenericModel.class.getSimpleName()));
		Model model = read.get(GenericModel.class.getSimpleName());
		Assert.assertTrue(model.getProperties().containsKey("name"));
		Assert.assertEquals("string",model.getProperties().get("name").getType());
		Assert.assertTrue(model.getProperties().containsKey("count"));
		Assert.assertEquals("integer",model.getProperties().get("count").getType());
	}
}
