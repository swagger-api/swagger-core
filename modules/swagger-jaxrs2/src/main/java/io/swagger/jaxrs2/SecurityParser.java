package io.swagger.jaxrs2;

import io.swagger.oas.models.security.OAuthFlow;
import io.swagger.oas.models.security.OAuthFlows;
import io.swagger.oas.models.security.Scopes;
import io.swagger.oas.models.security.SecurityRequirement;
import io.swagger.oas.models.security.SecurityScheme;
import io.swagger.oas.annotations.security.OAuthScope;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
		if (securityRequirement.scopes().length > 0) {
			securityRequirementObject.addList(securityRequirement.name(), Arrays.asList(securityRequirement.scopes()));
		} else {
			securityRequirementObject.addList(securityRequirement.name());
		}
		securityRequirements.add(securityRequirementObject);

		return Optional.of(securityRequirements);
	}

	public static Optional<SecurityScheme> getSecurityScheme(io.swagger.oas.annotations.security.SecurityScheme securityScheme) {
		if (securityScheme == null) {
			return Optional.empty();
		}
		SecurityScheme securitySchemeObject = new SecurityScheme();

		if (StringUtils.isNotBlank(securityScheme.in())) {
			securitySchemeObject.setIn(getIn(securityScheme.in()));
		}
		if (StringUtils.isNotBlank(securityScheme.type())) {
			securitySchemeObject.setType(getType(securityScheme.type()));
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
		getOAuthFlows(securityScheme.flows()).ifPresent(securitySchemeObject::setFlows);
		return Optional.of(securitySchemeObject);
	}

	public static Optional<OAuthFlows> getOAuthFlows(io.swagger.oas.annotations.security.OAuthFlows oAuthFlows) {
		if (isEmpty(oAuthFlows)) {
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

	private static boolean isEmpty(io.swagger.oas.annotations.security.OAuthFlows oAuthFlows) {
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
		return true;
	}

	private static boolean isEmpty(io.swagger.oas.annotations.security.OAuthFlow oAuthFlow) {
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
		return true;
	}

	private static boolean isEmpty(OAuthScope[] scopes) {
		if (scopes == null || scopes.length == 0) {
			return true;
		}
		return false;
	}

}
