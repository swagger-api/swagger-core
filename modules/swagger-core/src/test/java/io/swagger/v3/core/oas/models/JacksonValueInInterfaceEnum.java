
package io.swagger.v3.core.oas.models;

public enum JacksonValueInInterfaceEnum implements InterfaceWithJacksonValue {

	TEN(10);

	private final int jsonValue;

	JacksonValueInInterfaceEnum(int jsonValue) {
		this.jsonValue = jsonValue;
	}

	@Override
	public int getJsonValue() {
		return jsonValue;
	}
}
