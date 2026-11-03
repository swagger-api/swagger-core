package io.swagger.v3.oas.models;

public enum SpecVersion {
    V30,
    V31;

    /**
     * Maps an {@code openapi} version string (e.g. {@code "3.0.1"}, {@code "3.1.0"})
     * to the corresponding {@link SpecVersion}.
     *
     * <p>Returns {@code null} for {@code null}, empty or unrecognized values
     * (e.g. {@code "4.0.0"}, {@code "3.x"}), so callers can leave the current
     * state unchanged in that case.</p>
     *
     * <p>When a new constant is added here (e.g. {@code V32} for OpenAPI 3.2),
     * extend the mapping below accordingly.</p>
     *
     * @param openapi the value of the {@code openapi} field, may be {@code null}
     * @return the matching {@link SpecVersion}, or {@code null} if unrecognized
     */
    public static SpecVersion fromOpenapi(String openapi) {
        if (openapi == null || openapi.isEmpty()) {
            return null;
        }
        if (openapi.startsWith("3.0.")) {
            return V30;
        }
        if (openapi.startsWith("3.1.")) {
            return V31;
        }
        return null;
    }
}
