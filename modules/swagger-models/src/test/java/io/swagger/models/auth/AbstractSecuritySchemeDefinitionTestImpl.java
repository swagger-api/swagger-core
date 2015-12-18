package io.swagger.models.auth;

public class AbstractSecuritySchemeDefinitionTestImpl extends AbstractSecuritySchemeDefinition {
	String type;
	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type=type;

	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	

}
