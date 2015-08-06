package io.swagger.params;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Extension of {@link BaseBean} to ensure we process hierarchies properly.
 */
public class ChildBean extends BaseBean {

    @HeaderParam("HeaderParam")
    private String headerParam;
    private String pathParam;
    @QueryParam("QueryParam")
    private String queryParam;

    public String getHeaderParam() {
        return headerParam;
    }

    public void setHeaderParam(String headerParam) {
        this.headerParam = headerParam;
    }

    public String getPathParam() {
        return pathParam;
    }

    @PathParam("PathParam")
    public void setPathParam(String pathParam) {
        this.pathParam = pathParam;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

}
