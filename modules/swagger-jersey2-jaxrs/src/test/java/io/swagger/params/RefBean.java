package io.swagger.params;

import io.swagger.annotations.ApiModelProperty;

import javax.ws.rs.BeanParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * Like {@link ChildBean} but instead of using inheritance this Bean will use a {@link BeanParam} directly.
 */
public class RefBean {

    @BeanParam
    public BaseBean beanParam;

    @HeaderParam("HeaderParam")
    @ApiModelProperty("a header param")
    private String headerParam;

    private String pathParam;
    @QueryParam("QueryParam")
    private List<String> queryParam;

    public BaseBean getBeanParam() {
        return beanParam;
    }

    public void setBeanParam(BaseBean beanParam) {
        this.beanParam = beanParam;
    }

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

    public List<String> getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(List<String> queryParam) {
        this.queryParam = queryParam;
    }

}
