package io.swagger.jaxrs2;

import io.swagger.converter.ModelConverters;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.models.ExternalDocumentation;
import io.swagger.oas.models.examples.Example;
import io.swagger.oas.models.info.Contact;
import io.swagger.oas.models.info.Info;
import io.swagger.oas.models.info.License;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.media.MediaType;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.oas.models.responses.ApiResponses;
import io.swagger.oas.models.servers.Server;
import io.swagger.oas.models.servers.ServerVariable;
import io.swagger.oas.models.servers.ServerVariables;
import io.swagger.oas.models.tags.Tag;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by RafaelLopez on 5/27/17.
 */
public class OperationParser {

	public static Optional<List<Parameter>> getParametersList(io.swagger.oas.annotations.Parameter[] parameters) {
		if (parameters == null) {
			return Optional.empty();
		}
		List<Parameter> parametersObject = new ArrayList<>();
		for (io.swagger.oas.annotations.Parameter parameter : parameters) {
			getParameter(parameter).ifPresent(param -> parametersObject.add(param));

		}
		return Optional.of(parametersObject);
	}

	public static Optional<Parameter> getParameter(io.swagger.oas.annotations.Parameter parameter) {
		if (parameter == null) {
			return Optional.empty();
		}
		Parameter parameterObject = new Parameter();
		if (StringUtils.isNotBlank(parameter.description())) {
			parameterObject.setDescription(parameter.description());
		}
		if (StringUtils.isNotBlank(parameter.name())) {
			parameterObject.setName(parameter.name());
		}
		if (StringUtils.isNotBlank(parameter.in())) {
			parameterObject.setIn(parameter.in());
		}
		parameterObject.setDeprecated(parameter.deprecated());
		parameterObject.setRequired(parameter.required());
		parameterObject.setStyle(StringUtils.isNoneBlank(parameter.style()) ? Parameter.StyleEnum.valueOf(parameter.style()) : null);
		parameterObject.setAllowEmptyValue(parameter.allowEmptyValue());
		parameterObject.setAllowReserved(parameter.allowReserved());
		parameterObject.setExplode(parameter.explode());
		parameterObject.setContent(getContents(parameter.content()).get());

		return Optional.of(parameterObject);
	}

	public static Optional<Map<String, Schema>> getSchema(io.swagger.oas.annotations.media.Schema schema) {
		if (schema == null) {
			return Optional.empty();
		}
		if (schema.implementation() != Void.class) {
			return Optional.of(ModelConverters.getInstance().readAll(schema.implementation()));
		}
		return Optional.empty();
	}

	public static Optional<Set<Tag>> getTags(String[] tags) {
		if (tags == null) {
			return Optional.empty();
		}
		Set<Tag> tagsList = new LinkedHashSet<>();
		for (String tag : tags) {
			Tag tagObject = new Tag();
			tagObject.setDescription(tag);
			tagObject.setName(tag);
			tagsList.add(tagObject);
		}
		return Optional.of(tagsList);
	}

	public static Optional<List<Server>> getServers(io.swagger.oas.annotations.servers.Server[] servers) {
		if (servers == null) {
			return Optional.empty();
		}
		List<Server> serverObjects = new ArrayList<>();

		for (io.swagger.oas.annotations.servers.Server server : servers) {
			getServer(server).ifPresent(serverObject -> serverObjects.add(serverObject));
		}
		return Optional.of(serverObjects);
	}

	public static Optional<Server> getServer(io.swagger.oas.annotations.servers.Server server) {
		if (server == null) {
			return Optional.empty();
		}

		Server serverObject = new Server();
		if (StringUtils.isNotBlank(server.url())) {
			serverObject.setUrl(server.url());
		}
		if (StringUtils.isNotBlank(server.description())) {
			serverObject.setDescription(server.description());
		}
		io.swagger.oas.annotations.servers.ServerVariable[] serverVariables = server.variables();
		ServerVariables serverVariablesObject = new ServerVariables();
		for (io.swagger.oas.annotations.servers.ServerVariable serverVariable : serverVariables) {
			ServerVariable serverVariableObject = new ServerVariable();
			if (StringUtils.isNotBlank(serverVariable.description())) {
				serverVariableObject.setDescription(serverVariable.description());
			}
			serverVariablesObject.addServerVariable(serverVariable.name(), serverVariableObject);
		}
		serverObject.setVariables(serverVariablesObject);

		return Optional.of(serverObject);
	}

	public static Optional<ExternalDocumentation> getExternalDocumentation(io.swagger.oas.annotations.ExternalDocumentation externalDocumentation) {
		if (externalDocumentation == null) {
			return Optional.empty();
		}
		ExternalDocumentation external = new ExternalDocumentation();
		if (StringUtils.isNotBlank(externalDocumentation.description())) {
			external.setDescription(externalDocumentation.description());
		}
		if (StringUtils.isNotBlank(externalDocumentation.url())) {
			external.setUrl(externalDocumentation.url());
		}
		return Optional.of(external);
	}

