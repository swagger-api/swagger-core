/**
 * Copyright 2017 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.oas.models.media;

import io.swagger.oas.models.headers.Header;

import java.util.Map;
import java.util.Objects;

/**
 * Encoding
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.md#encodingObject"
 */


public class Encoding {
    private String contentType;
    private Map<String, Header> headers;
    private String style;
    private Boolean explode;
    private Boolean allowReserved;
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

    public Encoding style(String style) {
        this.style = style;
        return this;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
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

    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    public void addExtension(String name, Object value) {
        if(this.extensions == null) {
            this.extensions = new java.util.HashMap<>();
        }
        this.extensions.put(name, value);
    }

    public void setExtensions(java.util.Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        return "Encoding{" +
                "contentType='" + contentType + '\'' +
                ", headers=" + headers +
                ", style='" + style + '\'' +
                ", explode=" + explode +
                ", allowReserved=" + allowReserved +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Encoding)) {
            return false;
        }

        Encoding encoding = (Encoding) o;

        if (contentType != null ? !contentType.equals(encoding.contentType) : encoding.contentType != null) {
            return false;
        }
        if (headers != null ? !headers.equals(encoding.headers) : encoding.headers != null) {
            return false;
        }
        if (style != null ? !style.equals(encoding.style) : encoding.style != null) {
            return false;
        }
        if (explode != null ? !explode.equals(encoding.explode) : encoding.explode != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(encoding.extensions) : encoding.extensions != null) {
            return false;
        }
        return allowReserved != null ? allowReserved.equals(encoding.allowReserved) : encoding.allowReserved == null;

    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (style != null ? style.hashCode() : 0);
        result = 31 * result + (explode != null ? explode.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        result = 31 * result + (allowReserved != null ? allowReserved.hashCode() : 0);
        return result;
    }
}

