package models;

import com.wordnik.swagger.annotations.ApiParam;

import javax.ws.rs.*;

public class Pagination {
  @QueryParam("skip")
  @ApiParam("number of records to skip")
  protected Integer skip;

  @QueryParam("limit")
  @ApiParam("maximum number of records to return")
  protected Integer limit;

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