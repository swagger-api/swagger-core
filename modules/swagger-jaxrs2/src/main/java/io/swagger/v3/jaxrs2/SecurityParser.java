package io.swagger.v3.jaxrs2;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SecurityParser {

    public static Optional<List<SecurityRequirement>> getSecurityRequirements(io.swagger.v3.oas.annotations.security.SecurityRequirement[] securityRequirementsApi) {
        if (securityRequirementsApi == null || securityRequirementsApi.length == 0) {
            return Optional.empty();
        }
        List<SecurityRequirement> securityRequirements = new ArrayList<>();
        for (io.swagger.v3.oas.annotations.security.SecurityRequirement securityRequirementApi : securityRequirementsApi) {
            if (StringUtils.isBlank(securityRequirementApi.name())) {
                continue;
            }
            SecurityRequirement securityRequirement = new SecurityRequirement();
            if (securityRequirementApi.scopes().length > 0) {
                securityRequirement.addList(securityRequirementApi.name(), Arrays.asList(securityRequirementApi.scopes()));
            } else {
                securityRequirement.addList(securityRequirementApi.name());
            }
            securityRequirements.add(securityRequirement);
        }
        if (securityRequirements.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(securityRequirements);
    }

    public static Optional<SecurityScheme> getSecurityScheme(io.swagger.v3.oas.annotations.security.SecurityScheme securityScheme) {
        if (securityScheme == null) {
            return Optional.empty();
        }
        SecurityScheme securitySchemeObject = new SecurityScheme();

        if (StringUtils.isNotBlank(securityScheme.in().toString())) {
            securitySchemeObject.setIn(getIn(securityScheme.in().toString()));
        }
        if (StringUtils.isNotBlank(securityScheme.type().toString())) {
            securitySchemeObject.setType(getType(securityScheme.type().toString()));
        }

        if (StringUtils.isNotBlank(securityScheme.openIdConnectUrl())) {
            securitySchemeObject.setOpenIdConnectUrl(securityScheme.openIdConnectUrl());
        }
        if (StringUtils.isNotBlank(securityScheme.scheme())) {
            securitySchemeObject.setScheme(securityScheme.scheme());
        }

        if (StringUtils.isNotBlank(securityScheme.bearerFormat())) {
            securitySchemeObject.setBearerFormat(securityScheme.bearerFormat());
        }
        if (StringUtils.isNotBlank(securityScheme.description())) {
            securitySchemeObject.setDescription(securityScheme.description());
        }
        if (StringUtils.isNotBlank(securityScheme.name())) {
            securitySchemeObject.setName(securityScheme.name());
        }
        if (securityScheme.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(securityScheme.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    securitySchemeObject.addExtension(ext, extensions.get(ext));
                }
            }
        }

        getOAuthFlows(securityScheme.flows()).ifPresent(securitySchemeObject::setFlows);
        return Optional.of(securitySchemeObject);
    }

    public static Optional<OAuthFlows> getOAuthFlows(io.swagger.v3.oas.annotations.security.OAuthFlows oAuthFlows) {
        if (isEmpty(oAuthFlows)) {
            return Optional.empty();
        }
        OAuthFlows oAuthFlowsObject = new OAuthFlows();
        if (oAuthFlows.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(oAuthFlows.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    oAuthFlowsObject.addExtension(ext, extensions.get(ext));
                }
            }
        }

        getOAuthFlow(oAuthFlows.authorizationCode()).ifPresent(oAuthFlowsObject::setAuthorizationCode);
        getOAuthFlow(oAuthFlows.clientCredentials()).ifPresent(oAuthFlowsObject::setClientCredentials);
        getOAuthFlow(oAuthFlows.implicit()).ifPresent(oAuthFlowsObject::setImplicit);
        getOAuthFlow(oAuthFlows.password()).ifPresent(oAuthFlowsObject::setPassword);
        return Optional.of(oAuthFlowsObject);
    }

    public static Optional<OAuthFlow> getOAuthFlow(io.swagger.v3.oas.annotations.security.OAuthFlow oAuthFlow) {
        if (isEmpty(oAuthFlow)) {
            return Optional.empty();
        }
        OAuthFlow oAuthFlowObject = new OAuthFlow();
        if (StringUtils.isNotBlank(oAuthFlow.authorizationUrl())) {
            oAuthFlowObject.setAuthorizationUrl(oAuthFlow.authorizationUrl());
        }
        if (StringUtils.isNotBlank(oAuthFlow.refreshUrl())) {
            oAuthFlowObject.setRefreshUrl(oAuthFlow.refreshUrl());
        }
        if (StringUtils.isNotBlank(oAuthFlow.tokenUrl())) {
            oAuthFlowObject.setTokenUrl(oAuthFlow.tokenUrl());
        }
        if (oAuthFlow.extensions().length > 0) {
            Map<String, Object> extensions = AnnotationsUtils.getExtensions(oAuthFlow.extensions());
            if (extensions != null) {
                for (String ext : extensions.keySet()) {
                    oAuthFlowObject.addExtension(ext, extensions.get(ext));
                }
            }
        }

        getScopes(oAuthFlow.scopes()).ifPresent(oAuthFlowObject::setScopes);
        return Optional.of(oAuthFlowObject);
    }

    public static Optional<Scopes> getScopes(OAuthScope[] scopes) {
        if (isEmpty(scopes)) {
            return Optional.empty();
        }
        Scopes scopesObject = new Scopes();

        for (OAuthScope scope : scopes) {
            scopesObject.addString(scope.name(), scope.description());
        }
        return Optional.of(scopesObject);
    }

    private static SecurityScheme.In getIn(String value) {
        return Arrays.stream(SecurityScheme.In.values()).filter(i -> i.toString().equals(value)).findFirst().orElse(null);
    }

    private static SecurityScheme.Type getType(String value) {
        return Arrays.stream(SecurityScheme.Type.values()).filter(i -> i.toString().equals(value)).findFirst().orElse(null);
    }

    private static boolean isEmpty(io.swagger.v3.oas.annotations.security.OAuthFlows oAuthFlows) {
        if (oAuthFlows == null) {
            return true;
        }
        if (!isEmpty(oAuthFlows.implicit())) {
            return false;
        }
        if (!isEmpty(oAuthFlows.authorizationCode())) {
            return false;
        }
        if (!isEmpty(oAuthFlows.clientCredentials())) {
            return false;
        }
        if (!isEmpty(oAuthFlows.password())) {
            return false;
        }
        if (oAuthFlows.extensions().length > 0) {
            return false;
        }
        return true;
    }

    private static boolean isEmpty(io.swagger.v3.oas.annotations.security.OAuthFlow oAuthFlow) {
        if (oAuthFlow == null) {
            return true;
        }
        if (!StringUtils.isBlank(oAuthFlow.authorizationUrl())) {
            return false;
        }
        if (!StringUtils.isBlank(oAuthFlow.refreshUrl())) {
            return false;
        }
        if (!StringUtils.isBlank(oAuthFlow.tokenUrl())) {
            return false;
        }
        if (!isEmpty(oAuthFlow.scopes())) {
            return false;
        }
        if (oAuthFlow.extensions().length > 0) {
            return false;
        }
        return true;
    }

    private static boolean isEmpty(OAuthScope[] scopes) {
        if (scopes == null || scopes.length == 0) {
            return true;
        }
        return false;
    }

}
