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
import io.swagger.models.parameters.Parameter;



public class OperationTest {




	
	@Test
	public void testBuilders() throws Exception {
		TestUtils.testBuilders(Operation.class,new HashSet<String>(Arrays.asList("deprecated","vendorExtensions")));
	}
	
	@Test
	public void testGettersAndSetters(){
		Operation operation=new Operation();
		
		operation.addScheme(Scheme.HTTP);
		assertTrue(operation.getSchemes().contains(Scheme.HTTP));
		
		operation.scheme(Scheme.HTTPS);
		assertTrue(operation.getSchemes().contains(Scheme.HTTPS));
		
		operation.consumes("consumes");
		assertTrue(operation.getConsumes().contains("consumes"));
		
		operation.produces("produces");
		assertTrue(operation.getProduces().contains("produces"));
		
		SecurityRequirement requirement=new SecurityRequirement();
		requirement.setName("name");
		requirement.setScopes(new ArrayList<String>());
		operation.security(requirement);
		assertTrue(operation.getSecurity().get(0).keySet().contains("name"));
		requirement.setScopes(null);
		operation.security(requirement);
		assertTrue(operation.getSecurity().get(1).get("name").isEmpty());
		
		Parameter parameter=Mockito.mock(Parameter.class);
		operation.setParameters(null);
		operation.parameter(parameter);
		assertTrue(operation.getParameters().contains(parameter));
		
		Response response=Mockito.mock(Response.class);
		operation.response(44, response);
		assertEquals(operation.getResponses().get("44"),response);
		
		operation.defaultResponse(response);
		assertEquals(operation.getResponses().get("default"),response);
		
		operation.deprecated(false);
		assertNull(operation.isDeprecated());
		operation.setDeprecated(true);
		assertTrue(operation.isDeprecated());
		
		String vendorName="x-vendor";
		String value="value";
		operation.setVendorExtension(vendorName, value);
		operation.vendorExtensions(new HashMap<String, Object>());
		assertEquals(operation.getVendorExtensions().get(vendorName), value);
		
		operation.tag("tag");
		assertTrue(operation.getTags().contains("tag"));
		
		operation.setSummary("summary");
		assertEquals(operation.getSummary(), "summary");
		
		operation.setDescription("description");
		assertEquals(operation.getDescription(), "description");
		
		operation.setOperationId("operationId");
		assertEquals(operation.getOperationId(), "operationId");
		
		ExternalDocs externalDocs=new ExternalDocs();
		operation.setExternalDocs(externalDocs);
		assertEquals(operation.getExternalDocs(),externalDocs );
		
	}
	

}
