package io.swagger.v3.oas.models.tags;

import io.swagger.v3.oas.models.annotations.OpenAPI31;
import io.swagger.v3.oas.models.annotations.OpenAPI32;
import io.swagger.v3.oas.models.ExternalDocumentation;

import java.util.Objects;

/**
 * Tag
 *
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#tag-object">Tag (OpenAPI 3.0 specification)</a>
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/3.1.1/versions/3.1.1.md#tag-object">Tag (OpenAPI 3.1 specification)</a>
 */

public class Tag {
    private String name = null;
    @OpenAPI32
    private String summary = null;
    private String description = null;
    private ExternalDocumentation externalDocs = null;
    @OpenAPI32
    private String parent = null;
    @OpenAPI32
    private String kind = null;
    private java.util.Map<String, Object> extensions = null;

    /**
     * returns the name property from a Tag instance.
     *
     * @return String name
     **/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tag name(String name) {
        this.name = name;
        return this;
    }

    /**
     * returns the summary property from a Tag instance.
     *
     * @since 2.2.56 (OpenAPI 3.2)
     * @return String summary
     **/

    @OpenAPI32
    public String getSummary() {
        return summary;
    }

    @OpenAPI32
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @OpenAPI32
    public Tag summary(String summary) {
        this.summary = summary;
        return this;
    }

    /**
     * returns the description property from a Tag instance.
     *
     * @return String description
     **/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Tag description(String description) {
        this.description = description;
        return this;
    }

    /**
     * returns the externalDocs property from a Tag instance.
     *
     * @return ExternalDocumentation externalDocs
     **/

    public ExternalDocumentation getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }

    public Tag externalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
        return this;
    }

    /**
     * returns the parent property from a Tag instance.
     *
     * @since 2.2.56 (OpenAPI 3.2)
     * @return String parent
     **/

    @OpenAPI32
    public String getParent() {
        return parent;
    }

    @OpenAPI32
    public void setParent(String parent) {
        this.parent = parent;
    }

    @OpenAPI32
    public Tag parent(String parent) {
        this.parent = parent;
        return this;
    }

    /**
     * returns the kind property from a Tag instance.
     *
     * @since 2.2.56 (OpenAPI 3.2)
     * @return String kind
     **/

    @OpenAPI32
    public String getKind() {
        return kind;
    }

    @OpenAPI32
    public void setKind(String kind) {
        this.kind = kind;
    }

    @OpenAPI32
    public Tag kind(String kind) {
        this.kind = kind;
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return Objects.equals(this.name, tag.name) &&
                Objects.equals(this.summary, tag.summary) &&
                Objects.equals(this.description, tag.description) &&
                Objects.equals(this.externalDocs, tag.externalDocs) &&
                Objects.equals(this.parent, tag.parent) &&
                Objects.equals(this.kind, tag.kind) &&
                Objects.equals(this.extensions, tag.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, summary, description, externalDocs, parent, kind, extensions);
    }

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    public void addExtension(String name, Object value) {
        if (name == null || name.isEmpty() || !name.startsWith("x-")) {
            return;
        }
        if (this.extensions == null) {
            this.extensions = new java.util.LinkedHashMap<>();
        }
        this.extensions.put(name, value);
    }

    @OpenAPI31
    public void addExtension31(String name, Object value) {
        if (name != null && (name.startsWith("x-oas-") || name.startsWith("x-oai-"))) {
            return;
        }
        addExtension(name, value);
    }

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public Tag extensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Tag {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    externalDocs: ").append(toIndentedString(externalDocs)).append("\n");
        sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

