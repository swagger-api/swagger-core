package io.swagger.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.print.attribute.HashAttributeSet;

import static org.testng.Assert.*;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import io.swagger.TestUtils;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;



public class SwaggerTest {




	
	
	
	@Test
	public void testGettersAndSetters(){
		Swagger swagger=new Swagger();
		
		swagger.addScheme(Scheme.HTTP);
		assertTrue(swagger.getSchemes().contains(Scheme.HTTP));
		
		swagger.scheme(Scheme.HTTPS);
		assertTrue(swagger.getSchemes().contains(Scheme.HTTPS));
		
		swagger.consumes("consumes");
		assertTrue(swagger.getConsumes().contains("consumes"));
		
		swagger.produces("produces");
		assertTrue(swagger.getProduces().contains("produces"));
		
		SecurityRequirement requirement=new SecurityRequirement();
		swagger.security(requirement);
		assertTrue(swagger.getSecurity().contains(requirement));
		swagger.setSecurityRequirement(new ArrayList<SecurityRequirement>(Arrays.asList(requirement)));
		assertTrue(swagger.getSecurityRequirement().contains(requirement));
		swagger.addSecurityDefinition(requirement);
		assertTrue(swagger.getSecurityRequirement().contains(requirement));
		
		Parameter parameter=Mockito.mock(Parameter.class);
		swagger.setParameters(null);
		String key="key";
		assertNull(swagger.getParameter(key));
		swagger.parameter(key,parameter);
		assertEquals(swagger.getParameters().get(key),parameter);
		assertEquals(swagger.getParameter(key),parameter);
		
		Response response=Mockito.mock(Response.class);
		swagger.response("44", response);
		assertEquals(swagger.getResponses().get("44"),response);
		
		
		
		String vendorName="x-vendor";
		String value="value";
		
		swagger.vendorExtension(vendorName, value);
		swagger.vendorExtensions(new HashMap<String, Object>());
		assertEquals(swagger.getVendorExtensions().get(vendorName), value);
		swagger.setVendorExtension(vendorName, value);
		assertEquals(swagger.getVendorExtensions().get(vendorName), value);
		swagger.vendorExtensions(null);
		assertEquals(swagger.getVendorExtensions().get(vendorName), value);
		
		swagger=new Swagger();
		swagger.vendorExtensions(new HashMap<String, Object>());
		assertNull(swagger.getVendorExtensions().get(vendorName));
		
		
		Tag tag=new Tag();
		tag.setName("name");
		swagger.tag(tag);
		assertTrue(swagger.getTags().contains(tag));

		swagger.tag(tag);
		assertTrue(swagger.getTags().contains(tag));
		
		swagger.tags(Arrays.asList(tag));
		assertTrue(swagger.getTags().contains(tag));
		
		
		
		ExternalDocs externalDocs=new ExternalDocs();
		swagger.setExternalDocs(externalDocs);
		assertEquals(swagger.getExternalDocs(),externalDocs );
		
		swagger.setPaths(null);
		assertNull(swagger.getPaths());
		assertNull(swagger.getPath(key));
		Path path=new Path();
		swagger.path(key, path);
		assertEquals(swagger.getPaths().get(key), path);
		assertEquals(swagger.getPath(key), path);
		
		SecuritySchemeDefinition securityDefinition=new BasicAuthDefinition();
		String name="name";
		swagger.securityDefinition(name, securityDefinition);
		assertEquals(swagger.getSecurityDefinitions().get(name), securityDefinition);
		
		Model model=new ComposedModel();
		swagger.model(name, model);
		assertEquals(swagger.getDefinitions().get(name), model);
		
		
		
	}
	

}
