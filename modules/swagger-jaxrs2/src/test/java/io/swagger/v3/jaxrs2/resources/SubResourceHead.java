package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.Path;

@Path("/head")
public class SubResourceHead {

    /**
     * This path is expected to be collected
     *
     * @return class instance of sub-resource
     */
    @Path("tail")
    public Class<SubResourceTail> getTail() {
        return SubResourceTail.class;
    }

    /**
     * This path is expected to be collected
     *
     * @return class instance of sub-resource
     */
    @Path("noPath")
    public Class<NoPathSubResource> getNoPath() {
        return NoPathSubResource.class;
    }

    /**
     * This path is expected to be skipped as {@link String} doesn't process
     * any requests.
     *
     * @return string class
     */
    @Path("stringClass")
    public Class<String> getStringClass() {
        return String.class;
    }

    /**
     * This path is expected to be skipped as resource class is unknown here.
     *
     * @return {@code null}
     */
    @Path("anyClass")
    public <T> Class<T> getAnyClass() {
        return null;
    }

    /**
     * This path is expected to be skipped as resource class is unknown here.
     *
     * @return {@code null}
     */
    @Path("wildcardClass")
    public Class<?> getWildcardClass() {
        return null;
    }

    /**
     * This path is expected to be skipped as method result is an array.
     *
     * @return {@code null}
     */
    @Path("classes")
    public Class<?>[] getClasses() {
        return null;
    }
}
