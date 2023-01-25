package io.swagger.v3.oas.annotations.enums;

public enum SecuritySchemeType {
    DEFAULT(""),
    APIKEY("apiKey"),
    HTTP("http"),
    OPENIDCONNECT("openIdConnect"),
    MUTUALTLS("mutualTLS"),
    OAUTH2("oauth2");

    private String value;

    SecuritySchemeType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