	public static Optional<RequestBody> getRequestBody(io.swagger.oas.annotations.parameters.RequestBody requestBody) {
		if (requestBody == null) {
			return Optional.empty();
		}
		RequestBody requestBodyObject = new RequestBody();
		if (StringUtils.isNotBlank(requestBody.description())) {
			requestBodyObject.setDescription(requestBody.description());
		}
		requestBodyObject.setRequired(requestBody.required());
		getContents(requestBody.content()).ifPresent(content -> requestBodyObject.setContent(content));
		return Optional.of(requestBodyObject);
	}

	public static Optional<ApiResponses> getApiResponses(final io.swagger.oas.annotations.responses.ApiResponse[] responses, io.swagger.oas.annotations.links.Link links) {
		if (responses == null) {
			return Optional.empty();
		}
		ApiResponses apiResponsesObject = new ApiResponses();
		for (io.swagger.oas.annotations.responses.ApiResponse response : responses) {
			ApiResponse apiResponseObject = new ApiResponse();
			Content content = getContent(response.content()).get();
			apiResponseObject.content(content);
			if (StringUtils.isNotBlank(response.description())) {
				apiResponseObject.setDescription(response.description());
			}

			apiResponsesObject.addApiResponse(response.responseCode(), apiResponseObject);
		}
		return Optional.of(apiResponsesObject);
	}

	public static Optional<Content> getContents(io.swagger.oas.annotations.media.Content[] contents) {
		if (contents == null) {
			return Optional.empty();
		}
		Content contentObject = new Content();
		for (io.swagger.oas.annotations.media.Content content : contents) {
			ExampleObject[] examples = content.examples();
			for (ExampleObject example : examples) {
				getMediaType(example).ifPresent(mediaType -> contentObject.addMediaType(content.mediaType(), mediaType));
			}
		}
		return Optional.of(contentObject);
	}

	public static Optional<Content> getContent(io.swagger.oas.annotations.media.Content annotationContent) {
		if (annotationContent == null) {
			Optional.empty();
		}
		Content content = new Content();
		// TODO - Add the Schema from the Content annotation an use the ModelConverter
		if (annotationContent != null) {
			ExampleObject[] examples = annotationContent.examples();
			for (ExampleObject example : examples) {
				getMediaType(example).ifPresent(mediaType -> content.addMediaType(annotationContent.mediaType(), mediaType));
			}
		}
		return Optional.of(content);
	}

	public static Optional<MediaType> getMediaType(ExampleObject example) {
		if (example == null) {
			return Optional.empty();
		}
		if (StringUtils.isNotBlank(example.name())) {
			MediaType mediaType = new MediaType();
			Example exampleObject = new Example();
			if (StringUtils.isNotBlank(example.name())) {
				exampleObject.setDescription(example.name());
			}
			if (StringUtils.isNotBlank(example.summary())) {
				exampleObject.setSummary(example.summary());
			}
			if (StringUtils.isNotBlank(example.externalValue())) {
				exampleObject.setExternalValue(example.externalValue());
			}
			if (StringUtils.isNotBlank(example.value())) {
				exampleObject.setValue(example.value());
			}

			mediaType.addExamples(example.name(), exampleObject);
			return Optional.of(mediaType);
		}
		return Optional.empty();
	}

	public static Optional<Info> getInfo(io.swagger.oas.annotations.info.Info info) {
		if (info == null) {
			return Optional.empty();
		}
		Info infoObject = new Info();
		if (StringUtils.isNotBlank(info.description())) {
			infoObject.setDescription(info.description());
		}
		if (StringUtils.isNotBlank(info.termsOfService())) {
			infoObject.setTermsOfService(info.termsOfService());
		}
		if (StringUtils.isNotBlank(info.title())) {
			infoObject.setTitle(info.title());
		}
		if (StringUtils.isNotBlank(info.version())) {
			infoObject.setVersion(info.version());
		}
		getContact(info.contact()).ifPresent(contact -> infoObject.setContact(contact));
		getLicense(info.license()).ifPresent(license -> infoObject.setLicense(license));

		return Optional.of(infoObject);
	}

	public static Optional<Contact> getContact(io.swagger.oas.annotations.info.Contact contact) {
		if (contact == null) {
			return Optional.empty();
		}
		Contact contactObject = new Contact();
		if (StringUtils.isNotBlank(contact.email())) {
			contactObject.setEmail(contact.email());
		}
		if (StringUtils.isNotBlank(contact.name())) {
			contactObject.setName(contact.name());
		}
		if (StringUtils.isNotBlank(contact.url())) {
			contactObject.setUrl(contact.url());
		}
		return Optional.of(contactObject);
	}

	public static Optional<License> getLicense(io.swagger.oas.annotations.info.License license) {
		if (license == null) {
			return Optional.empty();
		}
		License licenseObject = new License();
		if (StringUtils.isNotBlank(license.name())) {
			licenseObject.setName(license.name());
		}
		if (StringUtils.isNotBlank(license.url())) {
			licenseObject.setUrl(license.url());
		}
		return Optional.of(licenseObject);
	}
}
