package io.swagger.v3.oas.models.parameters;

import java.util.Objects;

/**
 * QueryStringParameter. Represents a parameter with `in: querystring` (OpenAPI 3.2):
 * the parameter's `content` map describes how to deserialize the whole query string.
 * Per the 3.2 specification, `schema`, `style`, `explode` and `allowReserved` MUST NOT
 * be used, `content` MUST be used instead of `schema`, it MUST NOT coexist with
 * `in: query` parameters in the same effective parameter set, and at most one
 * `in: querystring` parameter is allowed per operation or path item. These rules
 * are not enforced by the model itself.
 *
 * @since 2.2.56 (OpenAPI 3.2)
 */

public class QueryStringParameter extends Parameter {
    private String in = "querystring";

    /**
     * returns the in property from a QueryStringParameter instance.
     *
     * @return String in
     **/
    @Override
    public String getIn() {
        return in;
    }

    @Override
    public void setIn(String in) {
        this.in = in;
    }

    @Override
    public QueryStringParameter in(String in) {
        this.in = in;
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueryStringParameter queryStringParameter = (QueryStringParameter) o;
        return Objects.equals(this.in, queryStringParameter.in) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(in, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class QueryStringParameter {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    in: ").append(toIndentedString(in)).append("\n");
        sb.append("}");
        return sb.toString();
    }

}
