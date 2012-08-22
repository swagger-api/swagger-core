/**
 *  Copyright 2012 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.swagger.jaxrs;

import com.wordnik.swagger.core.Documentation;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces("application/json")
public class ApiHelpMessageBodyWriter implements MessageBodyWriter<Documentation> {
    public long getSize(Documentation arg0, Class<?> arg1, Type arg2, Annotation[] arg3,
                        MediaType arg4) {
        try {
            return getStringRepresentation(arg0).length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Write a type to an HTTP response. The response header map is mutable
     * but any changes must be made before writing to the output stream since
     * the headers will be flushed prior to writing the response body.
     *
     * @param documentation            the instance to write.
     * @param type         the class of object that is to be written.
     * @param genericType  the type of object to be written, obtained either
     *                     by reflection of a resource method return type or by inspection
     *                     of the returned instance. {@link javax.ws.rs.core.GenericEntity}
     *                     provides a way to specify this information at runtime.
     * @param annotations  an array of the annotations on the resource
     *                     method that returns the object.
     * @param mediaType    the media type of the HTTP entity.
     * @param httpHeaders  a mutable map of the HTTP response headers.
     * @param entityStream the {@link java.io.OutputStream} for the HTTP entity. The
     *                     implementation should not close the output stream.
     * @throws java.io.IOException if an IO error arises
     * @throws javax.ws.rs.WebApplicationException
     *                             if a specific
     *                             HTTP error response needs to be produced. Only effective if thrown prior
     *                             to the response being committed.
     */
    public void writeTo(Documentation documentation, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(entityStream));
        bw.write(getStringRepresentation(documentation));
        bw.flush();
    }

    public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2,
                               MediaType arg3) {
        return arg0.equals(Documentation.class) && arg3.equals(MediaType.APPLICATION_JSON_TYPE);
    }

    private String getStringRepresentation(Documentation documentation) throws IOException {
        return com.wordnik.swagger.core.util.JsonUtil.getJsonMapper().writeValueAsString(documentation);
    }
}