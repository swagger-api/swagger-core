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

package io.swagger.v3.oas.models.security;

import java.util.Objects;

/**
 * OAuthFlows
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#oauthFlowsObject"
 */

public class OAuthFlows {
    private OAuthFlow implicit = null;
    private OAuthFlow password = null;
    private OAuthFlow clientCredentials = null;
    private OAuthFlow authorizationCode = null;
    private java.util.Map<String, Object> extensions = null;

    /**
     * returns the implicit property from a OAuthFlows instance.
     *
     * @return OAuthFlow implicit
     **/

    public OAuthFlow getImplicit() {
        return implicit;
    }

    public void setImplicit(OAuthFlow implicit) {
        this.implicit = implicit;
    }

    public OAuthFlows implicit(OAuthFlow implicit) {
        this.implicit = implicit;
        return this;
    }

    /**
     * returns the password property from a OAuthFlows instance.
     *
     * @return OAuthFlow password
     **/

    public OAuthFlow getPassword() {
        return password;
    }

    public void setPassword(OAuthFlow password) {
        this.password = password;
    }

    public OAuthFlows password(OAuthFlow password) {
        this.password = password;
        return this;
    }

    /**
     * returns the clientCredentials property from a OAuthFlows instance.
     *
     * @return OAuthFlow clientCredentials
     **/

    public OAuthFlow getClientCredentials() {
        return clientCredentials;
    }

    public void setClientCredentials(OAuthFlow clientCredentials) {
        this.clientCredentials = clientCredentials;
    }

    public OAuthFlows clientCredentials(OAuthFlow clientCredentials) {
        this.clientCredentials = clientCredentials;
        return this;
    }

    /**
     * returns the authorizationCode property from a OAuthFlows instance.
     *
     * @return OAuthFlow authorizationCode
     **/

    public OAuthFlow getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(OAuthFlow authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public OAuthFlows authorizationCode(OAuthFlow authorizationCode) {
        this.authorizationCode = authorizationCode;
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
        OAuthFlows oauthFlows = (OAuthFlows) o;
        return Objects.equals(this.implicit, oauthFlows.implicit) &&
                Objects.equals(this.password, oauthFlows.password) &&
                Objects.equals(this.clientCredentials, oauthFlows.clientCredentials) &&
                Objects.equals(this.authorizationCode, oauthFlows.authorizationCode) &&
                Objects.equals(this.extensions, oauthFlows.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(implicit, password, clientCredentials, authorizationCode, extensions);
    }

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    public void addExtension(String name, Object value) {
        if (name == null || name.isEmpty() || !name.startsWith("x-")) {
            return;
        }
        if (this.extensions == null) {
            this.extensions = new java.util.HashMap<>();
        }
        this.extensions.put(name, value);
    }

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public OAuthFlows extensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OAuthFlows {\n");

        sb.append("    implicit: ").append(toIndentedString(implicit)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    clientCredentials: ").append(toIndentedString(clientCredentials)).append("\n");
        sb.append("    authorizationCode: ").append(toIndentedString(authorizationCode)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

