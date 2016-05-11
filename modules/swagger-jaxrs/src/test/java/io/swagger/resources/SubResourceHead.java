package io.swagger.resources;

import io.swagger.annotations.Api;
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
    public Class<String> getStringClass() {
        return String.class;
    }

    /**
     * This path is expected to be skipped by
     * {@link Reader} as resource class is unknown here.
     *
     * @return {@code null}
     */
    @Path("anyClass")
    public <T> Class<T> getAnyClass() {
        return null;
    }

    /**
     * This path is expected to be skipped by
     * {@link Reader} as resource class is unknown here.
     *
     * @return {@code null}
     */
    @Path("wildcardClass")
    public Class<?> getWildcardClass() {
        return null;
    }

    /**
     * This path is expected to be skipped by
     * {@link Reader} as method result is an array.
     *
     * @return {@code null}
     */
    @Path("classes")
    public Class<?>[] getClasses() {
        return null;
    }
}
