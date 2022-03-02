/**
 * Copyright 2021 SmartBear Software
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

package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;

@Schema(name = "UploadRequest", title="Schema for Upload")
public class UploadRequest {

	@Hidden
	@FormDataParam("picture")
	private FormDataContentDisposition disposition;

	public FormDataContentDisposition getDisposition() {
		return disposition;
	}
	public void setDisposition(FormDataContentDisposition disposition) {
		this.disposition = disposition;
	}


	@FormDataParam("name")
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	@Schema(name="picture", type="string", format="binary")
	@FormDataParam("picture")
	private InputStream upload;


	public InputStream getUpload() {
		return upload;
	}
	public void setUpload(InputStream upload) {
		this.upload = upload;
	}

	@Override
	public String toString() {
		final String NL = System.lineSeparator();
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("name: ").append(name).append(NL);
		return sb.toString();
	}

}
