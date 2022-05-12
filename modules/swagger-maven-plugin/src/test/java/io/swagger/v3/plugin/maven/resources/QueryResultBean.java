package io.swagger.v3.plugin.maven.resources;

import javax.ws.rs.QueryParam;

public class QueryResultBean {
    @QueryParam("skip")
    private Integer skip;

    @QueryParam("limit")
    private Integer limit;

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
