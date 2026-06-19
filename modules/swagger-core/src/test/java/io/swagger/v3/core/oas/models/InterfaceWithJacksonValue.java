package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.annotation.JsonValue;

public interface InterfaceWithJacksonValue {

	@JsonValue
	int getJsonValue();
}
