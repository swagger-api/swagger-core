package resources;

import javax.ws.rs.Path;

@Path("/head")
public class SubResourceHead {

  /**
   * This path is expected to be collected by
   * {@link com.wordnik.swagger.jaxrs.Reader}.
   *
   * @return class instance of sub-resource
   */
  @Path("tail")
  public Class<SubResourceTail> getTail() {
    return SubResourceTail.class;
  }

  /**
   * This path is expected to be skipped by
   * {@link com.wordnik.swagger.jaxrs.Reader} as {@link String} doesn't process
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
   * {@link com.wordnik.swagger.jaxrs.Reader} as resource class is unknown here.
   *
   * @return <code>null</code>
   */
  @Path("anyClass")
  public <T> Class<T> getAnyClass() {
    return null;
  }

  /**
   * This path is expected to be skipped by
   * {@link com.wordnik.swagger.jaxrs.Reader} as resource class is unknown here.
   *
   * @return <code>null</code>
   */
  @Path("wildcardClass")
  public Class<?> getWildcardClass() {
    return null;
  }

  /**
   * This path is expected to be skipped by
   * {@link com.wordnik.swagger.jaxrs.Reader} as method result is an array.
   *
   * @return <code>null</code>
   */
  @Path("classes")
  public Class<?>[] getClasses() {
    return null;
  }
}
