package io.swagger.jaxrs2;

import io.swagger.oas.models.security.OAuthFlow;
import io.swagger.oas.models.security.OAuthFlows;
import io.swagger.oas.models.security.Scopes;
import io.swagger.oas.models.security.SecurityRequirement;
import io.swagger.oas.models.security.SecurityScheme;
import io.swagger.oas.annotations.security.OAuthScope;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by RafaelLopez on 5/26/17.
 */
public class SecurityParser {
	public static final String SCOPE_NAME = "name";
	public static final String SCOPE_DESCRIPTION = "description";

	public static Optional<List<SecurityRequirement>> getSecurityRequirement(io.swagger.oas.annotations.security.SecurityRequirement securityRequirement) {
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

	public static Optional<SecurityScheme> getSecurityScheme(io.swagger.oas.annotations.security.SecurityScheme securityScheme) {
		if (securityScheme == null) {
			return Optional.empty();
		}
		SecurityScheme securitySchemeObject = new SecurityScheme();
		if (StringUtils.isNotBlank(securityScheme.bearerFormat())) {
			securitySchemeObject.setBearerFormat(securityScheme.bearerFormat());
		}
		if (StringUtils.isNotBlank(securityScheme.description())) {
			securitySchemeObject.setDescription(securityScheme.description());
		}
		if (StringUtils.isNotBlank(securityScheme.name())) {
			securitySchemeObject.setName(securityScheme.name());
		}
		getOAuthFlows(securityScheme.flows()).ifPresent(securitySchemeObject::setFlows);
		return Optional.of(securitySchemeObject);
	}

	public static Optional<OAuthFlows> getOAuthFlows(io.swagger.oas.annotations.security.OAuthFlows oAuthFlows) {
		if (oAuthFlows == null) {
			return Optional.empty();
		}
		OAuthFlows oAuthFlowsObject = new OAuthFlows();
		getOAuthFlow(oAuthFlows.authorizationCode()).ifPresent(oAuthFlowsObject::setAuthorizationCode);
		getOAuthFlow(oAuthFlows.clientCredentials()).ifPresent(oAuthFlowsObject::setClientCredentials);
		getOAuthFlow(oAuthFlows.implicit()).ifPresent(oAuthFlowsObject::setImplicit);
		getOAuthFlow(oAuthFlows.password()).ifPresent(oAuthFlowsObject::setPassword);
		return Optional.of(oAuthFlowsObject);
	}

	public static Optional<OAuthFlow> getOAuthFlow(io.swagger.oas.annotations.security.OAuthFlow oAuthFlow) {
		if (oAuthFlow == null) {
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
		getScopes(oAuthFlow.scopes()).ifPresent(oAuthFlowObject::setScopes);
		return Optional.of(oAuthFlowObject);
	}

	public static Optional<Scopes> getScopes(OAuthScope[] scopes) {
		if (scopes == null) {
			return Optional.empty();
		}
		Scopes scopesObject = new Scopes();
		
		for (OAuthScope scope : scopes) {
			scopesObject.addString(scope.name(), scope.description());
		}
		return Optional.of(scopesObject);
	}

}
