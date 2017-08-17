/**
 * Copyright 2017 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.oas.models.security;

/**
 * SecurityScheme
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc2/versions/3.0.md#securitySchemeObject"
 */

public class SecurityScheme {
    /**
     * Gets or Sets type
     */
    public enum Type {
        APIKEY("apiKey"),
        HTTP("http"),
        OAUTH2("oauth2"),
        OPENIDCONNECT("openIdConnect");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private Type type = null;
    private String description = null;
    private String name = null;
    private String $ref = null;

    /**
     * Gets or Sets in
     */
    public enum In {
        COOKIE("cookie"),

        HEADER("header"),

        QUERY("query");

        private final String value;

        In(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private In in = null;
    private String scheme = null;
    private String bearerFormat = null;
    private OAuthFlows flows = null;
    private String openIdConnectUrl = null;
    private java.util.Map<String, Object> extensions = null;

    /**
     * returns the type property from a SecurityScheme instance.
     *
     * @return Type type
     **/

    public Type getType() {
        return type;
    }

    /**
     * Sets the type property of a SecurityScheme instance
     * to the parameter
     *
     * @param type
     */

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Sets the type property of a SecurityScheme instance
     * to the parameter and returns the instance.
     *
     * @param type
     * @return SecurityScheme instance with the set type property
     */

    public SecurityScheme type(Type type) {
        this.type = type;
        return this;
    }

    /**
     * returns the description property from a SecurityScheme instance.
     *
     * @return String description
     **/

    public String getDescription() {
        return description;
    }

    /**
     * Sets the description property of a SecurityScheme instance
     * to the parameter.
     *
     * @param description
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description property of a SecurityScheme instance
     * to the parameter and returns the instance.
     *
     * @param description
     * @return SecurityScheme instance with the set description property
     */

    public SecurityScheme description(String description) {
        this.description = description;
        return this;
    }

    /**
     * returns the name property from a SecurityScheme instance.
     *
     * @return String name
     **/

    public String getName() {
        return name;
    }

    /**
     * Sets the name property of a SecurityScheme instance
     * to the parameter.
     *
     * @param name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the name property of a SecurityScheme instance
     * to the parameter and returns the instance.
     *
     * @param name
     * @return SecurityScheme instance with the set name property
     */

    public SecurityScheme name(String name) {
        this.name = name;
        return this;
    }

    /**
     * returns the in property from a SecurityScheme instance.
     *
     * @return In in
     **/

    public In getIn() {
        return in;
    }

    /**
     * Sets the in property of a SecurityScheme instance
     * to the parameter.
     *
     * @param in
     */

    public void setIn(In in) {
        this.in = in;
    }

    /**
     * Sets the in property of a SecurityScheme instance
     * to the parameter and returns the instance.
     *
     * @param in
     * @return SecurityScheme instance with the set in property
     */

    public SecurityScheme in(In in) {
        this.in = in;
        return this;
    }

    /**
     * returns the scheme property from a SecurityScheme instance.
     *
     * @return String scheme
     **/

    public String getScheme() {
        return scheme;
    }

    /**
     * Sets the scheme property of a SecurityScheme instance
     * to the parameter.
     *
     * @param scheme
     */

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * Sets the scheme property of a SecurityScheme instance
     * to the parameter and returns the instance.
     *
     * @param scheme
     * @return SecurityScheme instance with the set scheme property
     */

    public SecurityScheme scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * returns the bearerFormat property from a SecurityScheme instance.
     *
     * @return String bearerFormat
     **/

    public String getBearerFormat() {
        return bearerFormat;
    }

    /**
     * Sets the bearerFormat property of a SecurityScheme instance
     * to the parameter.
     *
     * @param bearerFormat
     */

    public void setBearerFormat(String bearerFormat) {
        this.bearerFormat = bearerFormat;
    }

    /**
     * Sets the bearerFormat property of a SecurityScheme instance
     * to the parameter and returns the instance.
     *
     * @param bearerFormat
     * @return SecurityScheme instance with the set bearerFormat property
     */

    public SecurityScheme bearerFormat(String bearerFormat) {
        this.bearerFormat = bearerFormat;
        return this;
    }

    /**
     * returns the flows property from a SecurityScheme instance.
     *
     * @return OAuthFlows flows
     **/

    public OAuthFlows getFlows() {
        return flows;
    }

    /**
     * Sets the flows property of a SecurityScheme instance
     * to the parameter.
     *
     * @param flows
     */

    public void setFlows(OAuthFlows flows) {
        this.flows = flows;
    }

    /**
     * Sets the flows property of a SecurityScheme instance
     * to the parameter and returns the instance.
     *
     * @param flows
     * @return SecurityScheme instance with the set flows property
     */

    public SecurityScheme flows(OAuthFlows flows) {
        this.flows = flows;
        return this;
    }

    /**
     * returns the openIdConnectUrl property from a SecurityScheme instance.
     *
     * @return String openIdConnectUrl
     **/

    public String getOpenIdConnectUrl() {
        return openIdConnectUrl;
    }

    /**
     * Sets the openIdConnectUrl property of a SecurityScheme instance
     * to the parameter.
     *
     * @param openIdConnectUrl
     */

    public void setOpenIdConnectUrl(String openIdConnectUrl) {
        this.openIdConnectUrl = openIdConnectUrl;
    }

    /**
     * Sets the openIdConnectUrl property of a SecurityScheme instance
     * to the parameter and returns the instance.
     *
     * @param openIdConnectUrl
     * @return SecurityScheme instance with the set openIdConnectUrl property
     */

    public SecurityScheme openIdConnectUrl(String openIdConnectUrl) {
        this.openIdConnectUrl = openIdConnectUrl;
        return this;
    }

    /**
     * Returns extensions property of a SecurityScheme instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     */

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds an object item to extensions map of a SecurityScheme instance
     * at the specified key.
     * If extensions is null, then creates a new HashMap and adds the item.
     *
     * @param name
     * @param value
     */

    public void addExtension(String name, Object value) {
        if (this.extensions == null) {
            this.extensions = new java.util.HashMap<>();
        }
        this.extensions.put(name, value);
    }

    /**
     * Sets extensions property of a SecurityScheme instance
     * to the parameter.
     *
     * @param extensions
     */

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    /**
     * returns the $ref property from an SecurityScheme instance.
     *
     * @return String $ref
     **/
    public String get$ref() {
        return $ref;
    }

    /**
     * Sets the $ref property of a SecurityScheme instance
     * to the parameter.
     *
     * @param $ref
     */

    public void set$ref(String $ref) {
        if ($ref != null && ($ref.indexOf(".") == -1 && $ref.indexOf("/") == -1)) {
            $ref = "#/components/securitySchemes/" + $ref;
        }
        this.$ref = $ref;
    }

    /**
     * Sets the $ref property of a SecurityScheme instance
     * to the parameter and returns the instance.
     *
     * @param $ref
     * @return SecurityScheme instance with the set $ref property
     */

    public SecurityScheme $ref(String $ref) {
        this.$ref = $ref;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityScheme)) {
            return false;
        }

