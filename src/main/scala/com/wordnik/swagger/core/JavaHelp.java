/**
 *  Copyright 2011 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.wordnik.swagger.core;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.*;
import javax.ws.rs.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.sun.jersey.api.core.ResourceConfig;
import com.wordnik.swagger.core.*;

public abstract class JavaHelp {
	@GET
	public Response getHelp(@Context ServletConfig servConfig,
			@Context ResourceConfig resConfig, @Context HttpHeaders headers,
			@Context UriInfo uriInfo) throws JsonGenerationException,
			JsonMappingException, IOException {

		String apiVersion = servConfig != null ? servConfig.getInitParameter("api.version") : null;
		String swaggerVersion = servConfig != null ? servConfig.getInitParameter("swagger.version") : null;
		String basePath = servConfig != null ? servConfig.getInitParameter("swagger.api.basepath") : null;
		String apiFilterClassName = servConfig != null ? servConfig.getInitParameter("swagger.security.filter") : null;

		boolean filterOutTopLevelApi = true;

		Api currentApiEndPoint = this.getClass().getAnnotation(Api.class);
		String currentApiPath = currentApiEndPoint != null && filterOutTopLevelApi ? currentApiEndPoint.value() : null;

		HelpApi helpApi = new HelpApi(apiFilterClassName);
		System.out.println(this.getClass());
		Documentation docs = helpApi.filterDocs(ApiReader.read(this.getClass(),
				apiVersion, swaggerVersion, basePath, currentApiPath), headers,
				uriInfo, currentApiPath);
		Response response = Response.ok(docs).build();
		return response;
	}
}
