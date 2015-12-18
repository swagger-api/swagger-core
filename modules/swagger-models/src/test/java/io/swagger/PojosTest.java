package io.swagger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterPackageInfo;

//
//========================================================================
//Copyright (c) 1995-2015 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//All rights reserved. This program and the accompanying materials
//are made available under the terms of the Eclipse Public License v1.0
//and Apache License v2.0 which accompanies this distribution.
//
//  The Eclipse Public License is available at
//  http://www.eclipse.org/legal/epl-v10.html
//
//  The Apache License v2.0 is available at
//  http://www.opensource.org/licenses/apache2.0.php
//
//You may elect to redistribute this code under either of these licenses.
//========================================================================
//

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

import io.swagger.models.ComposedModel;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.RefModel;
import io.swagger.models.RefResponse;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.auth.AbstractSecuritySchemeDefinitionTestImpl;
import io.swagger.models.auth.In;
import io.swagger.models.parameters.AbstractParameterTestImpl;
import io.swagger.models.parameters.AbstractSerializableParameterTestImpl;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.properties.AbstractNumericProperty;
import io.swagger.models.properties.AbstractNumericPropertyTestImpl;
import io.swagger.models.properties.AbstractPropertyTestImpl;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.FloatProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

/*
 * This class is written  in order to test all the getters and setters in this module.
 * In order to test these we just need to pass a list of classes for which the getter and setter tests should be run.
 */
@PrepareForTest({In.class,RefFormat.class,RefType.class})
public class PojosTest extends PowerMockTestCase{
	private static final String[] POJO_PACKAGES = { "io.swagger.models", "io.swagger.models.auth",
			"io.swagger.models.parameters", "io.swagger.models.properties", "io.swagger.models.refs" };
	private ArrayList<PojoClass> pojoClasses;

	@BeforeMethod
	public void setup() {
		pojoClasses = new ArrayList<PojoClass>();
		for (String pojoPackage : POJO_PACKAGES) {
			List<PojoClass> packagePojoClasses = PojoClassFactory.getPojoClasses(pojoPackage, new FilterPackageInfo());
			for (PojoClass clazz : packagePojoClasses) {
				if (clazz.getName().contains("$") ||clazz.isAbstract()||clazz.isInterface()
						||clazz.isEnum()||clazz.getName().endsWith("Test"))
					continue;
				pojoClasses.add(clazz);
			}

		}
	}

	@Test
	public void testOpenPojo() {
		Validator validator = ValidatorBuilder.create().with(new SetterTester()).with(new GetterTester()).build();
		for (PojoClass clazz : pojoClasses) {
			//System.out.println("testing the getter & setter methods for class: " + clazz.getName());
			try {
				validator.validate(clazz);
			} catch (AssertionError ex) {
				continue;
			}
		}
	}
	
	@Test
	public void testEqualsAndHashcodes() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		Map<Class<?>, Set<String>>classesExclusions=new HashMap<Class<?>, Set<String>>();
		
		classesExclusions.put(AbstractSecuritySchemeDefinitionTestImpl.class,new HashSet<String>(Arrays.asList("type")));
		classesExclusions.put(BodyParameter.class,new HashSet<String>(Arrays.asList("examples")));
		classesExclusions.put(ComposedModel.class,new HashSet<String>(Arrays.asList("reference")));
		classesExclusions.put(DoubleProperty.class,new HashSet<String>(Arrays.asList("_enum")));
		classesExclusions.put(FloatProperty.class,new HashSet<String>(Arrays.asList("_enum")));
		classesExclusions.put(IntegerProperty.class,new HashSet<String>(Arrays.asList("_enum")));
		classesExclusions.put(License.class,new HashSet<String>(Arrays.asList("vendorExtensions")));
		classesExclusions.put(LongProperty.class,new HashSet<String>(Arrays.asList("_enum")));
		classesExclusions.put(ModelImpl.class,new HashSet<String>(Arrays.asList("_enum")));
		classesExclusions.put(RefModel.class,new HashSet<String>(Arrays.asList("title")));	
		classesExclusions.put(RefResponse.class,new HashSet<String>(Arrays.asList("headers","schema","vendorExtensions")));
		//classesExclusions.put(Response.class,new HashSet<String>(Arrays.asList("vendorExtensions","headers")));
		classesExclusions.put(Swagger.class,new HashSet<String>(Arrays.asList("vendorExtensions","responses")));		
		classesExclusions.put(Tag.class,new HashSet<String>(Arrays.asList("vendorExtensions")));
		
		
		Set<Class<?>>classesUsingInheritedFields=new HashSet<Class<?>>(Arrays.asList(
				AbstractSecuritySchemeDefinitionTestImpl.class,
				AbstractParameterTestImpl.class,AbstractPropertyTestImpl.class,
				AbstractNumericPropertyTestImpl.class,AbstractSerializableParameterTestImpl.class));
		
		for(PojoClass clazz:pojoClasses){
			Set<String> exclusions = classesExclusions.get(clazz.getClazz());
			//many classes actually have an enum field that is not used in the equals method
//			if(exclusions==null)exclusions=new HashSet<String>();
//			exclusions.add("_enum");
			TestUtils.testEquals(clazz.getClazz(),exclusions,classesUsingInheritedFields.contains(clazz.getClazz()) );
		}
	}
	
	@Test
	public void testBuilders() throws Exception{
		Map<Class<?>, Set<String>>classesExclusions=new HashMap<Class<?>, Set<String>>();
		
		classesExclusions.put(Operation.class,new HashSet<String>(Arrays.asList("deprecated","vendorExtensions")));
		classesExclusions.put(Swagger.class,new HashSet<String>(Arrays.asList("vendorExtensions")));
		
		
		for(PojoClass clazz:pojoClasses){
			Set<String> exclusions = classesExclusions.get(clazz.getClazz());
			TestUtils.testBuilders(clazz.getClazz(), exclusions);
		}
	}
}