        SecurityScheme that = (SecurityScheme) o;

        if (type != that.type) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if ($ref != null ? !$ref.equals(that.$ref) : that.$ref != null) {
            return false;
        }
        if (in != that.in) {
            return false;
        }
        if (scheme != null ? !scheme.equals(that.scheme) : that.scheme != null) {
            return false;
        }
        if (bearerFormat != null ? !bearerFormat.equals(that.bearerFormat) : that.bearerFormat != null) {
            return false;
        }
        if (flows != null ? !flows.equals(that.flows) : that.flows != null) {
            return false;
        }
        if (openIdConnectUrl != null ? !openIdConnectUrl.equals(that.openIdConnectUrl) : that.openIdConnectUrl != null) {
            return false;
        }
        return extensions != null ? extensions.equals(that.extensions) : that.extensions == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + ($ref != null ? $ref.hashCode() : 0);
        result = 31 * result + (in != null ? in.hashCode() : 0);
        result = 31 * result + (scheme != null ? scheme.hashCode() : 0);
        result = 31 * result + (bearerFormat != null ? bearerFormat.hashCode() : 0);
        result = 31 * result + (flows != null ? flows.hashCode() : 0);
        result = 31 * result + (openIdConnectUrl != null ? openIdConnectUrl.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SecurityScheme {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    in: ").append(toIndentedString(in)).append("\n");
        sb.append("    scheme: ").append(toIndentedString(scheme)).append("\n");
        sb.append("    bearerFormat: ").append(toIndentedString(bearerFormat)).append("\n");
        sb.append("    flows: ").append(toIndentedString(flows)).append("\n");
        sb.append("    openIdConnectUrl: ").append(toIndentedString(openIdConnectUrl)).append("\n");
        sb.append("    $ref: ").append(toIndentedString($ref)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Converts the given object to string with each line indented by 4 spaces
     * (except the first line).
     * This method adds formatting to the general toString() method.
     *
     * @param o Java object to be represented as String
     * @return Formatted String representation of the object
     */

    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
