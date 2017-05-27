package io.swagger.jaxrs2;

import io.swagger.oas.models.security.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by RafaelLopez on 5/26/17.
 */
public class SecurityParser {
    public static final String SCOPE_NAME = "name";
    public static final String SCOPE_DESCRIPTION = "description";

    public Optional<List<SecurityRequirement>> getSecurityRequirementObjectFromAnnotation(io.swagger.oas.annotations.security.SecurityRequirement securityRequirement) {
        if (securityRequirement == null) {
            return Optional.empty();
        }
        List<SecurityRequirement> securityRequirements = new ArrayList<>();
        SecurityRequirement securityRequirementObject = new SecurityRequirement();
        StringBuilder scopes = new StringBuilder();
        for (String scope : securityRequirement.scopes()) {
            scopes.append(scope);
        }
        securityRequirementObject.addList(securityRequirement.name(), scopes.toString());
        securityRequirements.add(securityRequirementObject);

        return Optional.of(securityRequirements);
    }

    public Optional<SecurityScheme> getSecurityScheme(io.swagger.oas.annotations.security.SecurityScheme securityScheme) {
        if (securityScheme == null) {
            return Optional.empty();
        }
        SecurityScheme securitySchemeObject = new SecurityScheme();
        securitySchemeObject.setBearerFormat(securityScheme.bearerFormat());
        securitySchemeObject.setDescription(securityScheme.description());
        securitySchemeObject.setName(securityScheme.name());
        securitySchemeObject.setFlows(getOAuthFlows(securityScheme.flows()).get());
        return Optional.of(securitySchemeObject);
    }

    private Optional<OAuthFlows> getOAuthFlows(io.swagger.oas.annotations.security.OAuthFlows oAuthFlows) {
        if (oAuthFlows == null) {
            Optional.empty();
        }
        OAuthFlows oAuthFlowsObject = new OAuthFlows();
        oAuthFlowsObject.setAuthorizationCode(getOAuthFlow(oAuthFlows.authorizationCode()).get());
        oAuthFlowsObject.setClientCredentials(getOAuthFlow(oAuthFlows.clientCredentials()).get());
        oAuthFlowsObject.setImplicit(getOAuthFlow(oAuthFlows.implicit()).get());
        oAuthFlowsObject.setPassword(getOAuthFlow(oAuthFlows.password()).get());
        return Optional.of(oAuthFlowsObject);
    }

    private Optional<OAuthFlow> getOAuthFlow(io.swagger.oas.annotations.security.OAuthFlow oAuthFlow) {
        if (oAuthFlow == null) {
            return Optional.empty();
        }
        OAuthFlow oAuthFlowObject = new OAuthFlow();
        oAuthFlowObject.setAuthorizationUrl(oAuthFlow.authorizationUrl());
        oAuthFlowObject.setScopes(getScopes(oAuthFlow.scopes()).get());
        oAuthFlowObject.setRefreshUrl(oAuthFlow.refreshUrl());
        oAuthFlowObject.setTokenUrl(oAuthFlow.tokenUrl());
        return Optional.of(oAuthFlowObject);
    }

    private Optional<Scopes> getScopes(io.swagger.oas.annotations.security.Scopes scopes) {
        if (scopes == null) {
            return Optional.empty();
        }
        Scopes scopesObject = new Scopes();
        scopesObject.addString(SCOPE_NAME, scopes.name());
        scopesObject.addString(SCOPE_DESCRIPTION, scopes.description());
        return Optional.of(scopesObject);
    }

}
