package com.wordnik.swagger.sample.resource;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.jaxrs.*;

import javax.ws.rs.*;

@Path("/resources")
@Api("/resources")
@Produces({"application/json","application/xml"})
public class ApiListingResource extends JavaApiListing {}
