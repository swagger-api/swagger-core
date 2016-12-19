package com.splitresourcesTest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by KangoV on 2016-05-22
 * #1800
 */
@Path("/api/1.0")
public interface SplitResource {
	
  @GET
	public Response getChildren();
	
}