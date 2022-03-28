package io.swagger.v3.oas.models.parameters;

import java.util.Objects;

/**
 * CookieParameter
 */

public class CookieParameter extends Parameter {
    private String in = "cookie";

    /**
     * returns the in property from a CookieParameter instance.
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
    public CookieParameter in(String in) {
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
        CookieParameter cookieParameter = (CookieParameter) o;
        return Objects.equals(this.in, cookieParameter.in) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(in, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CookieParameter {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    in: ").append(toIndentedString(in)).append("\n");
        sb.append("}");
        return sb.toString();
    }

}

