package com.wordnik.swagger.sample.resource.alt;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.jaxrs.*;

import javax.ws.rs.*;

@Path("/api-docs.json")
@Api("/api-docs")
@Produces({"application/json"})
public class ApiListingResourceJSON extends AlternateApiListing {}