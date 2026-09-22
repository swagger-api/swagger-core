package io.swagger.v3.oas.models.media;

import io.swagger.v3.oas.models.annotations.OpenAPI31;
import io.swagger.v3.oas.models.annotations.OpenAPI32;
import io.swagger.v3.oas.models.headers.Header;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Encoding
 *
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#encoding-object">Encoding (OpenAPI 3.0 specification)</a>
 */

public class Encoding {
    private String contentType;
    private Map<String, Header> headers;
    private StyleEnum style;
    private Boolean explode;
    private Boolean allowReserved;
    /**
     * Nested encoding for object properties (OpenAPI 3.2). MUST NOT coexist with
     * {@code prefixEncoding}/{@code itemEncoding}.
     *
     * @since 2.2.56 (OpenAPI 3.2)
     */
    @OpenAPI32
    private Map<String, Encoding> encoding;
    /**
     * Positional encoding for multipart array parts (OpenAPI 3.2). MUST NOT coexist
     * with {@code encoding}.
     *
     * @since 2.2.56 (OpenAPI 3.2)
     */
    @OpenAPI32
    private List<Encoding> prefixEncoding;
    /**
     * Encoding applied to array items beyond the {@code prefixEncoding} positions
     * (OpenAPI 3.2). MUST NOT coexist with {@code encoding}.
     *
     * @since 2.2.56 (OpenAPI 3.2)
     */
    @OpenAPI32
    private Encoding itemEncoding;
    private java.util.Map<String, Object> extensions = null;

    public enum StyleEnum {
        FORM("form"),
        SPACE_DELIMITED("spaceDelimited"),
        PIPE_DELIMITED("pipeDelimited"),
        DEEP_OBJECT("deepObject");

        private String value;

        StyleEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static StyleEnum fromString(String value) {
            for (StyleEnum e : values()) {
                if (e.value.equals(value)) {
                    return e;
                }
            }
            return null;
        }
    }

    public Encoding() {
    }

    public Encoding contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Encoding headers(Map<String, Header> headers) {
        this.headers = headers;
        return this;
    }

    public Map<String, Header> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Header> headers) {
        this.headers = headers;
    }

    public Encoding addHeader(String name, Header header) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(name, header);
        return this;
    }

    public Encoding style(StyleEnum style) {
        this.style = style;
        return this;
    }

    public StyleEnum getStyle() {
        return style;
    }

    public void setStyle(StyleEnum style) {
        this.style = style;
    }

    public Encoding explode(Boolean explode) {
        this.explode = explode;
        return this;
    }

    public Boolean getExplode() {
        return explode;
    }

    public void setExplode(Boolean explode) {
        this.explode = explode;
    }

    public Encoding allowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
        return this;
    }

    public Boolean getAllowReserved() {
        return allowReserved;
    }

    public void setAllowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
    }

    @OpenAPI32
    public Map<String, Encoding> getEncoding() {
        return encoding;
    }

    @OpenAPI32
    public void setEncoding(Map<String, Encoding> encoding) {
        this.encoding = encoding;
    }

    @OpenAPI32
    public Encoding encoding(Map<String, Encoding> encoding) {
        this.encoding = encoding;
        return this;
    }

    public Encoding addEncoding(String name, Encoding encodingItem) {
        if (this.encoding == null) {
            this.encoding = new LinkedHashMap<>();
        }
        this.encoding.put(name, encodingItem);
        return this;
    }

    @OpenAPI32
    public List<Encoding> getPrefixEncoding() {
        return prefixEncoding;
    }

    @OpenAPI32
    public void setPrefixEncoding(List<Encoding> prefixEncoding) {
        this.prefixEncoding = prefixEncoding;
    }

    @OpenAPI32
    public Encoding prefixEncoding(List<Encoding> prefixEncoding) {
        this.prefixEncoding = prefixEncoding;
        return this;
    }

    public Encoding addPrefixEncoding(Encoding prefixEncodingItem) {
        if (this.prefixEncoding == null) {
            this.prefixEncoding = new java.util.ArrayList<>();
        }
        this.prefixEncoding.add(prefixEncodingItem);
        return this;
    }

    @OpenAPI32
    public Encoding getItemEncoding() {
        return itemEncoding;
    }

    @OpenAPI32
    public void setItemEncoding(Encoding itemEncoding) {
        this.itemEncoding = itemEncoding;
    }

    @OpenAPI32
    public Encoding itemEncoding(Encoding itemEncoding) {
        this.itemEncoding = itemEncoding;
        return this;
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

    public Encoding extensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
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
        Encoding encoding = (Encoding) o;
        return Objects.equals(this.contentType, encoding.contentType) &&
                Objects.equals(this.headers, encoding.headers) &&
                Objects.equals(this.style, encoding.style) &&
                Objects.equals(this.explode, encoding.explode) &&
                Objects.equals(this.encoding, encoding.encoding) &&
                Objects.equals(this.prefixEncoding, encoding.prefixEncoding) &&
                Objects.equals(this.itemEncoding, encoding.itemEncoding) &&
                Objects.equals(this.extensions, encoding.extensions) &&
                Objects.equals(this.allowReserved, encoding.allowReserved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, headers, style, explode, allowReserved, encoding, prefixEncoding, itemEncoding, extensions);
    }

    @Override
    public String toString() {
        return "Encoding{" +
                "contentType='" + contentType + '\'' +
                ", headers=" + headers +
                ", style='" + style + '\'' +
                ", explode=" + explode +
                ", allowReserved=" + allowReserved +
                ", encoding=" + encoding +
                ", prefixEncoding=" + prefixEncoding +
                ", itemEncoding=" + itemEncoding +
                ", extensions=" + extensions +
                '}';
    }
}
