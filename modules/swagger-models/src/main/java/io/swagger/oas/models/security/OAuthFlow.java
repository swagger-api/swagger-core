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

import java.util.Objects;

/**
 * OAuthFlow
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc2/versions/3.0.md#oauthFlowsObject"
 */

public class OAuthFlow {
    private String authorizationUrl = null;
    private String tokenUrl = null;
    private String refreshUrl = null;
    private Scopes scopes = null;
    private java.util.Map<String, Object> extensions = null;

    /**
     * returns the authorizationUrl property from a OAuthFlow instance.
     *
     * @return String authorizationUrl
     **/

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    /**
     * Sets the authorizationUrl property of an OAuthFlow instance
     * to the parameter.
     *
     * @param authorizationUrl
     */

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    /**
     * Sets the authorizationUrl property of an OAuthFlow instance
     * to the parameter and returns the instance.
     *
     * @param authorizationUrl
     * @return OAuthFlow instance with the set authorizationUrl property
     */

    public OAuthFlow authorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
        return this;
    }

    /**
     * returns the tokenUrl property from a OAuthFlow instance.
     *
     * @return String tokenUrl
     **/

    public String getTokenUrl() {
        return tokenUrl;
    }

    /**
     * Sets the tokenUrl property of an OAuthFlow instance.
     *
     * @param tokenkUrl
     */

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    /**
     * Sets the tokenUrl property of an OAuthFlow instance
     * to the parameter and returns the instance.
     *
     * @param tokenUrl
     * @return OAuthFlow instance with the set tokenUrl property
     */

    public OAuthFlow tokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
        return this;
    }

    /**
     * returns the refreshUrl property from a OAuthFlow instance.
     *
     * @return String refreshUrl
     **/

    public String getRefreshUrl() {
        return refreshUrl;
    }

    /**
     * Sets the refreshUrl property of an OAuthFlow instance
     * to the parameter.
     *
     * @param refreshUrl
     */

    public void setRefreshUrl(String refreshUrl) {
        this.refreshUrl = refreshUrl;
    }

    /**
     * Sets the refreshUrl property of an OAuthFlow instance
     * to the parameter and returns the instance.
     *
     * @param refreshUrl
     * @return OAuthFlow instance with the set refreshUrl property
     */

    public OAuthFlow refreshUrl(String refreshUrl) {
        this.refreshUrl = refreshUrl;
        return this;
    }

    /**
     * returns the scopes property from a OAuthFlow instance.
     *
     * @return Scopes scopes
     **/

    public Scopes getScopes() {
        return scopes;
    }

    /**
     * Sets the scopes property of an OAuthFlow instance
     * to the parameter.
     *
     * @param scopes
     */

    public void setScopes(Scopes scopes) {
        this.scopes = scopes;
    }

    /**
     * Sets the scopes property of an OAuthFlow instance
     * to the parameter and returns the instance.
     *
     * @param scopes
     * @return OAuthFlow instance with the set scopes property
     */

    public OAuthFlow scopes(Scopes scopes) {
        this.scopes = scopes;
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
        OAuthFlow oauthFlow = (OAuthFlow) o;
        return Objects.equals(this.authorizationUrl, oauthFlow.authorizationUrl) &&
               Objects.equals(this.tokenUrl, oauthFlow.tokenUrl) &&
               Objects.equals(this.refreshUrl, oauthFlow.refreshUrl) &&
               Objects.equals(this.scopes, oauthFlow.scopes) &&
               Objects.equals(this.extensions, oauthFlow.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationUrl, tokenUrl, refreshUrl, scopes, extensions);
    }

    /**
     * Returns extensions property of a OAuthFlow instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     */

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds an object item to extensions map at
     * the specified key.
     * <p>
     * If extensions is null, creates a new HashMap
     * and adds item to it
     *
     * @param name - map key
     * @param value - map value
     */

    public void addExtension(String name, Object value) {
        if (this.extensions == null) {
            this.extensions = new java.util.HashMap<>();
        }
        this.extensions.put(name, value);
    }

    /**
     * Sets extensions property of a OAuthFlow instance
     * to the parameter.
     *
     * @param extensions
     */

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OAuthFlow {\n");

        sb.append("    authorizationUrl: ").append(toIndentedString(authorizationUrl)).append("\n");
        sb.append("    tokenUrl: ").append(toIndentedString(tokenUrl)).append("\n");
        sb.append("    refreshUrl: ").append(toIndentedString(refreshUrl)).append("\n");
        sb.append("    scopes: ").append(toIndentedString(scopes)).append("\n");
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
