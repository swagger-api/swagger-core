package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.jaxrs.Reader;

import javax.ws.rs.Path;

@Api
@Path("/head")
public class SubResourceHead {

    /**
     * This path is expected to be collected by
     * {@link Reader}.
     *
     * @return class instance of sub-resource
     */
    @Path("tail")
    @ApiOperation(value = "getTail")
    public Class<SubResourceTail> getTail() {
        return SubResourceTail.class;
    }

    /**
     * This path is expected to be collected by
     * {@link Reader}.
     *
     * @return class instance of sub-resource
     */
    @Path("noPath")
    @ApiOperation(value = "getNoPath")
    public Class<NoPathSubResource> getNoPath() {
        return NoPathSubResource.class;
    }

    /**
     * This path is expected to be skipped by
     * {@link Reader} as {@link String} doesn't process
     * any requests.
     *
     * @return string class
     */
    @Path("stringClass")
    @ApiOperation(value = "getStringClass")
    public Class<String> getStringClass() {
        return String.class;
    }

    /**
     * This path is expected to be skipped by
     * {@link Reader} as resource class is unknown here.
     *
     * @return <code>null</code>
     */
    @Path("anyClass")
    @ApiOperation(value = "getAnyClass")
    public <T> Class<T> getAnyClass() {
        return null;
    }

    /**
     * This path is expected to be skipped by
     * {@link Reader} as resource class is unknown here.
     *
     * @return <code>null</code>
     */
    @Path("wildcardClass")
    @ApiOperation(value = "getWildcardClass")
    public Class<?> getWildcardClass() {
        return null;
    }

    /**
     * This path is expected to be skipped by
     * {@link Reader} as method result is an array.
     *
     * @return <code>null</code>
     */
    @Path("classes")
    @ApiOperation(value = "getClasses")
    public Class<?>[] getClasses() {
        return null;
    }
}
