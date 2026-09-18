package io.swagger.v3.core.resolving.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Reproduces the case where the raw member name starts with a get/is prefix followed by a
 * lower-case character (which the {@code ModelResolver} prefix-stripping heuristic would
 * otherwise restore verbatim) but an explicit {@code @JsonProperty} value must win.
 * <p>
 * Related: <a href="https://github.com/swagger-api/swagger-core/issues/415">...</a>
 */
public class UserJsonPropertyWinsOverGetterPrefix {

    private String data;

    // Methods named like an ordinary getter pair (getX / setX) with a lower-case letter after
    // the prefix, so the get/is prefix-stripping heuristic would try to restore the raw name
    // ("getvalue"). The explicit @JsonProperty("renamed") must take precedence.
    @JsonProperty("renamed")
    public String getvalue() {
        return data;
    }

    @JsonProperty("renamed")
    public void setvalue(String data) {
        this.data = data;
    }

    // A get/is-prefixed member without @JsonProperty, with a lower-case letter after the prefix:
    // the heuristic must still restore the raw member name (pre-existing behavior from #415/#2635).
    @JsonProperty
    public boolean isdetermined() {
        return true;
    }
}