package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/v1")
@Api(value = "root")
@XmlRootElement
public class Resource1343 {
    RestApplication _application = null;

    @GET
    @Path("/{dbkey}")
    @ApiOperation(value = "Retrieve a database resource")
    public DatabaseResource getDatabase(@ApiParam(name = "dbkey", value = "Database key") @PathParam("dbkey") String dbKey) throws Exception {
        return new DatabaseResource(this, dbKey);
    }

    public ComplicatedObject nothing() {
        return null;
    }

    public RestApplication getApplication() {
        return _application;
    }

    static class RestApplication {
        public Integer id;
        public String name;
    }

    static class DatabaseResource {
        public String databaseName;

        public DatabaseResource(Object parent, String key) {

        }
    }

    static class ComplicatedObject {
        public Integer id;
    }
}
