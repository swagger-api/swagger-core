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

    /**
     * sets this Encoding's contentType property to the given contentType and
     * returns this instance of Encoding
     *
     * @param String contentType
     * @return Encoding
     */
    public Encoding contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
    
    /**
     * returns the contentType property from a Encoding instance.
     *
     * @return String contentType
     **/
    public String getContentType() {
        return contentType;
    }

    /**
     * sets this Encoding's contentType property to the given contentType.
     *
     * @param String contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * sets this Encoding's headers property to the given headers and
     * returns this instance of Encoding
     *
     * @param Map&lt;String, Header&gt; headers
     * @return Encoding
     */
    public Encoding headers(Map<String, Header> headers) {
        this.headers = headers;
        return this;
    }
    
    /**
     * returns the headers property from a Encoding instance.
     *
     * @return Map&lt;String, Header&gt; headers
     **/
    public Map<String, Header> getHeaders() {
        return headers;
    }

    /**
     * sets this Encoding's headers property to the given headers.
     *
     * @param Map&lt;String, Header&gt; headers
     */
    public void setHeaders(Map<String, Header> headers) {
        this.headers = headers;
    }
    
    /**
     * sets this Encoding's style property to the given style and
     * returns this instance of Encoding
     *
     * @param String style
     * @return Encoding
     */
    public Encoding style(String style) {
        this.style = style;
        return this;
    }
    
    /**
     * returns the style property from a Encoding instance.
     *
     * @return String style
     **/
    public String getStyle() {
        return style;
    }
    
    /**
     * sets this Encoding's style property to the given style.
     *
     * @param String style
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * sets this Encoding's explode property to the given explode and
     * returns this instance of Encoding
     *
     * @param Boolean explode
     * @return Encoding
     */
    public Encoding explode(Boolean explode) {
        this.explode = explode;
        return this;
    }

    /**
     * returns the explode property from a Encoding instance.
     *
     * @return Boolean explode
     **/
    public Boolean getExplode() {
        return explode;
    }

    /**
     * sets this Encoding's explode property to the given explode.
     *
     * @param Boolean explode
     */
    public void setExplode(Boolean explode) {
        this.explode = explode;
    }

    /**
     * sets this Encoding's allowReserved property to the given allowReserved and
     * returns this instance of Encoding
     *
     * @param Boolean allowReserved
     * @return Encoding
     */
    public Encoding allowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
        return this;
    }

    /**
     * returns the allowReserved property from a Encoding instance.
     *
     * @return Boolean allowReserved
     **/
    public Boolean getAllowReserved() {
        return allowReserved;
    }

    /**
     * sets this Encoding's allowReserved property to the given allowReserved.
     *
     * @param Boolean allowReserved
     */
    public void setAllowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
    }
    
    /**
     * returns the extensions property from a Encoding instance.
     *
     * @return Map&lt;String, Object&gt; extensions
     **/
    public java.util.Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Adds the given extension value to this Encoding's map of extensions, with the given name as its key.
     *
     * @param String name
     * @param Object value
     */
    public void addExtension(String name, Object value) {
        if(this.extensions == null) {
            this.extensions = new java.util.HashMap<>();
        }
        this.extensions.put(name, value);
    }

    /**
     * sets this Encoding's extensions property to the given extensions.
     *
     * @param Map&lt;String, Object&gt; extensions
     */
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
}

