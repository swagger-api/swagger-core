package io.swagger.models.auth;

import java.util.LinkedHashMap;
import java.util.Map;

public class OAuth2Definition extends AbstractSecuritySchemeDefinition {
    private String type = "oauth2";
    private String authorizationUrl;
    private String tokenUrl;
    private String flow;
    private Map<String, String> scopes;

    public OAuth2Definition() {
    }

    public OAuth2Definition implicit(String authorizationUrl) {
        this.setAuthorizationUrl(authorizationUrl);
        this.setFlow("implicit");
        return this;
    }

    public OAuth2Definition password(String tokenUrl) {
        this.setTokenUrl(tokenUrl);
        this.setFlow("password");
        return this;
    }

    public OAuth2Definition application(String tokenUrl) {
        this.setTokenUrl(tokenUrl);
        this.setFlow("application");
        return this;
    }

    public OAuth2Definition accessCode(String authorizationUrl, String tokenUrl) {
        this.setTokenUrl(tokenUrl);
        this.setAuthorizationUrl(authorizationUrl);
        this.setFlow("accessCode");
        return this;
    }

    public OAuth2Definition scope(String name, String description) {
        this.addScope(name, description);
        return this;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public Map<String, String> getScopes() {
        return scopes;
    }

    public void setScopes(Map<String, String> scopes) {
        this.scopes = scopes;
    }

    public void addScope(String name, String description) {
        if (this.scopes == null) {
            this.scopes = new LinkedHashMap<String, String>();
        }
        this.scopes.put(name, description);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((authorizationUrl == null) ? 0 : authorizationUrl.hashCode());
        result = prime * result + ((flow == null) ? 0 : flow.hashCode());
        result = prime * result + ((scopes == null) ? 0 : scopes.hashCode());
        result = prime * result + ((tokenUrl == null) ? 0 : tokenUrl.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OAuth2Definition other = (OAuth2Definition) obj;
        if (authorizationUrl == null) {
            if (other.authorizationUrl != null) {
                return false;
            }
        } else if (!authorizationUrl.equals(other.authorizationUrl)) {
            return false;
        }
        if (flow == null) {
            if (other.flow != null) {
                return false;
            }
        } else if (!flow.equals(other.flow)) {
            return false;
        }
        if (scopes == null) {
            if (other.scopes != null) {
                return false;
            }
        } else if (!scopes.equals(other.scopes)) {
            return false;
        }
        if (tokenUrl == null) {
            if (other.tokenUrl != null) {
                return false;
            }
        } else if (!tokenUrl.equals(other.tokenUrl)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
